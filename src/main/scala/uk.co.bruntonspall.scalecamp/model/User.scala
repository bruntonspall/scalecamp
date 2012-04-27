package uk.co.bruntonspall.scalecamp.model

import javax.persistence.Id
import com.googlecode.objectify.annotation.Unindexed
import com.googlecode.objectify.{Key, ObjectifyService}
import org.scribe.model.Token

case class User(
                 @Id id: Long,
                 twitter_id: String,
                 twitter_name: String,
                 @Unindexed twitter_access_token: Token,
                 @Unindexed fullName: String)

object User {
  ObjectifyService.register(classOf[User])

  def query = ObjectifyService.begin.query(classOf[User])
  def key(id: Long) = new Key(classOf[User], id)

  def getByTwitterHandle(handle: String) = Option(query.filter("twitter_name",handle).get)
	def get(id: Long):User = ObjectifyService.begin.get(key(id))
  def put(user: User):Key[User] = ObjectifyService.begin.put(user)
  def getOrCreate(id: String, handle: String, token: Token, fullname: String) = {
    getByTwitterHandle(id) match {
      case Some(user) => user
      case None => {
        val user = new User(null.asInstanceOf[Long], id, handle, token, fullname)
        val key:Key[User] = ObjectifyService.begin.put(user)
        user
      }
    }
  }
}