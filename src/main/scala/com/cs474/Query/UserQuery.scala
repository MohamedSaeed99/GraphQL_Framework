package com.cs474.Query

// User Search Response JSON
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

/*
query {
  search(query:"user:MohamedSaeed99", type: USER, first:1) {
    totalUsers: userCount
    edges{
      node{
        ... on User{
          username: login
          email
          url
          followers{
            followers: totalCount
          }
          following{
            following: totalCount
          }
          organizations(first:20){
            edges{
              node{
                name
              }
            }
          }
          watching(first:20){
            edges{
              node{
                name
              }
            }
          }
          repositories(first:20){
            edges{
              node{
                name
                url
              }
            }
          }
          repositoriesContributedTo(first:20){
            edges{
              node{
                name
                url
              }
            }
          }
        }
      }
    }
  }
}
 */

case class UserQuery(query:String) extends Query(query){
  override def queryString: String = query
}
case class UserQueryBuilder(user:String, pagination:Int=20, query:String=""){

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
