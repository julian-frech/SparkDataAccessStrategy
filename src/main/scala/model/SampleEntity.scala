package org.julian.frech
package model


case class SampleEntity(identifier: Int, message: Option[String]) extends Record

object SampleEntity{
  val name = this.getClass.getSimpleName.stripSuffix("$")
  val avroSchema = "{\"type\":\"record\",\"name\":\"SampleEntity\",\"namespace\":\"org.julian.frech.model\",\"fields\":[{\"name\":\"identifier\",\"type\":\"int\"},{\"name\":\"message\",\"type\":[\"string\",\"null\"]}]}"
}


case class SampleKey(identifier: Int) extends Record
object SampleKey{
  val avroSchema = "{\"type\":\"record\",\"name\":\"SampleKey\",\"namespace\":\"org.julian.frech.model\",\"fields\":[{\"name\":\"identifier\",\"type\":\"int\"}]}"
}





