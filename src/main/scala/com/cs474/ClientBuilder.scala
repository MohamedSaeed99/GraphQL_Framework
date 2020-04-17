package com.cs474

import scala.collection.mutable.ListBuffer

object ClientBuilder {
  class GQLClient(connectionURL: String, headers: ListBuffer[String]) {
    def display: Unit ={
      println("Current connection url: " + connectionURL)
      println("Current header list: " + headers.toList)
    }
  }

  abstract class TRUE
  abstract class FALSE

  class ClientBuilder
  [MandatoryConnection]
  (val connectionURL:String, val headers: ListBuffer[String]) {

    def setConnectionURL(connectionURL: String) =
      new ClientBuilder[TRUE](connectionURL, headers)

    def setHeader(key: String, value: String) = {
      var newHeaders = headers
      newHeaders += key
      newHeaders += value
      new ClientBuilder[MandatoryConnection](connectionURL, headers)
    }

    def setAuthorization(authType: String, value: String) =
      this.setHeader("Authorization", authType + " " + value)
  }

  implicit def enableBuild(builder:ClientBuilder[TRUE]) = new {
    def build() =
      new GQLClient(builder.connectionURL, builder.headers);
  }

  def builder = new ClientBuilder[FALSE]("", new ListBuffer[String])
}
