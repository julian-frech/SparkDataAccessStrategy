package org.julian.frech
package common

import model.{Configuration, HdfsConfig, KafkaConfig, SampleEntity, SampleKey, SecondEntity}

object Constants {

  //TODO: Move into configurations and HOCON
  val entityConfigMap: Map[String, Configuration] = Map(
    SampleEntity.name -> Configuration(hdfs = Some(HdfsConfig("file://tmp/" + SampleEntity.name.toLowerCase)), kafka = Some(KafkaConfig("topic_" + SampleEntity.name.toLowerCase, SampleKey.avroSchema, SampleEntity.avroSchema))),
    SecondEntity.name -> Configuration(hdfs = Some(HdfsConfig("file://tmp/" + SecondEntity.name.toLowerCase)), kafka = Some(KafkaConfig("topic_" + SecondEntity.name.toLowerCase, SampleKey.avroSchema, SecondEntity.avroSchema)))
  )


}
