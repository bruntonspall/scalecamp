package uk.co.bruntonspall.scalecamp.servlets

import uk.co.bruntonspall.scalecamp.scalatra.TwirlSupport
import org.scalatra.ScalatraServlet
import org.scribe.oauth.OAuthService
import org.scribe.builder.ServiceBuilder
import org.scribe.builder.api.TwitterApi
import uk.co.bruntonspall.scalecamp.utils.Config
import org.scribe.model._
import net.liftweb.json.Extraction
import net.liftweb.json
import uk.co.bruntonspall.scalecamp.model.{TwitterUser, User}

class TwitterOAuthServlet extends ScalatraServlet with TwirlSupport {
  implicit val formats = net.liftweb.json.DefaultFormats
  val service = new ServiceBuilder()
    .provider(classOf[TwitterApi.Authenticate])
    .apiKey(Config.twitter_consumer_key)
    .apiSecret(Config.twitter_consumer_secret)
    .callback(Config.twitter_callback)
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
            val j = json.parse(response.getBody)
            System.out.println(json.pretty(json.render(j)));
            val user = TwitterUser.extract(j)
            val userid = User.getOrCreate(user.id_str , user.screen_name, accessToken, user.name)
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
    val userid = User.getOrCreate("1", "testuser", new Token("key", "secre"), "Test User 1")
    session("current_user") = userid
  }
}
