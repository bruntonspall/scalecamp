package uk.co.bruntonspall.scalecamp.model

import cc.spray.json._

case class TwitterUser(id_str: String, name: String, profile_image_url: String, screen_name: String)

object TwitterJsonProtocols extends DefaultJsonProtocol {
  implicit val twitterUserFormat = jsonFormat4(TwitterUser.apply)
}

import TwitterJsonProtocols._
object TwitterUser {
  def parse(j: String): TwitterUser = JsonParser(j).convertTo
}