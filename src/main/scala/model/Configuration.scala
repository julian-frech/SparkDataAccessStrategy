package org.julian.frech
package model

case class Configuration(kafka: Option[KafkaConfig],
                         hdfs: Option[HdfsConfig])

case class KafkaConfig(topic: String,
                       keySchema: String,
                       valueSchema: String)

case class HdfsConfig(folder: String)

object Configuration {
  def getKafkaConfig(kafkaConfig: KafkaConfig) = Configuration(Some(kafkaConfig), None)
  def getHdfsConfig(hdfsConfig: HdfsConfig) = Configuration(None, Some(hdfsConfig))
}
