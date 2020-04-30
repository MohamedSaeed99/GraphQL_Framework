package com.cs474

import com.cs474.Query._
import org.apache.http.client.methods.HttpPost

// Object that is used to connect to the GitHub Api
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.HttpClientBuilder

import org.json4s._
import org.json4s.jackson.JsonMethods._
import scala.io.Source.fromInputStream

//Connection to the Github API
case class GQLClient (val connectionURL:String, val headers: List[(String, String)]) {
  private val client = HttpClientBuilder.create.build

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

  //  Sends the request and retrieves response
  private def execute[A](query: Query): JsonInput = {
    // Execute the query and get a response back
    val request = this.connect;
    val requestQuery = new StringEntity(query.queryString)
    request.setEntity(requestQuery)

//    sends the request
    val response = client.execute(request)

//    returns an input stream if it returns something
    response.getEntity match {
      case null => {
        null
      }
      case x if x != null => {
        fromInputStream(x.getContent).getLines.mkString
      }
    }
  }

  private def paginateRepoRequest(q: RepoQuery, nodes: List[RepoEdges], requestCount: Int ): List[RepoEdges] = {
    println("Fetching Repo Data: " + nodes.length + " / " + requestCount)

    // If we've got all of the data, or a good chunk of it return the data
    if(nodes.length >= requestCount){
      return nodes
    } else if(nodes.length >= 500) {
      println("Stopping early at 500 results")
      return nodes
    }

    // Sleep for a bit before making the next request
    Thread.sleep(2000)

    // Otherwise get the cursor of the last node
    val cursor = nodes.last.cursor

    // Rebuild the query but with the last cursor set
    val jsonRes = this.execute(q.builder.setCursor(cursor).build())
    println(jsonRes)
    implicit val formats = DefaultFormats
    val res = parse(jsonRes).extract[RepoSearchJsonFormat]
    val ret = res.data.search.edges

    // Get the rest of the data
    return paginateRepoRequest(q, nodes:::ret, requestCount)
  }

  //  Executes a REPOSITORY type query
  def executeQuery(q: RepoQuery): List[RepoNode] = {
    println("Fetching Initial Repo Data")
    val jsonRes = this.execute(q)
    implicit val formats = DefaultFormats
    val res = parse(jsonRes).extract[RepoSearchJsonFormat]
    val nonPaginatedData = paginateRepoRequest(q, res.data.search.edges, res.data.search.repositoryCount)

    return nonPaginatedData.map(_.node)
  }

  private def paginateUserRequest(q: UserQuery, nodes: List[UserSearchEdges], requestCount: Double ): List[UserSearchEdges] = {
    println("Fetching User Data: " + nodes.length + " / " + requestCount)

    // If we've got all of the data, or a good chunk of it return the data
    if(nodes.length >= requestCount){
      return nodes
    } else if(nodes.length >= 500) {
      println("Stopping early at 500 results")
      return nodes
    }

    // Sleep for a bit before making the next request
    Thread.sleep(2000)

    // Otherwise get the cursor of the last node
    val cursor = nodes.last.cursor

    // Rebuild the query but with the last cursor set
    val jsonRes = this.execute(q.builder.setCursor(cursor).build())
    implicit val formats = DefaultFormats
    val res = parse(jsonRes).extract[UserSearchJSONFormat]
    val ret = res.data.search.edges

    // Get the rest of the data
    return paginateUserRequest(q, nodes:::ret, requestCount)
  }

  // Executes a USER type query
  def executeQuery(q: UserQuery): List[UserNode] = {
    println("Fetching Initial User Data")
    val jsonRes = this.execute(q)
    implicit val formats = DefaultFormats
    val res = parse(jsonRes).extract[UserSearchJSONFormat]
    val nonPaginatedData = paginateUserRequest(q, res.data.search.edges, res.data.search.userCount)

    return nonPaginatedData.map(_.node)
  }


  private def paginateIssueRequest(q: IssueQuery, nodes: List[IssueSearchEdges], requestCount: Int ): List[IssueSearchEdges] = {
    println("Fetching Issue Data: " + nodes.length + " / " + requestCount)

    // If we've got all of the data, or a good chunk of it return the data
    if(nodes.length >= requestCount){
      return nodes
    } else if(nodes.length >= 500) {
      println("Stopping early at 500 results")
      return nodes
    }

    // Sleep for a bit before making the next request
    Thread.sleep(2000)

    // Otherwise get the cursor of the last node
    val cursor = nodes.last.cursor

    // Rebuild the query but with the last cursor set
    val jsonRes = this.execute(q.builder.setCursor(cursor).build())
    println(jsonRes)

    implicit val formats = DefaultFormats
    val res = parse(jsonRes).extract[IssueSearchJSONFormat]
    val ret = res.data.search.edges

    // Get the rest of the data
    return paginateIssueRequest(q, nodes:::ret, requestCount)
  }

  //  Executes a ISSUE type query
  def executeQuery(q: IssueQuery): List[IssueNode] = {
    println("Fetching Initial Issue Data")
    val jsonRes = this.execute(q)
    println(jsonRes)

    implicit val formats = DefaultFormats
    val res = parse(jsonRes).extract[IssueSearchJSONFormat]
    val nonPaginatedData = paginateIssueRequest(q, res.data.search.edges, res.data.search.issueCount)

    return nonPaginatedData.map(_.node)
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