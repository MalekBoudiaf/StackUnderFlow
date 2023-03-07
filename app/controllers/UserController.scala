package controllers

import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.data.validation.Constraints._
import javax.inject._

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */

case class UserData(username: String, password: String)
case class UserDataSignup(username: String, password1: String, password2: String)

@Singleton
class UserController @Inject()(cc: MessagesControllerComponents) extends MessagesAbstractController(cc) {

  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */

  val userLoginForm = Form(
    mapping(
      "username" -> nonEmptyText(8,40),
      "password"  -> nonEmptyText(8,40)
    )(UserData.apply)(UserData.unapply)
  )

  val userSignUpForm = Form(
    mapping(
      "username" -> nonEmptyText(8,40),
      "password1"  -> nonEmptyText(8,40),
      "password2"  -> nonEmptyText(8,40),

    )(UserDataSignup.apply)(UserDataSignup.unapply)
  )


  def validateUserLoginForm = Action {implicit request =>

    val errorFunction = { formWithErrors: Form[UserData] =>
      // form validation/binding failed...
      BadRequest(views.html.login(formWithErrors))
    }
    val successFunction = { userData: UserData =>
      // form validation/binding succeeded ...
      /* binding success, you get the actual value. */
      val username = userData.username
      val password = userData.password
      if(models.UserModel.validateUser(username, password)){
        Redirect(routes.HomeController.homePage()).withSession("username"->username)
      }else{
        Redirect(routes.UserController.login()).flashing("wronglogin"->"Wrong username and/or password... please try again")
      }
    }
    val formValidationResult: Form[UserData] = userLoginForm.bindFromRequest()
    formValidationResult.fold(
      errorFunction,
      successFunction
    )
  }

  def validateUserSignUpForm = Action {implicit request =>

    val errorFunction = { formWithErrors: Form[UserDataSignup] =>
      // form validation/binding failed...
      BadRequest(views.html.signup(formWithErrors))
    }
    val successFunction = { userData: UserDataSignup =>
      // form validation/binding succeeded ...
      /* binding success, you get the actual value. */
      val username = userData.username
      val password1 = userData.password1
      val password2 = userData.password2
      if(!password1.equals(password2)){
        Redirect(routes.UserController.signup()).flashing("nomatch"->"Passwords don't match... try again")
      }
      else if(models.UserModel.addUser(username, password1)){
        Redirect(routes.UserController.login()).flashing("successSignup"->"Please login using your user name and password")
      }else{
        Redirect(routes.UserController.signup()).flashing("userExists"->"User already exists...")
      }
    }
    val formValidationResult: Form[UserDataSignup] = userSignUpForm.bindFromRequest()
    formValidationResult.fold(
      errorFunction,
      successFunction
    )
  }

  def logout = Action {implicit request =>
    Redirect(routes.HomeController.homePage()).withNewSession

  }

  def login() = Action { implicit request =>
    Ok(views.html.login(userLoginForm))
  }

  def signup() = Action { implicit request =>
    Ok(views.html.signup(userSignUpForm))
  }

  
}
