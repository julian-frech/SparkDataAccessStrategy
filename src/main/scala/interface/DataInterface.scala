package org.julian.frech
package interface

import enrichment.InterfaceEnrichment
import model._
import org.apache.spark.sql.{Dataset, Encoder, Encoders, SparkSession}

trait DataInterface {
  implicit def tuple2Encoder[A1 <: Record : Encoder, A2 <: Record : Encoder]: Encoder[(A1, A2)] =
    Encoders.tuple(implicitly[Encoder[A1]], implicitly[Encoder[A2]])

  def read[K <: Record : Encoder, V <: Record : Encoder](config: Configuration)(implicit spark: SparkSession, enrichment: InterfaceEnrichment[K, V]): Dataset[(K, V)]

  def readKeyValue[K <: Record : Encoder, V <: Record : Encoder](config: Configuration)(implicit spark: SparkSession, enrichment: InterfaceEnrichment[K, V]): Dataset[(K, V)]

  def write[V <: Record](config: Configuration, data: Dataset[V])(implicit spark: SparkSession, enrichment: InterfaceEnrichment[_, V]): Unit

}



