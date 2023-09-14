package org.julian.frech
package enrichment

import common.Constants.entityConfigMap
import model.{Record, SampleEntity, SampleKey, SecondEntity}

import org.apache.spark.sql._
import org.apache.spark.sql.avro.{from_avro, to_avro}
import org.apache.spark.sql.functions.{col, struct}
import org.apache.spark.sql.types.StructType

import scala.reflect.runtime.universe._

trait InterfaceEnrichment[K <: Record, V <: Record] {
  val entityName: String

  def fromKafka(data: DataFrame)(implicit spark: SparkSession, encoderK: Encoder[K], encoderV: Encoder[V]): Dataset[(K,V)] = {
    val ds = data.select(
      from_avro(col("key"),entityConfigMap(entityName).kafka.get.keySchema).as[K],
      from_avro(col("value"),entityConfigMap(entityName).kafka.get.valueSchema).as[V]
    )
    ds
  }
  def toKafka(data: Dataset[V]): DataFrame
  def toHdfs(data: Dataset[V]): DataFrame = data.toDF()
  def getKafkaOutputMessage[T <: Product : TypeTag, K <: Product : TypeTag, V <: Product : TypeTag](dataset: Dataset[T]): DataFrame = {
    def getSchema[G <: Product : TypeTag](): StructType = {
      Encoders.product[G].schema
    }

    dataset
      .select(struct(
        getSchema[K].fields.map(f =>
          col(f.name).cast(f.dataType).as(f.name)
        ): _*).alias("key"),
        struct(getSchema[V].fields.map(f =>
          col(f.name).cast(f.dataType).as(f.name)
        ): _*).alias("value")
      )
  }

}

object ImplicitConversions {

  implicit class DataFrameOps[K<: Record ,V <: Record](data: DataFrame)(implicit enrichment: InterfaceEnrichment[K, V], spark: SparkSession, encoderK: Encoder[K], encoderV: Encoder[V]){
    def fromKafka: Dataset[(K,V)] = enrichment.fromKafka(data)
  }

  implicit class DatasetOps[_, V <: Record](data: Dataset[V])(implicit enrichment: InterfaceEnrichment[_, V]) {
    def toKafka: DataFrame = enrichment.toKafka(data)
    def toHdfs: DataFrame = enrichment.toHdfs(data)
  }

}

object SampleEntityExtensions {

  implicit val sampleEntityEnrichment: InterfaceEnrichment[SampleKey, SampleEntity] = new InterfaceEnrichment[SampleKey, SampleEntity] {
    override val entityName = SampleEntity.name

    override def fromKafka(data: DataFrame)(implicit spark: SparkSession, encoderK: Encoder[SampleKey], encoderV: Encoder[SampleEntity]): Dataset[(SampleKey, SampleEntity)] = super.fromKafka(data)
    override def toKafka(data: Dataset[SampleEntity]): DataFrame = {
      getKafkaOutputMessage[SampleEntity, SampleKey, SampleEntity](data)
        .select(to_avro(col("key")) as "key", to_avro(col("value")).as("value"))
    }
    override def toHdfs(data: Dataset[SampleEntity]): DataFrame = super.toHdfs(data)

  }
}

object SecondEntityExtensions {

  implicit val secondEntityEnrichment: InterfaceEnrichment[SampleKey, SecondEntity] = new InterfaceEnrichment[SampleKey, SecondEntity] {
    override val entityName = SecondEntity.name

    override def fromKafka(data: DataFrame)(implicit spark: SparkSession, encoderK: Encoder[SampleKey], encoderV: Encoder[SecondEntity]): Dataset[(SampleKey, SecondEntity)] = super.fromKafka(data)
    override def toKafka(data: Dataset[SecondEntity]): DataFrame = {
      getKafkaOutputMessage[SecondEntity, SampleKey, SecondEntity](data)
        .select(to_avro(col("key")) as "key", to_avro(col("value")).as("value"))
    }
    override def toHdfs(data: Dataset[SecondEntity]): DataFrame = super.toHdfs(data)
  }
}