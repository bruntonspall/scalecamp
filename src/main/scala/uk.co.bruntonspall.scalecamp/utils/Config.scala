package uk.co.bruntonspall.scalecamp.utils

import com.typesafe.config.ConfigFactory
import uk.co.bruntonspall.scalecamp.utils.Annotations._
import com.googlecode.objectify.ObjectifyService
import com.googlecode.objectify.annotation.Entity

@Entity
case class ConfigValue(
    @Id var id: String,
    var value: String) {
  private def this() { this(null, null) }
}

object Config {
  ObjectifyService.register(classOf[ConfigValue])

  val fallback = ConfigFactory.load()

  def get(key: String): String =
    Option(Ofy.load.kind(classOf[ConfigValue]).id(key).get()) match {
      case Some(cv) => cv.value
      case None => configs.getOrElse(key, "")
    }

  def put(key: String, value: String) = {
    val cf = ConfigValue(key, value)
    Ofy.save.entity(cf).now
  }

  val twitter_consumer_key = "twitter.consumer_key"
  val twitter_consumer_secret = "twitter.consumer_secret"
  val twitter_callback = "twitter.callback"

  val configs = Map(
    twitter_consumer_key -> fallback.getString(twitter_consumer_key),
    twitter_consumer_secret -> fallback.getString(twitter_consumer_secret),
    twitter_callback -> fallback.getString(twitter_callback)
  )
}
