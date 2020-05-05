import org.scalatest._
import com.cs474.ClientBuilder
import com.cs474.Query._
import org.apache.http.impl.client.HttpClientBuilder

class RepoTests extends FlatSpec with Matchers{

  "A Repo Query" should "be of the correct query type" in {
    val repoQ = (new QueryCommand()).setRepoQuery().build
    repoQ shouldBe a[RepoQuery]
  }

  "A Repo Query" should "be able to filter on stars before being executed" in {

    // Execute it
    val githubObject = new ClientBuilder[ClientBuilder.ConnectionParameters.Empty]()
      .setConnectionURL("https://api.github.com/graphql")
      .setHeader("Accept", "application/json")
      .setAuthorization("Bearer","f6f8623d4f23b15c9b60b328e9f77d49f28274a7")
      .build

    // Build the query
    val repoQ = (new QueryCommand()).setRepoQuery().setStars(">100")
    assert(repoQ.stars == ">100")
    val built1Response : List[Node] = (githubObject.executeQuery(repoQ.build))

    // Verify result
    for (node <- built1Response){
      // No stars greater less than 100
      assert(node.stargazers.get.starCount > 100)
    }

    // Build another query
    val repoQ2 = (new QueryCommand()).setRepoQuery().setStars("<100")

    // Execute it
    val built2Response : List[Node] = (githubObject.executeQuery(repoQ2.build))

    // Verify result
    for (node <- built2Response){
      // No stars greater than or equal to 100
      assert(node.stargazers.get.starCount <= 101)
    }

    // Build another query
    val repoQ3 = (new QueryCommand()).setRepoQuery().setStars("100..200")
    repoQ3.stars shouldEqual("100..200")

    // Execute it
    val built3Response : List[Node] = (githubObject.executeQuery(repoQ3.build))

    // Verify result
    for (node <- built3Response){
      val d = node.stargazers.get.starCount
      // No stars greater than or equal to 100
      assert(d >= 99 && d <=201)
    }
  }

  "A Repo Query" should "be able to filter on username before being executed" in {

    // Execute it
    val githubObject = new ClientBuilder[ClientBuilder.ConnectionParameters.Empty]()
      .setConnectionURL("https://api.github.com/graphql")
      .setHeader("Accept", "application/json")
      .setAuthorization("Bearer","f6f8623d4f23b15c9b60b328e9f77d49f28274a7")
      .build

    // Build the query
    val repoQ = (new QueryCommand()).setRepoQuery().setUser("MDausch")
    repoQ.user shouldEqual("MDausch")

    val built1 = repoQ.build
    built1.queryString should include ("MDausch")

    val built1Response : List[Node] = (githubObject.executeQuery(built1))

    // Verify result
    for (node <- built1Response){
//      println(node)
      assert(node.owner.get.ownerLogin.get === "MDausch")
    }
  }

  "A Repo Query" should "be able to filter on languages before being executed" in {

    // Execute it
    val githubObject = new ClientBuilder[ClientBuilder.ConnectionParameters.Empty]()
      .setConnectionURL("https://api.github.com/graphql")
      .setHeader("Accept", "application/json")
      .setAuthorization("Bearer","f6f8623d4f23b15c9b60b328e9f77d49f28274a7")
      .build

    // Build the query
    val repoQ = (new QueryCommand()).setRepoQuery().setLanguage(List("java", "scala"))

    val built1 = repoQ.build
    val built1Response : List[Node] = (githubObject.executeQuery(built1))

    // Verify result
    for (node <- built1Response){
//      println(node)
      node.languages.get.nodes should contain atLeastOneOf (PrimaryLanguage("Java"), PrimaryLanguage("Scala"))
    }
  }

  "A Repo Query" should "be able to filtered on the number of commits AFTER being executed" in {

    // Execute it
    val githubObject = new ClientBuilder[ClientBuilder.ConnectionParameters.Empty]()
      .setConnectionURL("https://api.github.com/graphql")
      .setHeader("Accept", "application/json")
      .setAuthorization("Bearer","f6f8623d4f23b15c9b60b328e9f77d49f28274a7")
      .build

    // Build the query
    val repoQ = (new QueryCommand()).setRepoQuery().setLanguage(List("java", "scala"))

    val built1 = repoQ.build
    val built1Response : List[Node] = (githubObject.executeQuery(built1))

    val commitFilteredResults: List[Node] = built1Response.filter(CommitsFilter(_)(_>200).compare())

    // Verify result
    for (node <- commitFilteredResults){
//      println(node)
      assert(node.`object`.get.history.get.totalCommits.get > 200)
    }
  }

  "A Repo Query" should "be able to filtered on the number of Issue AFTER being executed" in {

    // Execute it
    val githubObject = new ClientBuilder[ClientBuilder.ConnectionParameters.Empty]()
      .setConnectionURL("https://api.github.com/graphql")
      .setHeader("Accept", "application/json")
      .setAuthorization("Bearer","f6f8623d4f23b15c9b60b328e9f77d49f28274a7")
      .build

    // Build the query
    val repoQ = (new QueryCommand()).setRepoQuery().setLanguage(List("java", "scala"))

    val built1 = repoQ.build
    val built1Response : List[Node] = (githubObject.executeQuery(built1))

    val commitFilteredResults: List[Node] = built1Response.filter(IssueFilter(_)(_>200).compare())

    // Verify result
    for (node <- commitFilteredResults){
      assert(node.issues.get.totalIssues.get > 200)
    }
  }

}