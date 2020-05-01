package com.cs474.Query

//parsing of the ISSUE type response
//case class Author(
//                   url: String,
//                   login: String
//                 )
//case class Node(
//                 name: String,
//                 login: String,
//                 url: String
//               )
//case class Edges(node: Node)
//case class Assignees(edges: List[Edges])
//case class IssueNode(
//                      title: Option[String],
//                      url: Option[String],
//                      body: Option[String],
//                      locked: Option[Boolean],
//                      state: Option[String],
//                      author: Option[Author],
//                    )
//case class IssueSearchEdges(node: IssueNode, cursor: String)
//case class IssueSearch(issueCount: Int, edges: List[IssueSearchEdges])
//case class IssueData(search: IssueSearch)
//case class IssueSearchJSONFormat(data: IssueData)


// Filter status for the ISSUE type response
case class Status(n: Node)(f: (String)=>Boolean){
  def compare(): Boolean={
    f(n.state.get)
  }
}

// Contains built query string
case class IssueQuery(query:String, queryBuilder: IssueQueryBuilder) extends Query(query){
  override def queryString: String = query
  override def builder: IssueQueryBuilder = queryBuilder
}

// IssueQuery builder that would build a type ISSUE search query
case class IssueQueryBuilder(searchWord:String, pagination:Int=100,cursor:String=null, query:String="") extends QueryBuilder{

  // Sets the number of issues that can be visible at once
  def setPagination(value:Int): IssueQueryBuilder={
    val newPage = value
    this.copy(pagination=newPage)
  }


  def setCursor(c: String): IssueQueryBuilder = {
    this.copy(cursor= "\"" + c + "\"")
  }

  // builds a github object
  def build(): IssueQuery = {
    // builds the query
    var newQuery = query
    newQuery = "{\"query\":\"" + "query listIssues($specifics:String!, $pagination:Int!,$cursor:String) {"+
        "search(query:$specifics type: ISSUE first:$pagination, after:$cursor) { " +
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
    "\"variables\":{\"specifics\":\""+searchWord+"\", \"pagination\":"+pagination+", \"cursor\":" + cursor + "}, " +
    "\"operationName\":\"listIssues\"}"

    // returns an object with the built query command
    IssueQuery(newQuery, this.copy())
  }
}
