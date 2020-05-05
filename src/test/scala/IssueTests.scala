import com.cs474.ClientBuilder
import com.cs474.Query._
import org.scalatest._

class IssueTests extends FlatSpec with Matchers{

  "An Issue Query" should "be of the correct query type" in {
    val issueQ = (new QueryCommand()).setIssueQuery("test").build
    issueQ shouldBe a[IssueQuery]
  }

  "An Issue Query" should "be able to be filtered on its state" in {

    // Execute it
    val githubObject = new ClientBuilder[ClientBuilder.ConnectionParameters.Empty]()
      .setConnectionURL("https://api.github.com/graphql")
      .setHeader("Accept", "application/json")
      .setAuthorization("Bearer","f6f8623d4f23b15c9b60b328e9f77d49f28274a7")
      .build

    val issueQ = (new QueryCommand()).setIssueQuery("test").build
    val built : List[Node] = githubObject.executeQuery(issueQ)

    val openIssues : List[Node] = built.filter(StatusFilter(_)(_.equalsIgnoreCase("open")).compare)

    for (issue <- openIssues) {
      assert((issue.state.get.toLowerCase equals "open") === true)
    }

    val closedIssues : List[Node] = built.filter(StatusFilter(_)(_.equalsIgnoreCase("closed")).compare)
    for (issue <- closedIssues) {
      assert((issue.state.get.toLowerCase equals "closed") === true)
    }
  }
}