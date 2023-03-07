package models

import java.time.LocalDate


case class Post(title:String, id:Long, question: String, code:String, votes:Int, date: String,var comments: Seq[String],var answers:Seq[Answer])
case class Answer(text:String,code:String)
object PostsModel {

  val commentsList:Seq[String]=Seq("comment 1","comment 2","comment 3")
  val answersList:Seq[Answer]=Seq(Answer("answer 1","answer 1 code"),Answer("answer 2","answer 2 code"),Answer("answer 2","answer 2 code"))

  var sharedPosts: Seq[Post] = Seq(Post("post 1", generateId("post 1"), "question 1", "code 1", 0, java.time.LocalDate.now().toString,commentsList,answersList),
    Post("post 1", generateId("post 1"), "question 2", "code 2", 0, java.time.LocalDate.now().toString,commentsList,answersList),
    Post("post 1", generateId("post 1") ,"question 3", "code 3", 0, java.time.LocalDate.now().toString,commentsList,answersList))

  def getAllPosts():Seq[Post]={
    sharedPosts
  }

  def addPost(title:String, question:String,code:String):Unit ={
    val id=generateId(title)
    val newPost = Post(title, id, question, code, 0, java.time.LocalDate.now().toString,commentsList,answersList)
    sharedPosts = sharedPosts :+ newPost
  }

  def incrementVotes(index:Int):Unit={
    val incrementedVotes = sharedPosts(index).votes + 1
    val updatedPost = sharedPosts(index).copy(votes=incrementedVotes)
    sharedPosts = sharedPosts.updated(index,updatedPost)
  }

  def decrementVotes(index:Int):Unit={
    var incrementedVotes = sharedPosts(index).votes - 1
    if(incrementedVotes < 0) incrementedVotes = 0
    val updatedPost = sharedPosts(index).copy(votes=incrementedVotes)
    sharedPosts = sharedPosts.updated(index,updatedPost)
  }

  def generateId(title:String):Long={
    val idByteSeq=title.map(_.toByte)
    val shuffledByteSeq=scala.util.Random.shuffle(idByteSeq)
    val idString = (shuffledByteSeq.map(_.toString)).mkString("")
    idString.slice(0,10).toLong
  }

  def sortByDateHelper(post1: Post, post2: Post) = {
    val date1 =  LocalDate.parse(post1.date)
    val date2 =LocalDate.parse(post2.date)
    date2.isAfter(date1)
  }

  def sortByVotes():Unit = {
    sharedPosts=sharedPosts.sortWith(_.votes>_.votes)
  }

  def sortByDate():Unit={
    sharedPosts=sharedPosts.sortWith(sortByDateHelper)
  }

  def getPostIndexById(id: Long):Int ={
    var index=0
    for((post,i) <- sharedPosts.zipWithIndex){
      if(post.id==id) index=i
    }
    index
  }
  def getPostById(id: Long):Post ={
    val index:Int=getPostIndexById(id)
    sharedPosts(index)
  }

  def addAnswerToPost(id: Long,answerText:String,answerCode:String):Unit={
    val postIndex:Int=getPostIndexById(id)
    val answer:Answer=Answer(answerText,answerCode)
    var answersList: Seq[Answer] = sharedPosts(postIndex).answers
    answersList=answersList :+ answer
    val updatedPost = sharedPosts(postIndex).copy(answers=answersList)
    sharedPosts = sharedPosts.updated(postIndex,updatedPost)
  }

  def addCommentToPost(id: Long,comment:String):Unit={
    val postIndex:Int=getPostIndexById(id)
    var commentsList: Seq[String] = sharedPosts(postIndex).comments
    commentsList=commentsList :+ comment
    val updatedPost = sharedPosts(postIndex).copy(comments=commentsList)
    sharedPosts = sharedPosts.updated(postIndex,updatedPost)
  }


}
