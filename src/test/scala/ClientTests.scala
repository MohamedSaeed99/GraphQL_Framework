import org.scalatest._
import com.cs474.{Accept, Appjson, Bearer, ClientBuilder, GetConnectionUrl, GetKey}
import org.apache.http.impl.client.HttpClientBuilder

class ClientTests extends FlatSpec with Matchers{

  "A Client" should "require a connection URL" in {
    val connectionURL = "https://api.github.com/graphql"
    val githubClient = new ClientBuilder[ClientBuilder.ConnectionParameters.Empty]()
      .setConnectionURL(GetConnectionUrl().urlString)
      .build

    // Verify client inputs
    githubClient.connectionURL shouldBe connectionURL
    val client = HttpClientBuilder.create.build
  }


  "A Client" should "connect to a valid url" in {
    val githubClient = new ClientBuilder[ClientBuilder.ConnectionParameters.Empty]()
      .setConnectionURL(GetConnectionUrl().urlString)
      .setHeader(Accept, Appjson)
      .setAuthorization(Bearer,GetKey().keyValue)
      .build

    val client = HttpClientBuilder.create.build
    val connection = githubClient.connect
    val response = client.execute(connection)

    val resultOK = response.toString.startsWith("HttpResponseProxy{HTTP/1.1 200 OK")
    assert(resultOK === true)
  }
}