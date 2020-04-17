package com.cs474

case class GQLClient (val connectionURL:String, val headers: List[(String, String)]) {
  def display: Unit ={
    println("Current connection url: " + connectionURL)
    println("Current header list: " + headers.toList)
  }
}

case class ClientBuilder[ConnectionParameters <: ClientBuilder.ConnectionParameters] (
  connectionURL:String = "", // Required
  headers: List[(String, String)] = List()) // Optional
{
  import ClientBuilder.ConnectionParameters._

  def setConnectionURL(url: String): ClientBuilder[ConnectionParameters with ConnectionURL] =
    this.copy(connectionURL = url)

  def setHeader(key: String, value: String): ClientBuilder[ConnectionParameters with Empty] = {
    var newHeaders = headers
    newHeaders = newHeaders:+(key, value)
    this.copy(headers = newHeaders)
  }

  def setAuthorization(authType: String, value: String) =
    this.setHeader("Authorization", authType + " " + value)

  def build(implicit ev: ConnectionParameters =:= RequiredClientParameters): GQLClient = GQLClient(connectionURL, headers)
}

object ClientBuilder {
  sealed trait ConnectionParameters
  object ConnectionParameters {
    sealed trait Empty extends ConnectionParameters
    sealed trait ConnectionURL extends ConnectionParameters
    sealed trait Headers extends ConnectionParameters

    type RequiredClientParameters = Empty with ConnectionURL
  }
}