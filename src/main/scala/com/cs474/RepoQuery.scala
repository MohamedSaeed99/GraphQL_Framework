package com.cs474

case class RepoQuery( query: String) extends Query(query){
  override def queryString: String = query
}

case class RepoQueryBuilder( user:String, language:List[String]=List(), query:String=""){
  // Stores the language variables in query builder creates a new copy of this object with the modified query builder
  def setLanguage(languages: List[String]): RepoQueryBuilder = {
    val newVariables = languages
    this.copy(language=newVariables)
  }

  def build(): RepoQuery = {
    var specifications: String = ""
    if(user != null) {
      specifications= "user:" + user + " "
    }

    // concatenates the specifications together
    for (q <- language){
      specifications += "language:"
      specifications += q
      specifications += " "
    }

    // builds the query
    var newQuery = query
    newQuery = "{\"query\":\"" + "query listRepos($specifics:String!, $branch:String!){"+
      "search(query:$specifics, type: REPOSITORY, first:20){ " +
      "repositoryCount "+
      "edges { "+
      "node { "+
      "... on Repository { "+
      "repoName: name "+
      "repoDesc: description "+
      "repoURL: url "+
      "object(expression:$branch) { ... on Commit { history { totalCommits: totalCount } } } "+
      "primaryLanguage { name } " +
      "languages(first:20){ totalCount nodes{ name } } " +
      "pullRequests{ totalPulls: totalCount } " +
      "issues{ totalIssues: totalCount } "+
      "stargazers{ starCount: totalCount } "+
      "owner { ownerLogin: login ... on Organization{ OrganizationName: name OrganizationDesc: description OrganizationEmail: email } } " +
      "} } } } }\", "+
      "\"variables\":{\"specifics\":\""+specifications+"\", \"branch\":\"master\"}, " +
      "\"operationName\":\"listRepos\"}"
    // returns an object with the built query command
    RepoQuery(newQuery)
  }
}
