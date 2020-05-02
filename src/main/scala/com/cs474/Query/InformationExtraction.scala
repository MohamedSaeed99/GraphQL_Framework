package com.cs474.Query

abstract class InformationExtraction {
  def extractData(n:Node): Option[String]
}
//repoName: Option[String],
//repoDesc: Option[String],
//url: Option[String],
//`object`: Option[ObjectBis],
//primaryLanguage: Option[PrimaryLanguage],
//languages: Option[Languages],
//pullRequests: Option[PullRequests],
//issues: Option[Issues],
//stargazers: Option[Stargazers],
//owner: Option[Owner],
//title: Option[String],
//body: Option[String],
//locked: Option[Boolean],
//state: Option[String],
//author: Option[Author],
//username: Option[String],
//email: Option[String],
//followers: Option[Followers],
//following: Option[Following],
//repositories: Option[UserGitData],
case class GetRepositoryName() extends InformationExtraction {
  override def extractData(n:Node): Option[String] = if(n.repoName.isEmpty) None else Some("Repository Name: " + n.repoName.get)
}

case class GetRepositoryDescription() extends InformationExtraction {
  override def extractData(n:Node): Option[String] = if(n.repoName.isEmpty) None else Some("Repository Description: " + n.repoDesc.get)
}

case class GetUrl() extends InformationExtraction {
  override def extractData(n:Node): Option[String] = if(n.repoName.isEmpty) None else Some("Url: " + n.url.get)
}
case class GetCommitCount() extends InformationExtraction {
  override def extractData(n:Node): Option[String] = if(n.repoName.isEmpty) None else Some("Repository Name: " + n.`object`.get.history.get.totalCommits.get.toString)
}
case class GetPrimaryLanguage() extends InformationExtraction{
  override def extractData(n: Node): Option[String] = if(n.repoName.isEmpty) None else Some("Primary Language: "+ n.primaryLanguage.get.name.get)
}

case class GetLanguages() extends InformationExtraction{
  override def extractData(n: Node): Option[String] = {
    if(n.repoName.isEmpty)
      None
    else {
      var str = "Languages: " + n.languages.get.totalCount.toString + "\n"
      for (pl <- n.languages.get.nodes) {
        str += "\t" + pl.name.get + "\n"
      }
      Some(str)
    }
  }
}
case class GetPullReq() extends InformationExtraction{
  override def extractData(n: Node): Option[String] = if(n.repoName.isEmpty) None else Some("PullRequest Count: "+ n.pullRequests.get.totalPulls.get.toString)
}
case class GetIssues() extends InformationExtraction{
  override def extractData(n: Node): Option[String] = if(n.repoName.isEmpty) None else Some("Issue Count: "+ n.issues.get.totalIssues.get.toString)
}
case class GetStargazers() extends InformationExtraction{
  override def extractData(n: Node): Option[String] = if(n.repoName.isEmpty) None else Some("Star Count: "+ n.stargazers.get.starCount.toString)
}
case class GetOwner() extends InformationExtraction{
  override def extractData(n: Node): Option[String] = if(n.repoName.isEmpty) None else Some("Repository Owner: "+ n.owner.get.ownerLogin.get)
}
case class GetTitle() extends InformationExtraction{
  override def extractData(n: Node): Option[String] = if(n.repoName.isEmpty) None else Some("Issue Title: "+ n.title.get)
}
case class GetBody() extends InformationExtraction{
  override def extractData(n: Node): Option[String] = if(n.repoName.isEmpty) None else Some("Issue Body: "+ n.body.get)
}
case class GetLockedStatus() extends InformationExtraction{
  override def extractData(n: Node): Option[String] = if(n.repoName.isEmpty) None else Some("Issue Lock Status: "+ n.locked.get.toString)
}
case class GetStateStatus() extends InformationExtraction{
  override def extractData(n: Node): Option[String] = if(n.repoName.isEmpty) None else Some("Issue State Status: "+ n.state.get)
}
case class GetAuthor() extends InformationExtraction{
  override def extractData(n: Node): Option[String] = if(n.repoName.isEmpty) None else Some("Author Name: "+ n.author.get.login + "\nAuthor Url: " + n.author.get.url)
}
case class GetUsername() extends InformationExtraction{
  override def extractData(n: Node): Option[String] = if(n.repoName.isEmpty) None else Some("Username: "+ n.username.get)
}
case class GetEmail() extends InformationExtraction{
  override def extractData(n: Node): Option[String] = if(n.repoName.isEmpty) None else Some("Email: "+ n.email.get)
}
case class GetFollowers() extends InformationExtraction{
  override def extractData(n: Node): Option[String] = if(n.repoName.isEmpty) None else Some("Follower Count: "+ n.followers.get.followers)
}
case class GetFollowing() extends InformationExtraction{
  override def extractData(n: Node): Option[String] = if(n.repoName.isEmpty) None else Some("Following Count: "+ n.following.get.following)
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

