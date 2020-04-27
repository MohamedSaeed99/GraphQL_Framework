package com.cs474

import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.HttpClientBuilder
import org.json4s.JObject
import org.json4s.JsonAST.JArray
import org.json4s.jackson.JsonMethods.{parse, pretty, render}

import scala.io.Source.fromInputStream
import Query._

object Main extends App{
  val githubObject = new ClientBuilder[ClientBuilder.ConnectionParameters.Empty]()
    .setConnectionURL("https://api.github.com/graphql")
    .setHeader("Accept", "application/json")
    .setAuthorization("Bearer","f6f8623d4f23b15c9b60b328e9f77d49f28274a7")
    .build


  val q = (new QueryCommand()).setRepoQuery().build
  val response : List[RepoNode] = githubObject.executeQuery(q)
  println(response)

  val u1 = (new QueryCommand()).setUserQuery("MDausch").build
  val ur1 : List[UserNode] = githubObject.executeQuery(u1)
  println(ur1)

  val i = (new QueryCommand()).setIssueQuery("Test").build
  val ir : List[IssueNode] = githubObject.executeQuery(i)
  println(ir)
}