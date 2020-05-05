import org.scalatest._
import com.cs474.ClientBuilder
import com.cs474.Query._

class UserTests extends FlatSpec with Matchers{

  "A User Query" should "be of the correct query type" in {
    val userQ = (new QueryCommand()).setUserQuery().build
    userQ shouldBe a[UserQuery]
  }

  "A User Query" should "be able to filter on username before being executed" in {

    val githubObject = new ClientBuilder[ClientBuilder.ConnectionParameters.Empty]()
      .setConnectionURL("https://api.github.com/graphql")
      .setHeader("Accept", "application/json")
      .setAuthorization("Bearer","f6f8623d4f23b15c9b60b328e9f77d49f28274a7")
      .build

    // Build the query
    val userQ = (new QueryCommand()).setUserQuery().setUser("MDausch")

    val response : List[Node] = (githubObject.executeQuery(userQ.build))

    // Verify result
    assert(response.length === 1)
    for (node <- response){
      println(node)
      node.username.get shouldEqual("MDausch")
    }
  }
}