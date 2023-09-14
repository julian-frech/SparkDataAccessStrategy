package org.julian.frech
import enrichment.ImplicitConversions.DatasetOps
import enrichment.SampleEntityExtensions.sampleEntityEnrichment
import interface.{DataInterface, KafkaInterface}
import common.LoggerFactory
import dao.{SampleDao, SecondEntityDao}
import model.{SampleEntity, SecondEntity}

import org.apache.spark.sql.SparkSession
import org.joda.time.DateTime

object Main extends App{

  implicit var spark: SparkSession = _
  implicit var driver: DataInterface = _

  private val logger = LoggerFactory.getLogger(this.getClass)

  spark = SparkSession.builder()
    .appName("Spark Essentials Playground App")
    .config("spark.master", "local")
    .getOrCreate()

  run

  def run()(implicit spark: SparkSession): Unit = {
    driver = KafkaInterface
    import spark.implicits._

    val sampleData = Seq(SampleEntity(1,Some("Test")),SampleEntity(2,Some("Test" + DateTime.now().toString()))).toDS()
    val secondEntityData = Seq(SecondEntity(identifier = 1,message = 1234, name = Some("julian"))).toDS()


    logger.info("Saving SampleEntity to kafka.")
    SampleDao.save(sampleData)
    logger.info("Getting only values from datadriver = " + driver.getClass.getSimpleName)
    SampleDao.DS.show(false)


    logger.info("Saving SecondEntity to kafka.")
    SecondEntityDao.save(secondEntityData)
    logger.info("Getting only values from datadriver = " + driver.getClass.getSimpleName)
    val secondEntityDS = SecondEntityDao.DS



    logger.info("Getting keys and values from datadriver = " + driver.getClass.getSimpleName)
    val sampleEntityKeyValueDS = SampleDao.KeyValueDS()
    sampleEntityKeyValueDS.printSchema()

    val keys = sampleEntityKeyValueDS.map(entity => entity._1)
    keys.show(false)

    val values = sampleEntityKeyValueDS.map(entity => entity._2)
    values.show(false)

    logger.info("Using enrichment directly ")
    values.toKafka.show(false)
  }
}