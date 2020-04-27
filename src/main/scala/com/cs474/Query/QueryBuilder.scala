package com.cs474.Query

abstract class Query (query:String) {
  def queryString: String
}

//Builder to build the github object
class QueryCommand ()
{
  def setRepoQuery(): RepoQueryBuilder={
    val repoQuery:RepoQueryBuilder = new RepoQueryBuilder()
    repoQuery
  }

  def setUserQuery(name: String): UserQueryBuilder={
    val userQuery:UserQueryBuilder = new UserQueryBuilder(name)
    userQuery
  }

  def setIssueQuery(searchWord: String): IssueQueryBuilder={
    val issueQuery:IssueQueryBuilder = new IssueQueryBuilder("in:title " + searchWord)
    issueQuery
  }
}

