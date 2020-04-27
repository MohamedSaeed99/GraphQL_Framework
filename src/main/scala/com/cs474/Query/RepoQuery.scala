package com.cs474.Query

case class History( totalCommits: Double )
case class ObjectBis( history: History )
case class PrimaryLanguage( name: String )
case class Languages( totalCount: Double,  nodes: List[PrimaryLanguage] )
case class PullRequests( totalPulls: Double )
case class Issues( totalIssues: Double )
case class Stargazers( starCount: Double )
case class Owner( ownerLogin: String )
case class RepoNode(
                 repoName: String,
                 repoDesc: String,
                 repoURL: String,
                 `object`: ObjectBis,
                 primaryLanguage: PrimaryLanguage,
                 languages: Languages,
                 pullRequests: PullRequests,
                 issues: Issues,
                 stargazers: Stargazers,
                 owner: Owner
               )
case class RepoEdges( node: RepoNode )
case class RepoSearch( repositoryCount: Double,  edges: List[RepoEdges] )
case class RepoData( search: RepoSearch )
case class RepoSearchJsonFormat( data: RepoData )



case class RepoQuery( query: String) extends Query(query){
  override def queryString: String = query
}

case class RepoQueryBuilder( user:String=null, stars:String=null, language:List[String]=List(), query:String=""){

  // Stores the language variables in query builder creates a new copy of this object with the modified query builder
  def setLanguage(languages: List[String]): RepoQueryBuilder = {
    val newVariables = languages
    this.copy(language=newVariables)
  }

//  sets the condition on the number of stars
  def setStars(starCount: String): RepoQueryBuilder ={
    val newStarVar = starCount
    this.copy(stars=newStarVar)
  }

//  sets condition to retrieve specific username
  def setUser(username: String): RepoQueryBuilder ={
    val newUsername = username
    this.copy(user=newUsername)
  }

//  builds query on the
  def build(): RepoQuery = {
    var specifications: String = "is:public "
    if(user != null) {
      specifications= "user:"+user+" "
    }

    if(stars != null){
      specifications = "stars:"+stars+" "
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
      "search(query:$specifics, type: REPOSITORY, first:100){ " +
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
