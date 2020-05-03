package com.cs474.Query
import org.slf4j.{Logger, LoggerFactory};

// Filter status for the ISSUE type response
case class Status(n: Node)(f: String=>Boolean){
  val Logger = LoggerFactory.getLogger( classOf[Status])
  def compare(): Boolean={
    if(n.state.isEmpty) {
      Logger.warn("States is Empty.")
      false
    }
    else f(n.state.get)
  }
}

// Contains built query string
case class IssueQuery(query:String, queryBuilder: IssueQueryBuilder) extends Query(query){
  override def queryString: String = query
  override def builder: IssueQueryBuilder = queryBuilder
}

// IssueQuery builder that would build a type ISSUE search query
case class IssueQueryBuilder(searchWord:String,cursor:String=null, query:String="") extends QueryBuilder{

  def setCursor(c: String): IssueQueryBuilder = {
    this.copy(cursor= "\"" + c + "\"")
  }

  // builds a github object
  def build: IssueQuery = {
    // builds the query
    var newQuery = query
    newQuery = "{\"query\":\"" + "query listIssues($specifics:String!, $cursor:String) {"+
        "search(query:$specifics type: ISSUE first:50, after:$cursor) { " +
          "count: issueCount " +
          "edges { "+
            "node { "+
              "... on Issue { "+
                "title "+
                "url "+
                "body " +
                "locked " +
                "state " +
                "author { url login } " +
        "}" +
      "}" +
      "cursor" +
    "} } }\", "+
    "\"variables\":{\"specifics\":\""+searchWord+"\", \"cursor\":" + cursor + "}, " +
    "\"operationName\":\"listIssues\"}"

    // returns an object with the built query command
    IssueQuery(newQuery, this.copy())
  }
}
