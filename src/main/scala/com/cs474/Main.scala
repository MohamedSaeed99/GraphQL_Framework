package com.cs474

import Query._
import com.typesafe.config.ConfigFactory


object Main extends App{
  val githubObject = new ClientBuilder[ClientBuilder.ConnectionParameters.Empty]()
    .setConnectionURL(GetConnectionUrl().urlString)
    .setHeader(Accept, Appjson)
    .setAuthorization(Bearer,GetKey().keyValue)
    .build

//  val q = (new QueryCommand()).setRepoQuery().build
//  val response : List[Node] = (githubObject.executeQuery(q))
//  println(response)

  val q = new QueryCommand().setRepoQuery().setLanguage(List(Python, Java)).build
  val response : List[Node] = (githubObject.executeQuery(q))
    .filter(LanguageFilter(_)(_<3).compare()).filter(CommitsFilter(_)(_>10).compare())
    .map(_.extract(List(GetCommitCount(), GetIssues(), GetLanguages(), GetPrimaryLanguage(), GetBody())))


//
//  val u1 = ((new QueryCommand()).setUserQuery().setUser("MDausch").build)
//  val ur1 : List[Node] = githubObject.executeQuery(u1)
//  println(ur1)

  //  val u1 = ((new QueryCommand()).setUserQuery().build)
  //  val ur1 : List[Node] = githubObject.executeQuery(u1).filter(FollowingFilter(_)(_<100).compare())
  //  println(ur1)


//

//  val i = (new QueryCommand()).setIssueQuery("test").build
//  val ir : List[Node] = githubObject.executeQuery(i)
//  println(ir)

//  !_.locked gets all the unlocked repos
//  _.locked gets all the locked repos
//  println(ir.filter(!_.locked).filter(Status(_)(_.equalsIgnoreCase("open")).compare))


}