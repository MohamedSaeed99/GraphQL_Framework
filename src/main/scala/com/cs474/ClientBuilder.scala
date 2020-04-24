package com.cs474

import org.apache.http.client.methods.HttpPost

// Object that is used to connect to the GitHub Api
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.HttpClientBuilder

import org.json4s._
import org.json4s.jackson.JsonMethods._


import scala.io.Source.fromInputStream


case class History(
                    totalCommits: Double
                  )
case class ObjectBis(
                      history: History
                    )
case class PrimaryLanguage(
                            name: String
                          )
case class Languages(
                      totalCount: Double,
                      nodes: List[PrimaryLanguage]
                    )
case class PullRequests(
                         totalPulls: Double
                       )
case class Issues(
                   totalIssues: Double
                 )
case class Stargazers(
                       starCount: Double
                     )
case class Owner(
                  ownerLogin: String
                )
case class Node(
                 repoName: String,
                 repoDesc: String,
                 repoURL: String,
                 `object`: ObjectBis,
                 primaryLanguage: PrimaryLanguage,
                 languages: Languages,
                 pullRequests: PullRequests,
                 issues: Issues,
                 stargazers: Stargazers,
                 collaborators: String,
                 owner: Owner
               )
case class Edges(
                  node: Node
                )
case class Search(
                   repositoryCount: Double,
                   edges: List[Edges]
                 )
case class Data(
                 search: Search
               )
case class RepoSearchJsonFormat(
                           data: Data
                         )




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
    f.flatMap(_.toUpperCase)
  }


  def flatMap(query: Query):  List[Node] = {
    println("Flat map overriden Query")

    // Execute the query and get a response back
    val request = this.connect;
    val requestQuery = new StringEntity(query.queryString)
    request.setEntity(requestQuery)

    val response = client.execute(request)

    System.out.println("Response:" + response)
    response.getEntity match {
      case null => {
        System.out.println("Response entity is null")
        // parse("{}").asInstanceOf[JObject]
        List[Node]()
      }
      case x if x != null => {
        val respJson = fromInputStream(x.getContent).getLines.mkString
        // var parsed = parse(respJson).asInstanceOf[JObject]
        // parse(respJson).asInstanceOf[JObject]


        implicit val formats = DefaultFormats
        // println(pretty(render(parsed)))

        val res = parse(respJson).extract[RepoSearchJsonFormat]
        println(res.data.search.edges)

        res.data.search.edges.map(_.node)
      }
    }
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