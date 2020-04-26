package com.cs474.Query

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
               )
case class IssueEdges(node: IssueNode)
case class IssueSearch(edges: List[IssueEdges])
case class IssueData(search: IssueSearch)
case class IssueSearchJSONFormat(data: IssueData)


case class IssueQuery(query:String) extends Query(query){
  override def queryString: String = query
}
case class IssueQueryBuilder(searchWord:String, pagination:Int=20, query:String=""){

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
