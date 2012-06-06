package uk.co.bruntonspall.scalecamp.servlets

import org.scalatra.ScalatraServlet
import uk.co.bruntonspall.scalecamp.scalatra.TwirlSupport
import com.googlecode.objectify.ObjectifyService
import uk.co.bruntonspall.scalecamp.utils._
import uk.co.bruntonspall.scalecamp.model._
import com.google.appengine.api.datastore.Email

class DispatcherServlet extends ScalatraServlet with TwirlSupport {
  ObjectifyService.register(classOf[ConfigValue])
  ObjectifyService.register(classOf[AdminAccount])
  ObjectifyService.register(classOf[Registration])

  get("/") {
    if (Config.get("is.setup") != "1")
      redirect("/setup")
    html.welcome.render()
  }

  get("/setup") {
    Config.get("is.setup") match {
      case "1" => redirect("/")
      case _ => html.setup.render(Config.configs)
    }
  }

  post("/setup") {
    Config.get("is.setup") match {
      case "1" => redirect("/")
      case _ => {
        params.foreach { case (k, v) => Config.put(k, v) }
        Config.put("is.setup", "1")
        redirect("/")
      }
    }
  }

  get("/register") {
    html.register.render()
  }
  post("/register") {
    log("Got call with " + params)
    val registration = Registration.getOrCreate(
      new Email(params.getOrElse("email", "")),
      params.get("name").get,
      params.get("company"),
      params.get("twitter"),
      params.get("reason").get

    )
  }
}