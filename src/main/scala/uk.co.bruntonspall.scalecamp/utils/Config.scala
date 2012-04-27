package uk.co.bruntonspall.scalecamp.utils

import com.typesafe.config.ConfigFactory

object Config {
  lazy val conf = ConfigFactory.load()
  lazy val twitter_consumer_key = conf.getString("oauth.twitter.consumer_key")
  lazy val twitter_consumer_secret = conf.getString("oauth.twitter.consumer_secret")
  lazy val twitter_callback = conf.getString("oauth.twitter.callback")
}
