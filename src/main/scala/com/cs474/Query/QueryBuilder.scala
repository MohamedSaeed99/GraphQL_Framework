package com.cs474.Query

//Parses the response from github api
case class Followers(followers: Double)
case class Following(following: Double)
case class NameURLNode(name: String)
case class GitDataEdges(node: NameURLNode)
case class UserGitData(totalCount: Double, edges: List[GitDataEdges])
case class History( totalCommits: Option[Int] )
case class ObjectBis( history: Option[History] )
case class PrimaryLanguage( name: String )
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
           location: Option[String],
           repositories: Option[UserGitData]
         ){
  //  goes extracts the data from each node
  def extract[A<:InformationExtraction] (l: List[A]): Node={
    for(dataInfo <- l){
      val str: Option[String] = dataInfo.extractData(this)
      if(!str.isEmpty) {
        println(str.get)
      }
    }
    println("==========================================")
    this
  }
}
case class Edges( node: Node, cursor: String )
case class Search( count: Int,  edges: List[Edges] )
case class Data( search: Search )
case class JSONFormat( data: Data )


//Abstract class for the different types of queries being built
abstract class Query (query:String) {
  def queryString: String
  def builder: QueryBuilder
}

//Abstract class the is inherited by all query type builders
abstract class QueryBuilder{
  def build: Query
  def setCursor(c: String): QueryBuilder
}


//Builder to build the query
class QueryCommand ()
{

  // TODO
//  def setMaximumNumberOfResults(numResults: Int): QueryCommand = {
//
//  }

  // Sets the type of search query to REPOSITORY type
  def setRepoQuery(): RepoQueryBuilder={
    val repoQuery:RepoQueryBuilder = RepoQueryBuilder()
    repoQuery
  }

  // Sets the type of search query to a USER type
  def setUserQuery(): UserQueryBuilder={
    val userQuery:UserQueryBuilder = UserQueryBuilder()
    userQuery
  }

  // Sets the type of search query to a ISSUE type
  def setIssueQuery(searchWord: String): IssueQueryBuilder={
    val issueQuery:IssueQueryBuilder = IssueQueryBuilder("in:title " + searchWord)
    issueQuery
  }
}

