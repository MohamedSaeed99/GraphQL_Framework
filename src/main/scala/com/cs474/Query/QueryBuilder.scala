package com.cs474.Query

//Parses the response from github api
case class Followers(followers: Double)
case class Following(following: Double)
case class NameURLNode(name: String)
case class GitDataEdges(node: NameURLNode)
case class UserGitData(edges: List[GitDataEdges])
case class History( totalCommits: Option[Int] )
case class ObjectBis( history: Option[History] )
case class PrimaryLanguage( name: Option[String] )
case class Languages( totalCount: Double,  nodes: List[PrimaryLanguage] )
case class PullRequests( totalPulls: Option[Double] )
case class Issues( totalIssues: Option[Double] )
case class Stargazers( starCount: Double )
case class Owner( ownerLogin: Option[String] )


case class Author(
                   url: String,
                   login: String
                 )
case class UserInfo(
                 name: String,
                 login: String,
                 url: String
               )
case class UserEdges(node: UserInfo)
case class Assignees(edges: List[UserEdges])


case class Node(
                     repoName: Option[String],
                     repoDesc: Option[String],
                     url: Option[String],
                     `object`: Option[ObjectBis],
                     primaryLanguage: Option[PrimaryLanguage],
                     languages: Option[Languages],
                     pullRequests: Option[PullRequests],
                     issues: Option[Issues],
                     stargazers: Option[Stargazers],
                     owner: Option[Owner],
                     title: Option[String],
                     body: Option[String],
                     locked: Option[Boolean],
                     state: Option[String],
                     author: Option[Author],
                     username: Option[String],
                     email: Option[String],
                     followers: Option[Followers],
                     following: Option[Following],
                     organizations: Option[UserGitData],
                     watching: Option[UserGitData],
                     repositories: Option[UserGitData],
                     repositoriesContributedTo: Option[UserGitData]
                   )
case class Edge( node: Node, cursor: String )
case class Search( count: Int,  edges: List[Edge] )
case class Data( search: Search )
case class JSONFormat( data: Data )

//Abstract class for the different types of queries being built
abstract class Query (query:String) {
  def queryString: String
  def builder: QueryBuilder
}

abstract class QueryBuilder{
  def build: Query
  def setCursor(c: String): QueryBuilder
}


//Builder to build the query
class QueryCommand ()
{
//  Sets the type of search query to REPOSITORY type
  def setRepoQuery(): RepoQueryBuilder={
    val repoQuery:RepoQueryBuilder = RepoQueryBuilder()
    repoQuery
  }

//  Sets the type of search query to a USER type
  def setUserQuery(name: String): UserQueryBuilder={
    val userQuery:UserQueryBuilder = UserQueryBuilder(name)
    userQuery
  }

//  Sets the type of search query to a ISSUE type
  def setIssueQuery(searchWord: String): IssueQueryBuilder={
    val issueQuery:IssueQueryBuilder = IssueQueryBuilder("in:title " + searchWord)
    issueQuery
  }
}

