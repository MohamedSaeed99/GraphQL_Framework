import org.scalatest._
import com.cs474.{ClientBuilder}
import org.apache.http.impl.client.HttpClientBuilder

class ClientTests extends FlatSpec with Matchers{

  "A Client" should "require a connection URL" in {
    val connectionURL = "https://api.github.com/graphql"
    val githubClient = new ClientBuilder[ClientBuilder.ConnectionParameters.Empty]()
      .setConnectionURL("https://api.github.com/graphql")
      .build

    // Verify client inputs
    githubClient.connectionURL shouldBe connectionURL
    val client = HttpClientBuilder.create.build
  }


  "A Client" should "connect to a valid url" in {
    val connectionURL = "https://api.github.com/graphql"
    val githubClient = new ClientBuilder[ClientBuilder.ConnectionParameters.Empty]()
      .setConnectionURL("https://api.github.com/graphql")
      .setHeader("Accept", "application/json")
      .setAuthorization("Bearer","f6f8623d4f23b15c9b60b328e9f77d49f28274a7")
      .build

    val client = HttpClientBuilder.create.build
    val connection = githubClient.connect
    val response = client.execute(connection)

    val resultOK = response.toString.startsWith("HttpResponseProxy{HTTP/1.1 200 OK")
    assert(resultOK === true)
  }
}