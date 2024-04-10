package com.example;

import org.apache.spark.{SparkConf, SparkContext}

object MyScalaWordCount {
    def main(args: Array[String]) {
        println("Hello, world!")

        // if (args.length < 2) {
        //     System.err.println("Usage: MyWordCount <input> <output>")
        //     System.exit(1)
        // }

        val input = "/home/hadoop/workspace/app/bd-pf/data/spark/test_data/test_file"
        val output = "/home/hadoop/workspace/app/bd-pf/data/spark/test_data/spark_restult"
        val conf = new SparkConf().setAppName("myWordCount").setMaster("local") 
        val sc = new SparkContext(conf)
        val lines = sc.textFile(input)
        val resultRdd = lines.flatMap(_.split(" ")).map((_,1)).reduceByKey(_+_)
        resultRdd.saveAsTextFile(output)
        sc.stop()
    }
}