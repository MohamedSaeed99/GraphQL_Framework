package com.cs474.Query

import org.slf4j.{Logger, LoggerFactory};

//filters the data based on the number of following a user has
case class FollowingFilter(n: Node)(f: Double => Boolean) {
  val Logger: Logger = LoggerFactory.getLogger( classOf[FollowingFilter])

  def compare(): Boolean={
    if(n.following.isEmpty) {
      Logger.info("Following attribute is empty")
      false
    }
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
  val Logger: Logger = LoggerFactory.getLogger( classOf[UserQueryBuilder])

  // sets the cursor to get the next set of data
  def setCursor(c: String): UserQueryBuilder = {
    this.copy(cursor= "\"" + c + "\"")
  }

  // sets the specifications for Followers
  def setFollowers(follow:String): UserQueryBuilder = {
    Logger.info("Setting up specifications for followers: {}", follow)
    var newSpec = specs
    newSpec = newSpec:+("followers:", follow)
    this.copy(specs=newSpec)
  }

  // sets specifications for a specific user
  def setUser(username:String): UserQueryBuilder = {
    Logger.info("Getting a specific user: {}", username)
    var newSpec = specs
    newSpec = newSpec:+("user:", username)
    this.copy(specs = newSpec)
  }

  // sets specifications for a location
  def setLocation(loc: String): UserQueryBuilder = {
    Logger.info("Setting up specifications for location: {}", loc)
    var newSpec = specs
    newSpec = newSpec:+("location:", loc)
    this.copy(specs = newSpec)
  }

  // sets specifications for number of repos
  def setNumRepos(total: String): UserQueryBuilder = {
    Logger.info("Setting up specifications for total repos: {}", total)
    var newSpec = specs
    newSpec = newSpec:+("repos:", total)
    this.copy(specs = newSpec)
  }

 // sets specifications for a language
  def setLanguage(lang: String): UserQueryBuilder = {
    Logger.info("Setting up specifications for language: {}", lang)
    var newSpec = specs
    newSpec = newSpec:+("language:", lang)
    this.copy(specs = newSpec)
  }

  // builds a query object
  def build: UserQuery = {

    var specifications = ""
    for (spec <- specs){
      specifications += spec._1 + spec._2 + " "
    }

    if(specifications.isEmpty){
      specifications = "repos:>1 "
    }

    Logger.info("Building a User query with these specifications => {}", specifications)

    // builds the query
    var newQuery = query
    newQuery = "{\"query\":\"" + "query listUser($specifics:String!, $cursor:String) {"+
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
            "location " +
            "repositories(first:50) { totalCount edges { node { name url } } } "+
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
