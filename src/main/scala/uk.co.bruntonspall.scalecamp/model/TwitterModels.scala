package uk.co.bruntonspall.scalecamp.model

import net.liftweb.json.JsonAST.{JString, JValue}


case class TwitterUser(id_str: String, name: String, profile_image_url: String, screen_name: String)

case class JsonParseException(json: String) extends Throwable

object TwitterUser {
  def extract(j: JValue): TwitterUser = {
    def stringFieldValue(json: JValue) = json match {
      case JString(s) => s
      case _ => throw new JsonParseException(json.toString)
    }

    TwitterUser(stringFieldValue(j \ "id_str"),
    stringFieldValue(j \ "name"),
    stringFieldValue(j \ "profile_image_url"),
    stringFieldValue(j \ "screen_name")
    )
  }
}