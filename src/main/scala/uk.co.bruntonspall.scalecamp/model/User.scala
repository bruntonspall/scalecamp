package uk.co.bruntonspall.scalecamp.model

import com.googlecode.objectify.ObjectifyService
import org.scribe.model.Token
import uk.co.bruntonspall.scalecamp.utils.Annotations._
import uk.co.bruntonspall.scalecamp.utils.Ofy
import com.googlecode.objectify.annotation.Entity

@Entity
case class User(
    @Id var id: String,
    @Index var twitter_name: String,
    @Serialize var twitter_access_token: Token,
    @Serialize var user: TwitterUser) {
  private def this() { this(null, null, null, null) }
}

object User {
  ObjectifyService.register(classOf[User])

  def getByTwitterHandle(handle: String) = Option(Ofy.load.kind(classOf[User]).filter("twitter_name", handle).first.get)

  def get(id: String) = Ofy.load.kind(classOf[User]).id(id).get

  def put(user: User) = Ofy.save.entity(user)

  def getOrCreate(twitterUser: TwitterUser, token: Token) = {
    getByTwitterHandle(twitterUser.name) match {
      case Some(user) => user
      case None => {
        val user = new User(twitterUser.id_str, twitterUser.name, token, twitterUser)
        put(user)
        user
      }
    }
  }
}