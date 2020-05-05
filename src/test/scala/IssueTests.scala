import com.cs474.Query._
import org.scalatest._

class IssueTests extends FlatSpec with Matchers{

  "An Issue Query" should "be of the correct query type" in {
    val issueQ = (new QueryCommand()).setIssueQuery("test").build
    issueQ shouldBe a[IssueQuery]
  }

}