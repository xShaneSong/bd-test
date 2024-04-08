# receiver
bin/flume-ng agent -n agent1 -c conf -f /home/hadoop/workspace/app/bd-pf/flume/conf/avro-file-selector-logger.properties -Dflume.root.logger=INFO,console

#
bin/flume-ng agent -n agent1 -c conf -f /home/hadoop/workspace/app/bd-pf/flume/conf/taildir-file-selector-avro.properties -Dflume.root.logger=INFO,console


# test data

echo '00:00:00	2982199073774412	[AAA]	8 3	download.it.com.cn/softweb/software/firewall/antivirus/20067/17938.html' >> sogou.log

echo '00:00:00	2982199073774412	[BBB]	8 3	download.it.com.cn/softweb/software/firewall/antivirus/20067/17938.html' >> sogou.log


# start kafka consumer service
${KAFKA_HOME}/bin/kafka-topics.sh --bootstrap-server hadoop01:9092 --create --topic sogoulogs --replication-factor 3 --partitions 3
${KAFKA_HOME}/bin/kafka-console-consumer.sh --bootstrap-server hadoop01:9092 --topic sogoulogs