package org.julian.frech
package interface

import enrichment.InterfaceEnrichment
import model.{Configuration, Record}

import org.apache.spark.sql.{Dataset, Encoder, SparkSession}

object KafkaInterface extends DataInterface {

  import enrichment.ImplicitConversions._

  override def readKeyValue[K <: Record : Encoder, V <: Record : Encoder](config: Configuration)(implicit spark: SparkSession, enrichment: InterfaceEnrichment[K, V]): Dataset[(K, V)] = {
    spark
      .read
      .option("subscribe", config.kafka.get.topic)
      .option("startingOffsets", "earliest")
      .format("kafka")
      .option("kafka.bootstrap.servers", "localhost:22222")
      .load()
      .fromKafka
  }

  override def read[K <: Record : Encoder, V <: Record : Encoder](config: Configuration)(implicit spark: SparkSession, enrichment: InterfaceEnrichment[K, V]): Dataset[(K, V)] = {
    spark
      .read
      .option("subscribe", config.kafka.get.topic)
      .option("startingOffsets", "earliest")
      .format("kafka")
      .option("kafka.bootstrap.servers", "localhost:22222")
      .load()
      .fromKafka
  }

  override def write[K <: Record](config: Configuration, data: Dataset[K])(implicit spark: SparkSession, enrichment: InterfaceEnrichment[_, K]): Unit = {
    data.toKafka
      .write
      .option("topic", config.kafka.get.topic)
      .option("kafka.bootstrap.servers", "localhost:22222")
      .format("kafka")
      .save()
  }
}
