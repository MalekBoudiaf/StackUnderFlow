package models
import collection.mutable
object UserModel {
  private val usersDB = mutable.Map[String,String]("malek.boudiaf"->"malekboudiaf1234")

  def validateUser(username: String, password:String):Boolean={
    usersDB.get(username).map(_ == password).getOrElse(false)
  }

  def addUser(username: String, password:String):Boolean={
    if(usersDB.contains(username)) {false }
    else{
      usersDB(username) = password
      true
    }
  }
}
