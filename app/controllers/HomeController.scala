package controllers

import javax.inject._
import play.api._
import play.api.mvc._

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def homePage() = Action { implicit request: Request[AnyContent] =>
    var loggedIn:Boolean=false
    request.session.get("username")
      .map{username=>
        loggedIn=true
      }
      .getOrElse{
        loggedIn=false
      }
    val posts:Seq[models.Post]= models.PostsModel.getAllPosts()
    Ok(views.html.homepage(posts,loggedIn))
  }


  def sortByDate() = Action { implicit request: Request[AnyContent] =>
    models.PostsModel.sortByDate()
    Redirect(routes.HomeController.homePage())
  }

  def sortByVotes() = Action { implicit request: Request[AnyContent] =>
    models.PostsModel.sortByVotes()
    Redirect(routes.HomeController.homePage())
  }
  
}
