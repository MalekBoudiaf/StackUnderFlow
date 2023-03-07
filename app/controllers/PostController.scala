package controllers

import play.api.mvc._
import javax.inject._


/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class PostController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */


  def addPost() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.addpostform())
  }

  def validatePost = Action {implicit request =>
    val headerValues = request.body.asFormUrlEncoded
    headerValues.map{values =>
      val title = values("title").head
      val question = values("question").head
      val code = values("code").head
      if(true){
        models.PostsModel.addPost(title, question, code)
        Redirect(routes.HomeController.homePage())
      }else{
        Redirect(routes.UserController.login()).flashing("wronglogin"->"Wrong username and/or password... please try again")
      }
    }.getOrElse(Redirect(routes.UserController.login()))
  }

  def upVote() = Action { implicit request: Request[AnyContent] =>
    request.session.get("username")
      .map{username=>
        val headerValues = request.body.asFormUrlEncoded
        headerValues.map{values =>
          val index = values("upvoteIndex").head
          models.PostsModel.incrementVotes(index.toInt)
        }.getOrElse(Redirect(routes.HomeController.homePage()))
        Redirect(routes.HomeController.homePage())
      }
      .getOrElse{
        Redirect(routes.UserController.login())
      }

  }

  def downVote() = Action { implicit request: Request[AnyContent] =>
    request.session.get("username")
      .map{username=>
        val headerValues = request.body.asFormUrlEncoded
        headerValues.map{values =>
          val index = values("downvoteIndex").head
          models.PostsModel.decrementVotes(index.toInt)
        }.getOrElse(Redirect(routes.HomeController.homePage()))
        Redirect(routes.HomeController.homePage())
      }
      .getOrElse{
        Redirect(routes.UserController.login())
      }

  }

  def postDetails(id: Long) = Action { implicit request: Request[AnyContent] =>
    val postToShow=models.PostsModel.getPostById(id)
    Ok(views.html.postdetails(postToShow))
  }

  def addAnswer(id: Long) = Action { implicit request: Request[AnyContent] =>
    request.session.get("username")
      .map{username=>
        Ok(views.html.addanswerform(id))
      }
      .getOrElse{
        Redirect(routes.UserController.login())
      }
  }

  def validateAnswer() = Action {implicit request: Request[AnyContent] =>
    request.session.get("username")
      .map{username=>
        val headerValues = request.body.asFormUrlEncoded
        headerValues.map{values =>
          val answerText = values("textAnswer").head
          val answerCode = values("codeAnswer").head
          val postId = values("id").head
          models.PostsModel.addAnswerToPost(postId.toInt,answerText,answerCode)
          Redirect(routes.PostController.postDetails(postId.toLong))
        }.getOrElse(Redirect(routes.HomeController.homePage()))
      }
      .getOrElse{
        Redirect(routes.UserController.login())
      }
  }

  def validateComment() = Action {implicit request: Request[AnyContent] =>
    request.session.get("username")
      .map{username=>
        val headerValues = request.body.asFormUrlEncoded
        headerValues.map{values =>
          val comment = values("comment").head
          val postId = values("id").head
          models.PostsModel.addCommentToPost(postId.toLong,comment)
          Redirect(routes.PostController.postDetails(postId.toLong))
        }.getOrElse(Redirect(routes.HomeController.homePage()))
      }
      .getOrElse{
        Redirect(routes.UserController.login())
      }
  }




  
}
