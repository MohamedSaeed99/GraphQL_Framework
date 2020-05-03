package com.cs474.Query

import org.slf4j.LoggerFactory

// Filter specific for the REPOSITORY type response
case class Commits(n:Node)(f: Double => Boolean) {
  def compare(): Boolean={
    val Logger = LoggerFactory.getLogger( classOf[Commits])

    if(n.`object`.isEmpty) {
      Logger.info("Commit attribute is empty")
      false
    }
    else f(n.`object`.get.history.get.totalCommits.get)
  }
}

case class Issue(n:Node)(f: Double => Boolean) {
  val Logger = LoggerFactory.getLogger( classOf[Issue])
  def compare(): Boolean={
    if(n.issues.isEmpty) {
      Logger.info("Issues attribute is empty")
      false
    }
    else f(n.issues.get.totalIssues.get)
  }
}

case class PullRequest(n:Node)(f: Double => Boolean) {
  val Logger = LoggerFactory.getLogger( classOf[PullRequest])

  def compare(): Boolean={
    if(n.pullRequests.isEmpty) {
      Logger.info("Pullrequest attribute is empty")
      false
    }
    else f(n.pullRequests.get.totalPulls.get)
  }
}
case class LanguageFilter(n:Node)(f: Double => Boolean) {
  val Logger = LoggerFactory.getLogger( classOf[LanguageFilter])
  def compare(): Boolean={
    if(n.languages.isEmpty) {
      Logger.info("Language attribute is empty")
      false
    }
    else f(n.languages.get.totalCount)
  }
}


// Contains built query string
case class RepoQuery(query: String, queryBuilder: RepoQueryBuilder) extends Query(query){
  override def queryString: String = query
  override def builder: RepoQueryBuilder = queryBuilder
}

//RepoQuery builder that would build a type REPOSITORY search query
case class RepoQueryBuilder( user:String=null, stars:String=null,
                             language:List[String]=List(),cursor:String=null, query:String="") extends QueryBuilder{

  val Logger = LoggerFactory.getLogger( classOf[RepoQueryBuilder])

  // Stores the language variables in query builder creates a new copy of this object with the modified query builder
  def setLanguage(languages: List[String]): RepoQueryBuilder = {
    Logger.info("Setting up specifications for languages: {}", languages)
    val newVariables = languages
    this.copy(language=newVariables)
  }

  //  sets the condition on the number of stars
  //  ">n" : returns repos with more than n stars
  //  ">=n" : returns repos with more than or equal to n stars
  //  "<n" : returns repos with less than n stars
  //  "<=n" : returns repose with less than or equal to n stars
  //  "a..n" : returns repos with stars in between a and n inclusive
  def setStars(starCount: String): RepoQueryBuilder ={
    Logger.info("Setting up specifications for number of stars: {}", starCount)
    val newStarVar = starCount
    this.copy(stars=newStarVar)
  }

  // sets condition to retrieve a specific username
  def setUser(username: String): RepoQueryBuilder ={
    Logger.info("Getting a specific user: {}", username)
    val newUsername = username
    this.copy(user=newUsername)
  }

  // builds query based on the specifications
  def setCursor(c: String): RepoQueryBuilder = {
    this.copy(cursor= "\"" + c + "\"")
  }

  def build: RepoQuery = {
    var specifications: String = "is:public "
    if(user != null) {
      specifications += "user:"+user+" "
    }

    if(stars != null){
      specifications += "stars:"+stars+" "
    }

    // concatenates the specifications together
    for (q <- language){
      specifications += "language:"
      specifications += q
      specifications += " "
    }

    Logger.info("Building a Repo query with these specifications => {}", specifications)

    // builds the query
    var newQuery = "{\"query\":\"" + "query listRepos($specifics:String!, $branch:String!, $cursor:String){"+
      "search(query:$specifics, type: REPOSITORY, first:50, after:$cursor){ " +
      "count: repositoryCount "+
      "edges { "+
      "node { "+
      "... on Repository { "+
      "repoName: name "+
      "repoDesc: description "+
      "url "+
      "object(expression:$branch) { ... on Commit { history { totalCommits: totalCount } } } "+
      "primaryLanguage { name } " +
      "languages(first:5){ totalCount nodes{ name } } " +
      "pullRequests{ totalPulls: totalCount } " +
      "issues{ totalIssues: totalCount } "+
      "stargazers{ starCount: totalCount } "+
      "owner { ownerLogin: login ... on Organization{ OrganizationName: name OrganizationDesc: description OrganizationEmail: email } } " +
      "}" +
    "}" +
    "cursor" +
    "} } }\", "+
    "\"variables\":{\"specifics\":\""+specifications+"\", \"branch\":\"master\", \"cursor\":" + cursor + "}, " +
    "\"operationName\":\"listRepos\"}"

    // returns an object with the built query command
    RepoQuery(newQuery, this.copy())
  }
}
