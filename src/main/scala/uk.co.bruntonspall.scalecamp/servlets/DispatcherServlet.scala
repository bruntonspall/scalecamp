package uk.co.bruntonspall.scalecamp.servlets

import org.scalatra.ScalatraServlet
import uk.co.bruntonspall.scalecamp.scalatra.TwirlSupport
import uk.co.bruntonspall.scalecamp.utils.Config

class DispatcherServlet extends ScalatraServlet with TwirlSupport {

  get("/") {
  	if (Config.get("is.setup").isEmpty)
  	  redirect("/setup")
    Config.put("key", "value")
    println(Config.get("key"))
    html.welcome.render
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
      params.foreach { case(k,v) => Config.put(k,v) }
      Config.put("is.setup", "1")
      redirect("/")
		}
	}	
}
}