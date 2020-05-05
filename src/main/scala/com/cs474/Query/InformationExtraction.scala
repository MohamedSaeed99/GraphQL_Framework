package com.cs474.Query
import org.slf4j.{Logger, LoggerFactory};

abstract class InformationExtraction {
  def extractData(n:Node): Option[String]
}

//extracts the repo name
case class GetRepositoryName() extends InformationExtraction {
  val Logger: Logger = LoggerFactory.getLogger( classOf[GetRepositoryName])

  override def extractData(n:Node): Option[String] = if(n.repoName.isEmpty){
    Logger.warn("Repository name attribute is empty")
    None
  } else Some("Repository Name: " + n.repoName.get)
}

//extracts the repo description
case class GetRepositoryDescription() extends InformationExtraction {
  val Logger: Logger = LoggerFactory.getLogger( classOf[GetRepositoryDescription])

  override def extractData(n:Node): Option[String] = if(n.repoDesc.isEmpty) {
    Logger.warn("Repository description attribute is empty")
    None
  } else Some("Repository Description: " + n.repoDesc.get)
}

//extracts the repo url
case class GetUrl() extends InformationExtraction {
  val Logger: Logger = LoggerFactory.getLogger( classOf[GetUrl])

  override def extractData(n:Node): Option[String] = if(n.url.isEmpty) {
    Logger.warn("Url attribute is empty")
    None
  } else Some("Url: " + n.url.get)
}

//extracts the total number of commits in a repo
case class GetCommitCount() extends InformationExtraction {
  val Logger: Logger = LoggerFactory.getLogger( classOf[GetCommitCount])

  override def extractData(n:Node): Option[String] = if(n.`object`.isEmpty) {
    Logger.warn("Commit count attribute is empty")
    None
  } else Some("Commit Total: " + n.`object`.get.history.get.totalCommits.get.toString)
}

//extracts the primary language used in a repo
case class GetPrimaryLanguage() extends InformationExtraction{
  val Logger: Logger = LoggerFactory.getLogger( classOf[GetPrimaryLanguage])

  override def extractData(n: Node): Option[String] = if(n.primaryLanguage.isEmpty) {
    Logger.warn("Primary language attribute is empty")
    None
  } else Some("Primary Language: "+ n.primaryLanguage.get.name)
}

//extracts the total number and the languages used in a repo
case class GetLanguages() extends InformationExtraction{
  val Logger: Logger = LoggerFactory.getLogger( classOf[GetLanguages])

  override def extractData(n: Node): Option[String] = {
    if (n.languages.isEmpty) {
      Logger.warn("Languages attribute is empty")
      None
    } else {
      var str = "Languages: " + n.languages.get.totalCount.toString + "\n"
      for (pl <- n.languages.get.nodes) {
        str += "\t" + pl.name + "\n"
      }
      Some(str)
    }
  }
}

//extracts the total number of pull requests of a repo
case class GetPullReq() extends InformationExtraction{
  val Logger: Logger = LoggerFactory.getLogger( classOf[GetPullReq])

  override def extractData(n: Node): Option[String] = if(n.pullRequests.isEmpty) {
    Logger.warn("Pull request attribute is empty")
    None
  } else Some("PullRequest Count: "+ n.pullRequests.get.totalPulls.get.toString)
}

//extracts the total number of issues of a repo
case class GetIssues() extends InformationExtraction{
  val Logger: Logger = LoggerFactory.getLogger( classOf[GetIssues])

  override def extractData(n: Node): Option[String] = if(n.issues.isEmpty) {
    Logger.warn("Issues attribute is empty")
    None
  } else Some("Issue Count: "+ n.issues.get.totalIssues.get.toString)
}

//extracts the star count of a repo
case class GetStargazers() extends InformationExtraction{
  val Logger: Logger = LoggerFactory.getLogger( classOf[GetStargazers])

  override def extractData(n: Node): Option[String] = if(n.stargazers.isEmpty) {
    Logger.warn("Star count attribute is empty")
    None
  } else Some("Star Count: "+ n.stargazers.get.starCount.toString)
}

//extracts the owner of a repo
case class GetOwner() extends InformationExtraction{
  val Logger: Logger = LoggerFactory.getLogger( classOf[GetOwner])

  override def extractData(n: Node): Option[String] = if(n.owner.isEmpty) {
    Logger.warn("Owner attribute is empty")
    None
  } else Some("Repository Owner: "+ n.owner.get.ownerLogin.get)
}

//extracts the title of an issue
case class GetTitle() extends InformationExtraction{
  val Logger: Logger = LoggerFactory.getLogger( classOf[GetTitle])

  override def extractData(n: Node): Option[String] = if(n.title.isEmpty) {
    Logger.warn("Title attribute is empty")
    None
  } else Some("Issue Title: "+ n.title.get)
}

//extracts the body of an issue
case class GetBody() extends InformationExtraction{
  val Logger: Logger = LoggerFactory.getLogger( classOf[GetBody])

  override def extractData(n: Node): Option[String] = if(n.body.isEmpty) {
    Logger.warn("Body attribute is empty")
    None
  } else Some("Issue Body: "+ n.body.get)
}

//extracts the lock status of an issue
case class GetLockedStatus() extends InformationExtraction{
  val Logger: Logger = LoggerFactory.getLogger( classOf[GetLockedStatus])

  override def extractData(n: Node): Option[String] = if(n.locked.isEmpty) {
    Logger.warn("Locked attribute is empty")
    None
  } else Some("Issue Lock Status: "+ n.locked.get.toString)
}

//extracts the state status of an issue
case class GetStateStatus() extends InformationExtraction{
  val Logger: Logger = LoggerFactory.getLogger( classOf[GetStateStatus])

  override def extractData(n: Node): Option[String] = if(n.state.isEmpty) {
    Logger.warn("State attribute is empty")
    None
  } else Some("Issue State Status: "+ n.state.get)
}

//extracts the author of an issue
case class GetAuthor() extends InformationExtraction{
  val Logger: Logger = LoggerFactory.getLogger( classOf[GetAuthor])

  override def extractData(n: Node): Option[String] = if(n.author.isEmpty) {
    Logger.warn("Author attribute is empty")
    None
  } else Some("Author Name: "+ n.author.get.login + "\nAuthor Url: " + n.author.get.url)
}

//extracts the email of the user
case class GetUsername() extends InformationExtraction{
  val Logger: Logger = LoggerFactory.getLogger( classOf[GetUsername])

  override def extractData(n: Node): Option[String] = if(n.username.isEmpty) {
    Logger.warn("Username attribute is empty")
    None
  } else Some("Username: "+ n.username.get)
}

//extracts the email of the user
case class GetEmail() extends InformationExtraction{
  val Logger: Logger = LoggerFactory.getLogger( classOf[GetEmail])

  override def extractData(n: Node): Option[String] = if(n.email.isEmpty) {
    Logger.warn("Email attribute is empty")
    None
  } else Some("Email: "+ n.email.get)
}

//extracts the follower count of a user
case class GetFollowers() extends InformationExtraction{
  val Logger: Logger = LoggerFactory.getLogger( classOf[GetFollowers])

  override def extractData(n: Node): Option[String] = if(n.followers.isEmpty) {
    Logger.warn("Follower attribute is empty")
    None
  } else Some("Follower Count: "+ n.followers.get.followers)
}

//extracts the following count of a user
case class GetFollowing() extends InformationExtraction{
  val Logger: Logger = LoggerFactory.getLogger( classOf[GetFollowing])

  override def extractData(n: Node): Option[String] = if(n.following.isEmpty) {
    Logger.warn("Following attribute is empty")
    None
  } else Some("Following Count: "+ n.following.get.following)
}

//extracts all the repositories of a user
case class GetRepositories() extends InformationExtraction{
  val Logger: Logger = LoggerFactory.getLogger( classOf[GetRepositories])

  override def extractData(n: Node): Option[String] = {
    if(n.repositories.isEmpty){
      Logger.warn("Repositories attribute is empty")
      None
    } else {
      var str = "List of Repositories: \n"
      for (r <- n.repositories.get.edges) {
        str += "\t" + r.node.name + "\n"
      }
      Some(str)
    }
  }
}

