package com.cs474

import com.cs474.Query._
import com.typesafe.config.ConfigFactory
import org.apache.http.client.methods.HttpPost
import org.slf4j.{Logger, LoggerFactory}
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.HttpClientBuilder
import org.json4s._
import org.json4s.jackson.JsonMethods._

import scala.io.Source.fromInputStream


trait Header
trait Value

//Objects that represents strings
object Bearer extends Header{
  override def toString: String = "Bearer"
}
object Accept extends Header{
  override def toString: String = "Accept"
}
object Authorization extends Header{
  override def toString: String = "Authorization"
}
object Appjson extends Value{
  override def toString: String = "application/json"
}

// retrieves the auth key from the config file
case class GetKey() extends Value {
  def keyValue:String = {
    val config = ConfigFactory.load("application.conf")
    config.getString("OAUTH_KEY")
  }
}


//retrieves the url from the connection file
case class GetConnectionUrl() extends Value {
  def urlString:String = {
    val config = ConfigFactory.load("application.conf")
    config.getString("URL")
  }
}


// Connection to the Github API
case class GQLClient (connectionURL:String, headers: List[(String, String)]) {
  val Logger: Logger = LoggerFactory.getLogger( classOf[GQLClient])

  // constructs a uri request with the specified headers
  def connect: HttpPost = {
    val httpUriRequest = new HttpPost(connectionURL)
    for (header <- headers){
      httpUriRequest.addHeader(header._1, header._2)
    }
    httpUriRequest
  }

  // Sends the request and retrieves response
  private def execute[A](query: Query): JsonInput = {
    // Execute the query and get a response back
    val client = HttpClientBuilder.create.build
    val request = this.connect

    Logger.info("Connected to Github API server")
    val requestQuery = new StringEntity(query.queryString)
    request.setEntity(requestQuery)

    // sends the request
    val response = client.execute(request)
    Logger.info("Request sent")

    // returns an input stream if it returns something
    response.getEntity match {
      case null =>
        Logger.error("Response returned null")
        null
      case x if x != null =>
        Logger.info("Request was a success")
        fromInputStream(x.getContent).getLines.mkString
    }
  }

  //  Retrieves more data from github api by pagination
  private def paginateRequest[A <: Query](q: A, nodes: List[Edges], requestCount: Int ): List[Edges] = {
    Logger.info("Fetching Data: {}/{}", nodes.length, requestCount)
//    println("Fetching Repo Data: " + nodes.length + " / " + requestCount)

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
    val jsonRes = this.execute(q.builder.setCursor(cursor).build)
    implicit val formats = DefaultFormats
    val res = parse(jsonRes).extract[JSONFormat]
    val ret = res.data.search.edges

    // Get the rest of the data
    paginateRequest(q, nodes:::ret, requestCount)
  }

  // Executes a query
  def executeQuery[A <: Query](q: A): List[Node] = {
    Logger.info("Fetching Initial Data")
    val jsonRes = this.execute(q)
    implicit val formats = DefaultFormats
    val res = parse(jsonRes).extract[JSONFormat]

    if(res.data != null) {
      val nonPaginatedData = paginateRequest(q, res.data.search.edges, res.data.search.count)
      nonPaginatedData.map(_.node)
    }
    else{
      Logger.error("API request error: " + jsonRes)
      List[Node]()
    }
  }
}


//Builder to build the github object
case class ClientBuilder[ConnectionParameters <: ClientBuilder.ConnectionParameters] (
  connectionURL:String = "", // Required
  headers: List[(String, String)] = List()) // Optional
{

  import ClientBuilder.ConnectionParameters._
  val Logger: Logger = LoggerFactory.getLogger( classOf[ClientBuilder[ConnectionParameters]])

  // copies and returns this object with connection Url modified
  def setConnectionURL(url: String): ClientBuilder[ConnectionParameters with ConnectionURL] = {
    Logger.info("Setting up connection url: {}", url)
    this.copy(connectionURL = url)
  }

  // copies and returns this object with the headers stored in the data structure
  def setHeader(key: Header, value: Value): ClientBuilder[ConnectionParameters with Empty] = {
    Logger.info("Setting up connection headers: ({}, {})", key, value)
    var newHeaders = headers
    newHeaders = newHeaders:+(key.toString, value.toString)
    this.copy(headers = newHeaders)
  }

  // copies and returns this object with the headers stored in the data structure
  def setAuthorization(authType: Header, value: String): ClientBuilder[ConnectionParameters with Empty] = {
    var newHeaders = headers
    Logger.info("Setting up connection authorization: ({}, {})", authType.toString, value)
    newHeaders = newHeaders:+(Authorization.toString, authType.toString + " " + value)
    this.copy(headers = newHeaders)
  }

  // builds a github object
  def build(implicit ev: ConnectionParameters =:= RequiredClientParameters): GQLClient = {
    Logger.info("Building GQLClient")
    GQLClient(connectionURL, headers)
  }
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