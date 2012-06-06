package uk.co.bruntonspall.scalecamp.servlets

import uk.co.bruntonspall.scalecamp.scalatra.TwirlSupport
import org.scalatra.ScalatraServlet
import org.scribe.builder.ServiceBuilder
import org.scribe.builder.api.TwitterApi
import uk.co.bruntonspall.scalecamp.utils.Config
import org.scribe.model._
import uk.co.bruntonspall.scalecamp.model.{ TwitterUser, AdminAccount }
import io.Source

class TwitterOAuthServlet extends ScalatraServlet with TwirlSupport {
  lazy val service = new ServiceBuilder()
    .provider(classOf[TwitterApi.Authenticate])
    .apiKey(Config.get(Config.twitter_consumer_key))
    .apiSecret(Config.get(Config.twitter_consumer_secret))
    .callback(Config.get(Config.twitter_callback))
    .build

  get("/login") {
    session.get("current_user") match {
      case Some(user) => redirect("/")
      case None => {
        val requestToken = service.getRequestToken
        session("requestToken") = requestToken
        redirect(service.getAuthorizationUrl(requestToken))
      }
    }
  }

  get("/callback") {
    val accessTokenOption =
      for {
        requestToken <- session.get("requestToken")
        verifier <- params.get("oauth_verifier")
      } yield service.getAccessToken(requestToken.asInstanceOf[Token], new Verifier(verifier))

    accessTokenOption match {
      case Some(accessToken) => {
        // Store the access token for re-use
        session("accessToken") = accessToken
        // Now create or fetch the user object based on twitter login name
        val request = new OAuthRequest(Verb.GET, "http://api.twitter.com/1/account/verify_credentials.json");
        service.signRequest(accessToken, request); // the access token from step 4
        val response = request.send();
        response.isSuccessful match {
          case true => {
            val user = TwitterUser.parse(response.getBody)
            val userid = AdminAccount.getOrCreate(user, accessToken)
            session("current_user") = userid
          }
          case _ => halt(500, "Failed to get a valid response from twitter")
        }
      }
      case _ => System.out.println("Didn't find a token")
    }

    redirect("/")
  }

  get("/fake") {
    val cl = Thread.currentThread().getContextClassLoader

    val rawJson = Source.fromInputStream(cl.getResourceAsStream("verify_credentials.json")).mkString
    val accessToken = new Token("key", "secret")
    val user = TwitterUser.parse(rawJson)
    val userid = AdminAccount.getOrCreate(user, accessToken)
    session("current_user") = userid
    redirect("/")
  }
}
