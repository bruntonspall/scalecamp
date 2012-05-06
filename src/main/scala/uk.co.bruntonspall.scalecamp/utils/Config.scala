package uk.co.bruntonspall.scalecamp.utils


import com.googlecode.objectify.annotation.Unindexed
import com.googlecode.objectify.{NotFoundException, Key, ObjectifyService}
import javax.persistence.Id

class ConfigValue {
  @Id var id:String = null
  @Unindexed var value:String = ""
}

object Config {
  ObjectifyService.register(classOf[ConfigValue])

  def get(key:String):String =
    Option(ObjectifyService.begin().find(classOf[ConfigValue], key)) match {
      case Some(cv) => cv.value
      case None => configs.getOrElse(key, "")
    }

  def put(key:String, value:String):Key[ConfigValue] = {
    val cf = new ConfigValue()
    cf.id = key
    cf.value = value
    ObjectifyService.begin().put(cf)
  }

  val twitter_consumer_key = "oauth.twitter.consumer_key"
  val twitter_consumer_secret = "oauth.twitter.consumer_secret"
  val twitter_callback = "oauth.twitter.callback"

  val configs = Map(
    twitter_consumer_key -> "",
    twitter_consumer_secret -> "",
    twitter_callback -> ""
    )
}
