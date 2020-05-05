package com.cs474.Query

abstract class InformationExtraction {
  def extractData(n:Node): Option[String]
}

case class GetRepositoryName() extends InformationExtraction {
  override def extractData(n:Node): Option[String] = if(n.repoName.isEmpty) None else Some("Repository Name: " + n.repoName.get)
}

case class GetRepositoryDescription() extends InformationExtraction {
  override def extractData(n:Node): Option[String] = if(n.repoDesc.isEmpty) None else Some("Repository Description: " + n.repoDesc.get)
}

case class GetUrl() extends InformationExtraction {
  override def extractData(n:Node): Option[String] = if(n.url.isEmpty) None else Some("Url: " + n.url.get)
}
case class GetCommitCount() extends InformationExtraction {
  override def extractData(n:Node): Option[String] = if(n.`object`.isEmpty) None else Some("Commit Total: " + n.`object`.get.history.get.totalCommits.get.toString)
}
case class GetPrimaryLanguage() extends InformationExtraction{
  override def extractData(n: Node): Option[String] = if(n.primaryLanguage.isEmpty) None else Some("Primary Language: "+ n.primaryLanguage.get.name)
}

case class GetLanguages() extends InformationExtraction{
  override def extractData(n: Node): Option[String] = {
    if(n.languages.isEmpty)
      None
    else {
      var str = "Languages: " + n.languages.get.totalCount.toString + "\n"
      for (pl <- n.languages.get.nodes) {
        str += "\t" + pl.name + "\n"
      }
      Some(str)
    }
  }
}
case class GetPullReq() extends InformationExtraction{
  override def extractData(n: Node): Option[String] = if(n.pullRequests.isEmpty) None else Some("PullRequest Count: "+ n.pullRequests.get.totalPulls.get.toString)
}
case class GetIssues() extends InformationExtraction{
  override def extractData(n: Node): Option[String] = if(n.issues.isEmpty) None else Some("Issue Count: "+ n.issues.get.totalIssues.get.toString)
}
case class GetStargazers() extends InformationExtraction{
  override def extractData(n: Node): Option[String] = if(n.stargazers.isEmpty) None else Some("Star Count: "+ n.stargazers.get.starCount.toString)
}
case class GetOwner() extends InformationExtraction{
  override def extractData(n: Node): Option[String] = if(n.owner.isEmpty) None else Some("Repository Owner: "+ n.owner.get.ownerLogin.get)
}
case class GetTitle() extends InformationExtraction{
  override def extractData(n: Node): Option[String] = if(n.title.isEmpty) None else Some("Issue Title: "+ n.title.get)
}
case class GetBody() extends InformationExtraction{
  override def extractData(n: Node): Option[String] = if(n.body.isEmpty) None else Some("Issue Body: "+ n.body.get)
}
case class GetLockedStatus() extends InformationExtraction{
  override def extractData(n: Node): Option[String] = if(n.locked.isEmpty) None else Some("Issue Lock Status: "+ n.locked.get.toString)
}
case class GetStateStatus() extends InformationExtraction{
  override def extractData(n: Node): Option[String] = if(n.state.isEmpty) None else Some("Issue State Status: "+ n.state.get)
}
case class GetAuthor() extends InformationExtraction{
  override def extractData(n: Node): Option[String] = if(n.author.isEmpty) None else Some("Author Name: "+ n.author.get.login + "\nAuthor Url: " + n.author.get.url)
}
case class GetUsername() extends InformationExtraction{
  override def extractData(n: Node): Option[String] = if(n.username.isEmpty) None else Some("Username: "+ n.username.get)
}
case class GetEmail() extends InformationExtraction{
  override def extractData(n: Node): Option[String] = if(n.email.isEmpty) None else Some("Email: "+ n.email.get)
}
case class GetFollowers() extends InformationExtraction{
  override def extractData(n: Node): Option[String] = if(n.followers.isEmpty) None else Some("Follower Count: "+ n.followers.get.followers)
}
case class GetFollowing() extends InformationExtraction{
  override def extractData(n: Node): Option[String] = if(n.following.isEmpty) None else Some("Following Count: "+ n.following.get.following)
}
case class GetRepositories() extends InformationExtraction{
  override def extractData(n: Node): Option[String] = {
    if(n.repositories.isEmpty){
      None
    }
    else {
      var str = "List of Repositories: \n"
      for (r <- n.repositories.get.edges) {
        str += "\t" + r.node.name + "\n"
      }
      Some(str)
    }
  }
}

