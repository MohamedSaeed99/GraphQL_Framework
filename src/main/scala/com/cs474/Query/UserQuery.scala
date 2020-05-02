package com.cs474.Query

//parsing of the USER type response
//case class Followers(followers: Double)
//case class Following(following: Double)
//case class NameURLNode(name: String)
//case class GitDataEdges(node: NameURLNode)
//case class UserGitData(edges: List[GitDataEdges])
//case class UserNode(
//                 username: Option[String],
//                 email: Option[String],
//                 url: Option[String],
//                 followers: Option[Followers],
//                 following: Option[Following],
//                 organizations: Option[UserGitData],
//                 watching: Option[UserGitData],
//                 repositories: Option[UserGitData],
//                 repositoriesContributedTo: Option[UserGitData]
//               )
//case class UserSearchEdges(node: UserNode, cursor: String)
//case class UserSearchJSON(
//                           userCount: Double,
//                           edges: List[UserSearchEdges]
//                 )
//case class UserDataJSON(search: UserSearchJSON)
//case class UserSearchJSONFormat(data: UserDataJSON)


// Filter specific for the REPOSITORY type response
case class FollowingFilter(n: Node)(f: Double => Boolean) {
  def compare(): Boolean={
    if(n.following.isEmpty) false
    else f(n.following.get.following)
  }
}


// Contains built query string
case class UserQuery(query:String, queryBuilder: UserQueryBuilder) extends Query(query){
  override def queryString: String = query
  override def builder: UserQueryBuilder = queryBuilder
}

//UserQuery builder that would build a type USER search query
case class UserQueryBuilder(specs: List[(String, String)] = List(),
                            cursor:String=null, query:String="") extends QueryBuilder{

  def setCursor(c: String): UserQueryBuilder = {
    this.copy(cursor= "\"" + c + "\"")
  }

  // sets the specifications for Followers
  def setFollowers(follow:String): UserQueryBuilder = {
    var newSpec = specs
    newSpec = newSpec:+("followers:", follow)
    this.copy(specs=newSpec)
  }

  // sets specifications for a specific user
  def setUser(username:String): UserQueryBuilder = {
    var newSpec = specs
    newSpec = newSpec:+("user:", username)
    this.copy(specs = newSpec)
  }

  // sets specifications for a location
  def setLocation(loc: String): UserQueryBuilder = {
    var newSpec = specs
    newSpec = newSpec:+("location:", loc)
    this.copy(specs = newSpec)
  }

  // sets specifications for number of repos
  def setNumRepos(total: String): UserQueryBuilder = {
    var newSpec = specs
    newSpec = newSpec:+("repos:", total)
    this.copy(specs = newSpec)
  }

 // sets specifications for a language
  def setLanguage(lang: String): UserQueryBuilder = {
    var newSpec = specs
    newSpec = newSpec:+("language:", lang)
    this.copy(specs = newSpec)
  }

  // builds a github object
  def build: UserQuery = {

    var specifications = ""
    for (spec <- specs){
      specifications += spec._1 + spec._2 + " "
    }

    if(specifications.isEmpty){
      specifications = "repos:>1 "
    }

    println(specifications)

    // builds the query
    var newQuery = query
    newQuery = "{\"query\":\"" + "query listUser($specifics:String!, $pagination:Int!, $cursor:String) {"+
      "search(query:$specifics, type: USER, first:50, after:$cursor) { " +
      "count: userCount "+
      "edges { "+
        "node { "+
          "... on User { "+
            "username: login "+
            "email "+
            "url "+
            "followers { followers: totalCount } "+
            "following { following: totalCount } "+
            "repositories(first:5) { edges { node { name url } } } "+
          "}" +
        "}" +
        "cursor" +
    "} } }\", "+
    "\"variables\":{\"specifics\":\"" + specifications + "\", \"cursor\":" + cursor + "}, " +
    "\"operationName\":\"listUser\"}"
    // returns an object with the built query command
    UserQuery(newQuery, this.copy())
  }
}
