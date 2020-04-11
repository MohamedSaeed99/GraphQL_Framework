import javax.xml.crypto.Data
import org.slf4j.{Logger, LoggerFactory};
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import scala.io.Source.fromInputStream;

class Authenticate{
  val Logger = LoggerFactory.getLogger( classOf[Authenticate])
  Logger.info("Hello from the Pizza class");
}

object main extends App{
  val BASE_GHQL_URL = "https://api.github.com/graphql"
  val temp="{viewer {Email Goes Here}}"
//  implicit val formats = DefaultFormats

  val client = HttpClientBuilder.create.build
  val httpUriRequest = new HttpPost(BASE_GHQL_URL)
  httpUriRequest.addHeader("Authorization", "Bearer GITHUB Oauth Key HERE")
  httpUriRequest.addHeader("Accept", "application/json")
  val gqlReq = new StringEntity("{\"query\":\"" + temp + "\"}" )
  httpUriRequest.setEntity(gqlReq)

  val response = client.execute(httpUriRequest)
  System.out.println("Response:" + response)
  response.getEntity match {
    case null => System.out.println("Response entity is null")
    case x if x != null => {
      val respJson = fromInputStream(x.getContent).getLines.mkString
      System.out.println(respJson)
    }
  }
}
