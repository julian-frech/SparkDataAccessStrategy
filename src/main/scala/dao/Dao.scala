package org.julian.frech
package dao

import enrichment.InterfaceEnrichment
import enrichment.SampleEntityExtensions.sampleEntityEnrichment
import enrichment.SecondEntityExtensions.secondEntityEnrichment
import interface.DataInterface
import model.{Configuration, DefaultKey, Record, SampleEntity, SampleKey, SecondEntity}

import org.apache.spark.sql.{Dataset, Encoder, SparkSession}
import org.julian.frech.common.Constants.entityConfigMap

import scala.reflect.runtime.universe._


trait Dao[K <: Record, V <: Record] {

  def DS(config: Configuration)(implicit driver: DataInterface, spark: SparkSession, encoderK: Encoder[K], tagK: TypeTag[K], encoderV: Encoder[V], tagV: TypeTag[V], enrichment: InterfaceEnrichment[K,V]): Dataset[(K,V)] = {
    driver.read[K,V](config)
  }

  def KeyValueDS(config: Configuration)(implicit driver: DataInterface, spark: SparkSession, encoderK: Encoder[K], tagK: TypeTag[K], encoderV: Encoder[V], tagV: TypeTag[V], enrichment: InterfaceEnrichment[K,V]): Dataset[(K, V)] = {
    driver.readKeyValue[K, V](config)
  }

  def save(config: Configuration, dataset: Dataset[V])(implicit driver: DataInterface, spark: SparkSession, encoderK: Encoder[K], tagK: TypeTag[K], encoderV: Encoder[V], tagV: TypeTag[V], enrichment: InterfaceEnrichment[K,V]): Unit = {
    driver.write[V](config, dataset)
  }
}

object SampleDao extends Dao[SampleKey, SampleEntity] {

  def KeyValueDS()(implicit driver: DataInterface, spark: SparkSession, encoderK: Encoder[SampleKey], tagK: TypeTag[SampleKey], encoderV: Encoder[SampleEntity], tagV: TypeTag[SampleEntity]): Dataset[(SampleKey, SampleEntity)] = {
    super.KeyValueDS(entityConfigMap(SampleEntity.name))
  }

  def DS()(implicit driver: DataInterface, spark: SparkSession, encoderK: Encoder[SampleKey], tagK: TypeTag[SampleKey], encoderV: Encoder[SampleEntity], tagV: TypeTag[SampleEntity]): Dataset[(SampleKey,SampleEntity)] = {
    super.DS(entityConfigMap(SampleEntity.name))
  }

  def save(dataset: Dataset[SampleEntity])(implicit driver: DataInterface, spark: SparkSession, encoderK: Encoder[SampleKey], tagK: TypeTag[SampleKey], encoderV: Encoder[SampleEntity], tagV: TypeTag[SampleEntity]): Unit = {
    super.save(entityConfigMap(SampleEntity.name), dataset)
  }
}

object SecondEntityDao extends Dao[SampleKey, SecondEntity] {

  def DS()(implicit driver: DataInterface, spark: SparkSession, encoderK: Encoder[SampleKey], tagK: TypeTag[SampleKey], encoderV: Encoder[SecondEntity], tagV: TypeTag[SecondEntity]): Dataset[(SampleKey,SecondEntity)] = {
    super.DS(entityConfigMap(SecondEntity.name))
  }

  def save(dataset: Dataset[SecondEntity])(implicit driver: DataInterface, spark: SparkSession, encoderK: Encoder[SampleKey], tagK: TypeTag[SampleKey], encoderV: Encoder[SecondEntity], tagV: TypeTag[SecondEntity]): Unit = {
    super.save(entityConfigMap(SecondEntity.name), dataset)
  }
}