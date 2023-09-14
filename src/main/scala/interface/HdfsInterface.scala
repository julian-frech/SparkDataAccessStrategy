package org.julian.frech
package interface

import enrichment.InterfaceEnrichment
import model.{Configuration, Record}

import org.apache.spark.sql.{Dataset, Encoder, SparkSession}

object HdfsInterface extends DataInterface {

  import enrichment.ImplicitConversions._

  override def readKeyValue[K <: Record : Encoder, V <: Record : Encoder](config: Configuration)(implicit spark: SparkSession, enrichment: InterfaceEnrichment[K, V]): Dataset[(K, V)] = {
    spark.read.option("path", config.hdfs.get.folder).load().as[(K, V)]
  }

  override def read[K <: Record : Encoder, V <: Record : Encoder](config: Configuration)(implicit spark: SparkSession, enrichment: InterfaceEnrichment[K, V]): Dataset[(K, V)] = {
    spark.read.option("path", config.hdfs.get.folder).load().as[(K, V)]
  }

  override def write[V <: Record](config: Configuration, data: Dataset[V])(implicit spark: SparkSession, enrichment: InterfaceEnrichment[_, V]): Unit = {
    data.toHdfs
      .write
      .option("path", config.hdfs.get.folder)
      .save()
  }
}
