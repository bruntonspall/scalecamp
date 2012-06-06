package uk.co.bruntonspall.scalecamp.model

import com.googlecode.objectify.ObjectifyService
import org.scribe.model.Token
import uk.co.bruntonspall.scalecamp.utils.Annotations._
import uk.co.bruntonspall.scalecamp.utils.Ofy
import com.googlecode.objectify.annotation.Entity
import com.google.appengine.api.datastore.Email
import scala.util.control.Exception._
import com.weiglewilczek.slf4s.Logger

@Entity
case class Registration(
    @Id var id: java.lang.Long,
    @Index var email: Email,
    @Index var name: String,
    var company: String,
    @Index var twitter: String,
    var reason: String) {
  private def this() { this(null, null, null, null, null, null) }
}

object Registration {

  def getByEmail(email: Email) = Option(Ofy.load.kind(classOf[Registration]).filter("email", email.getEmail).first().get())

  def put(user: Registration) = Ofy.save.entity(user)

  def getOrCreate(email: Email, name: String, company: Option[String], twitter: Option[String], reason: String) = {
    getByEmail(email) match {
      case Some(registration) => {
        Logger("log").warn("Returning user" + registration)
        registration
      }
      case None => {
        val user = new Registration(null, email, name, company.getOrElse(""), twitter.getOrElse(""), reason)
        put(user).now
        Logger("log").warn("Saving user" + user)
        user
      }
    }
  }
}