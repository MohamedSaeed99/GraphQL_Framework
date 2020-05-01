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


// Contains built query string
case class UserQuery(query:String, queryBuilder: UserQueryBuilder) extends Query(query){
  override def queryString: String = query
  override def builder: UserQueryBuilder = queryBuilder
}

//UserQuery builder that would build a type USER search query
case class UserQueryBuilder(user:String, pagination:Int=100, cursor:String=null, query:String="") extends QueryBuilder{

  //  Sets the number of repos that can be visible at once
  def setPagination(value:Int): UserQueryBuilder={
    val newPage = value
    this.copy(pagination=newPage)
  }

  def setCursor(c: String): UserQueryBuilder = {
    this.copy(cursor= "\"" + c + "\"")
  }

  // builds a github object
  def build(): UserQuery = {

    // builds the query
    var newQuery = query
    newQuery = "{\"query\":\"" + "query listUser($specifics:String!, $pagination:Int!, $cursor:String) {"+
      "search(query:$specifics, type: USER, first:$pagination, after:$cursor) { " +
      "count: userCount "+
      "edges { "+
        "node { "+
          "... on User { "+
            "username: login "+
            "email "+
            "url "+
            "followers { followers: totalCount } "+
            "following { following: totalCount } "+
            "organizations(first:$pagination) { edges { node { name url } } } "+
            "watching(first:$pagination) { edges { node { name url } } } "+
            "repositories(first:$pagination) { edges { node { name url } } } "+
            "repositoriesContributedTo(first:$pagination) { edges { node { name url } } } "+
          "}" +
        "}" +
        "cursor" +
    "} } }\", "+
    "\"variables\":{\"specifics\":\"user:"+user+"\", \"pagination\":"+pagination+", \"cursor\":" + cursor + "}, " +
    "\"operationName\":\"listUser\"}"
    // returns an object with the built query command
    UserQuery(newQuery, this.copy())
  }
}
