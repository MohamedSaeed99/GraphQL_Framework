package com.cs474


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
case class UserQuery(user:String, pagination:Int=20, query:String=""){

  def setPagination(value:Int): UserQuery={
    val newPage = value
    this.copy(pagination=newPage)
  }


  // builds a github object
  def build(): Query = {
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
            "organizations(first:$pagination) { edges { node{ name } } } "+
            "watching(first:$pagination) { edges { node{ name } } } "+
            "repositories(first:20) { edges { node { name url } } } "+
            "repositoriesContributedTo(first:20) { edges { node { name url } } } "+
      "} } } } }\", "+
      "\"variables\":{\"specifics\":\"user:"+user+"\", \"pagination\":"+pagination+"}, " +
      "\"operationName\":\"listUser\"}"
    // returns an object with the built query command
    Query(newQuery)
  }
}
