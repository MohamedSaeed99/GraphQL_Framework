package com.cs474

import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.HttpClientBuilder

object Main extends App{
  val temp="{viewer {email login url}}"

  val githubClient = new ClientBuilder[ClientBuilder.ConnectionParameters.Empty]()
    .setConnectionURL("https://api.github.com/graphql")
    .setHeader("Accept", "application/json")
    .setAuthorization("Bearer","f6f8623d4f23b15c9b60b328e9f77d49f28274a7")
    .build

  val client = HttpClientBuilder.create.build
  val gqlReq = new StringEntity("{\"query\":\"" + temp + "\"}" )
  val response = client.execute(githubClient.connect)
  System.out.println("Response:" + response)


  githubClient.testConnection
}