package com.cs474

//class Authenticate{
//  val Logger = LoggerFactory.getLogger( classOf[Authenticate])
//  Logger.info("Hello from the Pizza class");
//}

object Main {
  def main(args: Array[String]): Unit = {

    //    val BASE_GHQL_URL = "https://api.github.com/graphql"
    //    val temp="{viewer {email login url}}"
    ////    implicit val formats = DefaultFormats
    //
    //    val client = HttpClientBuilder.create.build
    //    val httpUriRequest = new HttpPost(BASE_GHQL_URL)
    //    httpUriRequest.addHeader("Authorization", "Bearer Token")
    //    httpUriRequest.addHeader("Accept", "application/json")
    //    val gqlReq = new StringEntity("{\"query\":\"" + temp + "\"}" )
    //    httpUriRequest.setEntity(gqlReq)
    //
    //    val response = client.execute(httpUriRequest)
    //    System.out.println("Response:" + response)
    //    response.getEntity match {
    //      case null => System.out.println("Response entity is null")
    //      case x if x != null => {
    //        val respJson = fromInputStream(x.getContent).getLines.mkString
    //        System.out.println(respJson)
    ////        val viewer = parse(respJson).extract[Data]
    ////        System.out.println(viewer)
    ////        System.out.println(write(viewer))
    //      }
    //    }

    val client: GQLClient = new ClientBuilder[ClientBuilder.ConnectionParameters.Empty]()
      .setConnectionURL("https://api.github.com/graphql")
      .setHeader("Accept", "application/json")
      .setAuthorization("Bearer","f6f8623d4f23b15c9b60b328e9f77d49f28274a7")
      .build

    client.display
  }
}