#CS474 Final Project
##By: Mohamed Saeed and Maxwell Dausch

###Description
This project is a functional GraphQL framework for retrieving data from the github api.

###Installation
In order to install this program, please clone this repo:

```
git clone https://MohamedSaeed99@bitbucket.org/mdausc2/maxwell_dausch_project.git
```



###Limitations
This program takes String literals as parameters rather than types, this could result in 
incorrect input from the user, which might end up crashing the program or retrieving the wrong data.

###Examples
There are 3 different types of queries present in this framework:

######Repository Type:
This type of query retrieves repository information from the Github API.

This type of query can be instantiated by having:

```(new QueryCommand()).setRepoQuery().build```


######User Type:
This type of query retrieves user information from the Github API.

This type of query can be instantiated by having:

```(new QueryCommand()).setUserQuery().build```

######Issue Type:
This type of query retrieves issue information from the Github API.

This type of query can be instantiated by having:

```(new QueryCommand()).setIssueQuery("test").build```
