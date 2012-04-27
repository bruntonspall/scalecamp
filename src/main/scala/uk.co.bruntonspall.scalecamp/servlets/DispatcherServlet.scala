package uk.co.bruntonspall.scalecamp.servlets

import org.scalatra.ScalatraServlet
import uk.co.bruntonspall.scalecamp.scalatra.TwirlSupport

class DispatcherServlet extends ScalatraServlet with TwirlSupport {

  get("/") {
    html.welcome.render("First paragraph" :: "Second Paragraph" :: Nil)
  }

}