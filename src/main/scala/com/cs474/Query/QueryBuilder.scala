package com.cs474.Query

//Abstract class for the different types of queries being built
abstract class Query (query:String) {
  def queryString: String
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

