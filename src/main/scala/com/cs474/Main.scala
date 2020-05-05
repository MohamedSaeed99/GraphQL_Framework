package com.cs474

import Query._

object Main extends App{
  val githubObject = new ClientBuilder[ClientBuilder.ConnectionParameters.Empty]()
    .setConnectionURL("https://api.github.com/graphql")
    .setHeader("Accept", "application/json")
    .setAuthorization("Bearer","f6f8623d4f23b15c9b60b328e9f77d49f28274a7")
    .build

//  val q = (new QueryCommand()).setRepoQuery().build
//  val response : List[Node] = (githubObject.executeQuery(q))
//  println(response)


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