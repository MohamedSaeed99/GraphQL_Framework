package com.cs474.Query

//parsing of the REPOSITORY type response
case class History( totalCommits: Option[Int] )
case class ObjectBis( history: Option[History] )
case class PrimaryLanguage( name: Option[String] )
case class Languages( totalCount: Double,  nodes: List[PrimaryLanguage] )
case class PullRequests( totalPulls: Option[Double] )
case class Issues( totalIssues: Option[Double] )
case class Stargazers( starCount: Double )
case class Owner( ownerLogin: Option[String] )
case class RepoNode(
                 repoName: Option[String],
                 repoDesc: Option[String],
                 repoURL: Option[String],
                 `object`: Option[ObjectBis],
                 primaryLanguage: Option[PrimaryLanguage],
                 languages: Option[Languages],
                 pullRequests: Option[PullRequests],
                 issues: Option[Issues],
                 stargazers: Option[Stargazers],
                 owner: Option[Owner]
               )
case class RepoEdges( node: RepoNode, cursor: String )
case class RepoSearch( repositoryCount: Int,  edges: List[RepoEdges] )
case class RepoData( search: RepoSearch )
case class RepoSearchJsonFormat( data: RepoData )

// Filter specific for the REPOSITORY type response
case class Stars(n:RepoNode)(f: (Double) => Boolean) {
  def compare(): Boolean={
    f(n.stargazers.get.starCount)
  }
}

// Contains built query string
case class RepoQuery( query: String, builder: RepoQueryBuilder) extends Query(query){
  override def queryString: String = query
}

//RepoQuery builder that would build a type REPOSITORY search query
case class RepoQueryBuilder( user:String=null, stars:String=null, language:List[String]=List(),cursor:String=null, query:String=""){
  // Stores the language variables in query builder creates a new copy of this object with the modified query builder
  def setLanguage(languages: List[String]): RepoQueryBuilder = {
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
    val newStarVar = starCount
    this.copy(stars=newStarVar)
  }

  // sets condition to retrieve a specific username
  def setUser(username: String): RepoQueryBuilder ={
    val newUsername = username
    this.copy(user=newUsername)
  }

  // builds query based on the specifications
  def setCursor(c: String): RepoQueryBuilder = {
    this.copy(cursor= "\"" + c + "\"")
  }

  def build(): RepoQuery = {
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

    // builds the query
    var newQuery = "{\"query\":\"" + "query listRepos($specifics:String!, $branch:String!, $cursor:String){"+
      "search(query:$specifics, type: REPOSITORY, first:100, after:$cursor){ " +
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
