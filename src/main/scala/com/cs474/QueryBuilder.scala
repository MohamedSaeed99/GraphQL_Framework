package com.cs474

case class Query (val query:String) {
  def display: Unit ={
    println("Current query: " + query)
  }
  def queryString: String = {
    query
  }
}

//Builder to build the github object
case class QueryCommand[QueryParameters <: QueryBuilder.QueryParameters] (
                                                                           query:String = "",
                                                                           queryStringBuilder: List[(String, String)]=List()
                                                                         )
{
  import QueryBuilder.QueryParameters._

  // if a user is specified it will create a key value pair of users and the login name
  // other wise it doesn't specify a specific user

//  def setRepo(name: String): QueryCommand[QueryParameters with Empty] ={
//    var newVariables = queryStringBuilder
//    if(name != "AllRepos"){
//      newVariables = newVariables :+ ("user:", name)
//    }
//    this.copy(queryStringBuilder=newVariables)
//  }

  def setRepo(name:String=""): RepoQuery={
    val repoQuery:RepoQuery = new RepoQuery(name)
    repoQuery
  }

  def setUser(name: String): UserQuery={
    val userQuery:UserQuery = new UserQuery(name)
    userQuery
  }

  def setIssue(): IssueQuery={
    val issueQuery:IssueQuery = new IssueQuery()
    issueQuery
  }

//  // Stores the language variables in query builder creates a new copy of this object with the modified query builder
//  def setLanguage(languages: List[String]): QueryCommand[QueryParameters with Empty] = {
//    var newVariables = queryStringBuilder
//    for (lang <- languages) {
//      newVariables = newVariables :+ ("language:", lang)
//    }
//    this.copy(queryStringBuilder=newVariables)
//  }

  // builds a github object
//  def build(implicit ev: QueryParameters =:= RequiredQuery): Query = {
//    var specifications:String = ""
//
//    // concatenates the specifications together
//    for (q <- queryStringBuilder){
//      specifications += q._1
//      specifications += q._2
//      specifications += " "
//    }
//
//    // builds the query
//    var newQuery = query
//    newQuery = "{\"query\":\"" + "query listRepos($specifics:String!, $branch:String!){"+
//      "search(query:$specifics, type: REPOSITORY, first:20){ " +
//      "repositoryCount "+
//      "edges { "+
//      "node { "+
//      "... on Repository { "+
//      "repoName: name "+
//      "repoDesc: description "+
//      "repoURL: url "+
//      "object(expression:$branch) { ... on Commit { history { totalCommits: totalCount } } } "+
//      "primaryLanguage { name } " +
//      "languages(first:20){ totalCount nodes{ name } } " +
//      "pullRequests{ totalPulls: totalCount } " +
//      "issues{ totalIssues: totalCount } "+
//      "stargazers{ starCount: totalCount } "+
//      "owner { ownerLogin: login ... on Organization{ OrganizationName: name OrganizationDesc: description OrganizationEmail: email } } " +
//      "} } } } }\", "+
//      "\"variables\":{\"specifics\":\""+specifications+"\", \"branch\":\"master\"}, " +
//      "\"operationName\":\"listRepos\"}"
//    // returns an object with the built query command
//    Query(newQuery)
//  }
}

// sealed traits specifies which parameters are required in creating the object
object QueryBuilder {
  sealed trait QueryParameters
  object QueryParameters {
    sealed trait Empty extends QueryParameters
    sealed trait Repo extends QueryParameters
    sealed trait User extends QueryParameters
    sealed trait Issue extends QueryParameters

    type RequiredRepo = Empty with Repo
    type RequiredUser = Empty with User
    type RequiredIssue = Empty with Issue
  }
}