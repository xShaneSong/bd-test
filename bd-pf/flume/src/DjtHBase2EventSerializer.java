/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license
 * agreements. See the NOTICE file distributed with this work for additional information regarding
 * copyright ownership. The ASF licenses this file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License. You may obtain a
 * copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package org.apache.flume.sink.hbase2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.FlumeException;
import org.apache.flume.conf.ComponentConfiguration;
import org.apache.flume.sink.hbase2.HBase2EventSerializer;
import org.apache.flume.sink.hbase2.SimpleHBase2EventSerializer;
import org.apache.flume.sink.hbase2.SimpleRowKeyGenerator;
// import org.apache.flume.sink.hbase2.utils.JsonUtils;
import org.apache.hadoop.hbase.client.Increment;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;

/**
 * A simple serializer to be used with the AsyncHBaseSink that returns puts from an event, by
 * writing the event body into it. The headers are discarded. It also updates a row in hbase which
 * acts as an event counter.
 *
 * Takes optional parameters:
 * <p>
 * <tt>rowPrefix:</tt> The prefix to be used. Default: <i>default</i>
 * <p>
 * <tt>incrementRow</tt> The row to increment. Default: <i>incRow</i>
 * <p>
 * <tt>suffix:</tt> <i>uuid/random/timestamp.</i>Default: <i>uuid</i>
 * <p>
 *
 * Mandatory parameters:
 * <p>
 * <tt>cf:</tt>Column family.
 * <p>
 * Components that have no defaults and will not be used if absent: <tt>payloadColumn:</tt> Which
 * column to put payload in. If it is not present, event data will not be written.
 * <p>
 * <tt>incrementColumn:</tt> Which column to increment. If this is absent, it means no column is
 * incremented.
 */
public class DjtHBase2EventSerializer implements HBase2EventSerializer {

    private String rowPrefix;
    private byte[] incrementRow;
    private byte[] cf;
    private byte[] plCol;
    private byte[] incCol;
    private SimpleHBase2EventSerializer.KeyType keyType;
    private byte[] payload;

    private Logger logger = LoggerFactory.getLogger(DjtHBase2EventSerializer.class);

    @Override
    public void configure(Context context) {

        logger.info("################### DjtHBase2EventSerializer [configure] ###################");
        rowPrefix = context.getString("rowPrefix", "default");
        incrementRow = context.getString("incrementRow", "incRow").getBytes(Charsets.UTF_8);
        String suffix = context.getString("suffix", "uuid");

        String payloadColumn = context.getString("payloadColumn", "pCol");
        String incColumn = context.getString("incrementColumn", "iCol");
        if (payloadColumn != null && !payloadColumn.isEmpty()) {
            switch (suffix) {
                case "timestamp":
                    keyType = SimpleHBase2EventSerializer.KeyType.TS;
                    break;
                case "random":
                    keyType = SimpleHBase2EventSerializer.KeyType.RANDOM;
                    break;
                case "nano":
                    keyType = SimpleHBase2EventSerializer.KeyType.TSNANO;
                    break;
                default:
                    keyType = SimpleHBase2EventSerializer.KeyType.UUID;
                    break;
            }
            plCol = payloadColumn.getBytes(Charsets.UTF_8);
        }
        if (incColumn != null && !incColumn.isEmpty()) {
            incCol = incColumn.getBytes(Charsets.UTF_8);
        }
    }

    @Override
    public void configure(ComponentConfiguration conf) {
        // TODO Auto-generated method stub
    }

    @Override
    public void initialize(Event event, byte[] cf) {
        logger.info("################### DjtHBase2EventSerializer [initialize] ###################");
        this.payload = event.getBody();
        this.cf = cf;
    }

    @Override
    public List<Row> getActions() throws FlumeException {
        logger.info("################### DjtHBase2EventSerializer [getActions] ###################");
        
        List<Row> actions = new LinkedList<>();
        if (plCol != null) {
            byte[] rowKey;
            try {
                // 解析列字段
                String[] columns = new String(this.plCol).split(",");
                // logger.info("columns={},lenth={}", JsonUtils.toString(columns), columns.length);
                // 解析每列对应的值
                String[] values = new String(this.payload).split(",");
                // logger.info("values={},lenth={}", JsonUtils.toString(values), values.length);
                // 判断数据与字段长度是否一致
                if (columns.length != values.length) {
                    return actions;
                }
                // 时间
                String datetime = values[0];
                // 用户id
                String userId = values[1];
                // 自定义生成RowKey
                rowKey = SimpleRowKeyGenerator.getDjtRowKey(userId, datetime);
                Put put = new Put(rowKey);
                for (int i = 0; i < columns.length; i++) {
                    byte[] colColumn = columns[i].getBytes();
                    byte[] colValue = values[i].getBytes(StandardCharsets.UTF_8);
                    put.addColumn(cf, colColumn, colValue);
                    actions.add(put);
                }
            } catch (Exception e) {
                logger.error("##############", e);
            }

        }
        return actions;
    }

    @Override
    public List<Increment> getIncrements() {
        logger.info("################### DjtHBase2EventSerializer [getIncrements] ###################");
        List<Increment> incs = new LinkedList<>();
        if (incCol != null) {
            Increment inc = new Increment(incrementRow);
            inc.addColumn(cf, incCol, 1);
            incs.add(inc);
        }
        return incs;
    }

    @Override
    public void close() {
        logger.info("################### DjtHBase2EventSerializer [getIncrements] ###################");
    }
}
