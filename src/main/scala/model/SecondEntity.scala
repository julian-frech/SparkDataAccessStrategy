package org.julian.frech
package model

case class SecondEntity(identifier: Int, message: Int, name: Option[String]) extends Record

object SecondEntity{
  val name = this.getClass.getSimpleName.stripSuffix("$")
  val avroSchema = "{\"type\":\"record\",\"name\":\"SecondEntity\",\"namespace\":\"org.julian.frech.model\",\"fields\":[{\"name\":\"identifier\",\"type\":\"int\"},{\"name\":\"message\",\"type\":\"int\"},{\"name\":\"name\",\"type\":[\"string\",\"null\"]}]}"
}






