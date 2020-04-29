package com.cs474.Query

//parsing of the USER type response
case class Followers(followers: Double)
case class Following(following: Double)
case class NameURLNode(name: String)
case class GitDataEdges(node: NameURLNode)
case class UserGitData(edges: List[GitDataEdges])
case class UserNode(
                 username: String,
                 email: String,
                 url: String,
                 followers: Followers,
                 following: Following,
                 organizations: UserGitData,
                 watching: UserGitData,
                 repositories: UserGitData,
                 repositoriesContributedTo: UserGitData
               )
case class UserSearchEdges(node: UserNode)
case class UserSearchJSON(
                   edges: List[UserSearchEdges]
                 )
case class UserDataJSON(search: UserSearchJSON)
case class UserSearchJSONFormat(data: UserDataJSON)

// Contains built query string
case class UserQuery(query:String) extends Query(query){
  override def queryString: String = query
}

//UserQuery builder that would build a type USER search query
case class UserQueryBuilder(user:String, pagination:Int=100, query:String=""){

//  Sets the number of repos that can be visible at once
  def setPagination(value:Int): UserQueryBuilder={
    val newPage = value
    this.copy(pagination=newPage)
  }

  // builds a github object
  def build(): UserQuery = {

    // builds the query
    var newQuery = query
    newQuery = "{\"query\":\"" + "query listUser($specifics:String!, $pagination:Int!) {"+
      "search(query:$specifics, type: USER, first:$pagination) { " +
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
      "} } } } }\", "+
      "\"variables\":{\"specifics\":\"user:"+user+"\", \"pagination\":"+pagination+"}, " +
      "\"operationName\":\"listUser\"}"

    // returns an object with the built query command
    UserQuery(newQuery)
  }
}
