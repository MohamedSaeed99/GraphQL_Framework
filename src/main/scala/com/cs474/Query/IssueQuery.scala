package com.cs474.Query

//parsing of the ISSUE type response
case class Author(
                   url: String,
                   login: String
                 )
case class Node(
                 name: String,
                 login: String,
                 url: String
               )
case class Edges(node: Node)
case class Assignees(edges: List[Edges])
case class IssueNode(
                 title: String,
                 url: String,
                 body: String,
                 locked: Boolean,
                 state: String,
                 author: Author,
               ){
}
case class IssueEdges(node: IssueNode)
case class IssueSearch(edges: List[IssueEdges])
case class IssueData(search: IssueSearch)
case class IssueSearchJSONFormat(data: IssueData)


// Filter status for the ISSUE type response
case class Status(n: IssueNode)(f: (String)=>Boolean){
  def compare(): Boolean={
    f(n.state)
  }
}

// Contains built query string
case class IssueQuery(query:String) extends Query(query){
  override def queryString: String = query
}

//IssueQuery builder that would build a type ISSUE search query
case class IssueQueryBuilder(searchWord:String, pagination:Int=100, query:String=""){
//  Sets the number of issues that can be visible at once
  def setPagination(value:Int): IssueQueryBuilder={
    val newPage = value
    this.copy(pagination=newPage)
  }

  // builds a github object
  def build(): IssueQuery = {
    // builds the query
    var newQuery = query
    newQuery = "{\"query\":\"" + "query listIssues($specifics:String!, $pagination:Int!) {"+
        "search(query:$specifics type: ISSUE first:$pagination) { " +
          "edges { "+
            "node { "+
              "... on Issue { "+
                "title "+
                "url "+
                "body " +
                "locked " +
                "state " +
                "author { url login } " +
      "} } } } }\", "+
      "\"variables\":{\"specifics\":\""+searchWord+"\", \"pagination\":"+pagination+"}, " +
      "\"operationName\":\"listIssues\"}"

    // returns an object with the built query command
    IssueQuery(newQuery)
  }
}
