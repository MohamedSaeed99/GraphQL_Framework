package com.cs474

import org.apache.http.client.methods.HttpPost

// Object that is used to connect to the GitHub Api
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.HttpClientBuilder
import scala.io.Source.fromInputStream

case class GQLClient (val connectionURL:String, val headers: List[(String, String)]) {
  val client = HttpClientBuilder.create.build

  def display: Unit ={
    println("Current connection url: " + connectionURL)
    println("Current header list: " + headers(0)._1)
  }

  // constructs a uri request with the specified headers
  def connect: HttpPost = {
    val httpUriRequest = new HttpPost(connectionURL)
    for (header <- headers){
      httpUriRequest.addHeader(header._1, header._2)
    }
    httpUriRequest
  }

  def testConnection: Unit = {
    val client = HttpClientBuilder.create.build
    val connection = connect

    val temp="{viewer {email login url}}"
    val gqlReq = new StringEntity("{\"query\":\"" + temp + "\"}" )
    connection.setEntity(gqlReq)

    val response = client.execute(connection)
    System.out.println("Response:" + response)
    response.getEntity match {
      case null => System.out.println("Response entity is null")
      case x if x != null => {
        val respJson = fromInputStream(x.getContent).getLines.mkString
        System.out.println(respJson)
      }
    }
  }

  def flatMap(f: Seq[String]):  Seq[Char] = {
    println("Flat map overriden")

    // TODO: Take in a built query

    // Execute the query and get a response back

    // Return sequence of the returned objects
    f.flatMap(_.toUpperCase)
  }




}

//Builder to build the github object
case class ClientBuilder[ConnectionParameters <: ClientBuilder.ConnectionParameters] (
  connectionURL:String = "", // Required
  headers: List[(String, String)] = List()) // Optional
{
  import ClientBuilder.ConnectionParameters._

  // copies and returns this object with connection Url modified
  def setConnectionURL(url: String): ClientBuilder[ConnectionParameters with ConnectionURL] =
    this.copy(connectionURL = url)

  // copies and returns this object with the headers stored in the data structure
  def setHeader(key: String, value: String): ClientBuilder[ConnectionParameters with Empty] = {
    var newHeaders = headers
    newHeaders = newHeaders:+(key, value)
    this.copy(headers = newHeaders)
  }

  // copies and returns this object with the headers stored in the data structure
  def setAuthorization(authType: String, value: String) =
    this.setHeader("Authorization", authType + " " + value)

  // builds a github object
  def build(implicit ev: ConnectionParameters =:= RequiredClientParameters): GQLClient = GQLClient(connectionURL, headers)
}

// sealed traits specifies which parameters are required in creating the object
object ClientBuilder {
  sealed trait ConnectionParameters
  object ConnectionParameters {
    sealed trait Empty extends ConnectionParameters
    sealed trait ConnectionURL extends ConnectionParameters
    sealed trait Headers extends ConnectionParameters

    type RequiredClientParameters = Empty with ConnectionURL
  }
}