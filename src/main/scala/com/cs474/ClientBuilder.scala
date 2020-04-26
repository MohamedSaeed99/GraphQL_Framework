package com.cs474

import com.cs474.Query._
import org.apache.http.HttpResponse
import org.apache.http.client.methods.HttpPost

// Object that is used to connect to the GitHub Api
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.HttpClientBuilder

import org.json4s._
import org.json4s.jackson.JsonMethods._


import scala.io.Source.fromInputStream


case class GQLClient (val connectionURL:String, val headers: List[(String, String)]) {

  // constructs a uri request with the specified headers
  def connect: HttpPost = {
    val httpUriRequest = new HttpPost(connectionURL)
    for (header <- headers){
      httpUriRequest.addHeader(header._1, header._2)
    }
    httpUriRequest
  }


//  def execute[A <: Query, B](query: A): List[B] = {
//    println("Flat map overriden Query")
//
//    // Execute the query and get a response back
//    val request = this.connect;
//    val requestQuery = new StringEntity(query.queryString)
//    request.setEntity(requestQuery)
//
//    val response = client.execute(request)
//
//    response.getEntity match {
//      case null => {
//        System.out.println("Response entity is null")
//        List[B]()
//      }
//      case x if x != null => {
//        val respJson = fromInputStream(x.getContent).getLines.mkString
//
//        implicit val formats = DefaultFormats
//
//        val res = parse(respJson).extract[B]
//
//        res.data.search.edges.map(_.node)
//      }
//    }
//  }


  private def execute[A](query: Query): JsonInput = {
    val client = HttpClientBuilder.create.build

    // Execute the query and get a response back
    val request = this.connect;
    val requestQuery = new StringEntity(query.queryString)
    request.setEntity(requestQuery)

    val response = client.execute(request)
    response.getEntity match {
      case null => {
        println("Response entity is null")
        return null
      }
      case x if x != null => {
        fromInputStream(x.getContent).getLines.mkString
      }
    }
  }

  def executeQuery(q: RepoQuery): List[RepoNode] = {
    val jsonRes = this.execute(q)
    implicit val formats = DefaultFormats
    val res = parse(jsonRes).extract[RepoSearchJsonFormat]
    res.data.search.edges.map(_.node)
  }

  def executeQuery(q: UserQuery): List[UserNode] = {
    val jsonRes = this.execute(q)
    implicit val formats = DefaultFormats
    val res = parse(jsonRes).extract[UserSearchJSONFormat]
    res.data.search.edges.map(_.node)
  }

  def executeQuery(q: IssueQuery): List[IssueNode] = {
    val jsonRes = this.execute(q)
    implicit val formats = DefaultFormats
    val res = parse(jsonRes).extract[IssueSearchJSONFormat]
    res.data.search.edges.map(_.node)
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