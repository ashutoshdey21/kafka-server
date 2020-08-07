# Adhoc Spark Structured Streaming Example


## Start Kafka

1. Download Kafka 2.4.0 for Scala 2.11: https://kafka.apache.org/downloads
2. Unzip the zip file.
3. Jump to the folder
4. Open a terminal (keep it open) and start Zookeeper: `./bin/zookeeper-server-start.sh ./config/zookeeper.properties`
5. Open another terminal (keep it open) and start Kafka: `./bin/kafka-server-start.sh ./config/server.properties`

Now a Kafka with a single broker is running on the laptop. This tutorial is from Kafka Quickstart: https://kafka.apache.org/quickstart

## Start Producer and Consumer

1. Clone this repo and jump to this folder
2. Compile the code `mvn clean compile assembly:single`
3. Run the code `java -jar target/kafka-1.0-SNAPSHOT-jar-with-dependencies.jar`

This will start a Java application that pushes messages to Kafka in two Thread and read messages in the main Thread. The output you will see in the terminal is the messages received in the consumer.

## Start a Spark cluster
1. Download Spark 2.3.0 for Scala 2.11
2. In your spark folder, run `./sbin/start-all.sh`. This will start a Spark cluster. If you didn't change the 'worker' file before, this cluster will use localhost as the single worker.
3. Open `localhost:8080` in your browser
4. Copy the master info on the web page. On my laptop, it shows: `spark://Jias-MacBook-Pro.local:7077`

## Start a Spark shell

In your spark folder, run `./bin/spark-shell --packages org.apache.spark:spark-sql-kafka-0-10_2.11:2.3.0 -- master spark://Jias-MacBook-Pro.local:7077`. Note that `--master` should be the master info you just copied in the last step.

## Try Spark Streaming on two Kafka topics

The following content is from Spark Structured Streaming + Kafka tutorial: https://spark.apache.org/docs/2.3.0/structured-streaming-kafka-integration.html

Note that, in this example the bootstrap servers are "localhost:9092". Because we just have one broker in the Kafka cluster.

Use `:paste` to open the paste mode in the Spark shell. Then paste the following content. Use CTRL + D to finish.

Print the table. No aggregation

```
// Subscribe to a pattern
val df = spark
  .readStream
  .format("kafka")
  .option("kafka.bootstrap.servers", "localhost:9092")
  .option("subscribePattern", "sensor.*")
  .load()
val castDf = df.selectExpr("CAST(key AS STRING)", "CAST(value AS STRING)").as[(String, String)]

val query = castDf.writeStream.format("console").option("truncate", "false").outputMode("append")

query.start().awaitTermination()

```


Aggregate in the complete mode

```
// Subscribe to a pattern
val df = spark
  .readStream
  .format("kafka")
  .option("kafka.bootstrap.servers", "localhost:9092")
  .option("subscribePattern", "sensor.*")
  .load()
val castDf = df.selectExpr("CAST(key AS STRING)", "CAST(value AS STRING)").as[(String, String)]

val myDf = castDf.groupBy("key").count()

val query = myDf.writeStream.format("console").option("truncate", "false").outputMode("complete")

query.start().awaitTermination()
```

