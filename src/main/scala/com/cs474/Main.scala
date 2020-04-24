package com.cs474

import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.HttpClientBuilder

import scala.io.Source.fromInputStream

object Main extends App{
  val temp="{viewer {email login url}}"

  val client = HttpClientBuilder.create.build
  val githubObject = new ClientBuilder[ClientBuilder.ConnectionParameters.Empty]()
    .setConnectionURL("https://api.github.com/graphql")
    .setHeader("Accept", "application/json")
    .setAuthorization("Bearer","f6f8623d4f23b15c9b60b328e9f77d49f28274a7")
    .build


  // Testing flatmap overriden
  val myList: Seq[String] = Seq("Testing flat map", "one", "two")
  val c: Seq[Char] = githubObject.flatMap(myList)
  println(c)


  val query = new QueryCommand[QueryBuilder.QueryParameters.Empty]()
    .setRepo("MohamedSaeed99")
    .setLanguage(List("Java", "JavaScript"))
    .build

  val httpUriRequest = githubObject.connect;
  println(query.queryString)
  val gqlReq = new StringEntity( query.queryString )
  httpUriRequest.setEntity(gqlReq)

  val response = client.execute(httpUriRequest)
  System.out.println("Response:" + response)
  response.getEntity match {
    case null => System.out.println("Response entity is null")
    case x if x != null => {
      val respJson = fromInputStream(x.getContent).getLines.mkString
      System.out.println(respJson)
    }
  }
}