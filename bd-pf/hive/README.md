CREATE EXTERNAL TABLE sogoulogs (
    id STRING,
    datetime STRING,
    userid STRING,
    searchname STRING,
    retorder STRING,
    cliorder STRING,
    cliurl STRING
)
STORED BY 'org.apache.hadoop.hive.hbase.HBaseStorageHandler'
WITH SERDEPROPERTIES (
    "hbase.columns.mapping" = ":key,info:userid,info:datetime,info:searchname,info:retorder,info:cliorder,info:cliurl"
)
TBLPROPERTIES (
    "hbase.table.name" = "sogoulogs"
);

# command
select searchname,count(*) as rank from sogoulogs group by searchname order by rank desc limit 10;

select substr(id,0,5),count(substr(id,0,5)) as counter from sogoulogs group by substr(id,0,5) order by counter desc limit 10;