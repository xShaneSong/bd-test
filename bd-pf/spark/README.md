# standalone mode


# standalone cluster
 bin/spark-submit --master spark://hadoop01:7077,hadoop02:7077 --class com.example.MyScalaWordCount $HADOOP_HOME/shell/lib/spark-sample-1.0.jar /test11/test_file /test11/output

# spark on Yarn
// stop all spark process
sbin/stop-all.sh

bin/spark-submit --master yarn --class com.example.MyScalaWordCount $HADOOP_HOME/shell/lib/spark-sample-1.0.jar /test11/test_file /test11/output