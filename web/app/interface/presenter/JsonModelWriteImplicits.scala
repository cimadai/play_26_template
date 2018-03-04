package interface.presenter

import domain._
import domain.errors.{CannotCreateUserBecauseSameIdAlreadyExists, NotFoundSuchUser}
import domain.responses.{CreateUserResponse, DeleteUserResponse, GetUserResponse, GetUsersResponse}
import interface.{HttpErrorResponse, InterfaceError, InvalidJsonFormat, JsonParseError}
import play.api.i18n.{I18nSupport, Messages}
import play.api.libs.functional.syntax._
import play.api.libs.json._
import play.api.mvc.RequestHeader

class ErrorMessageGenerator(messages: Messages) {
  def toHttpErrorResponse(error: InterfaceError): HttpErrorResponse = error match {
    case InvalidJsonFormat() =>
      HttpErrorResponse(400, error.category, error.code, messages("invalid.json"))
    case JsonParseError(errors) =>
      val errorMessage = errors.map(e =>
        s"${e._1.toString()} -> ${e._2.map(ve => messages(ve.message)).mkString(",")}"
      ).mkString(",")
      HttpErrorResponse(400, error.category, error.code, messages("cannot.parse.json", errorMessage))
  }

  def toHttpErrorResponse(error: errors.DomainError): HttpErrorResponse = error match {
    case e: NotFoundSuchUser =>
      HttpErrorResponse(404, error.category, error.code, messages("not.found.such.user", e.id))
    case e: CannotCreateUserBecauseSameIdAlreadyExists =>
      HttpErrorResponse(409, error.category, error.code, messages("cannot.create.user.because.same.id.already.exists", e.id))

  }
}

trait I18nJsonFormatImplicitsSupport extends I18nSupport {
  val implicitFactory: JsonModelWriteImplicitsFactory

  implicit def request2messageGen(implicit request: RequestHeader): ErrorMessageGenerator =
    new ErrorMessageGenerator(request2Messages(request))

  implicit def request2jsonImplicits(implicit request: RequestHeader): JsonModelWriteImplicits =
    implicitFactory.generateWrites(request2Messages(request))
}


class JsonModelWriteImplicitsFactory {
  def generateWrites(messages: Messages) = new JsonModelWriteImplicits(new ErrorMessageGenerator(messages))
}

class JsonModelWriteImplicits(val messageGen: ErrorMessageGenerator) {

  implicit val writesHttpErrorResponse: Writes[HttpErrorResponse] = new Writes[HttpErrorResponse] {
    override def writes(o: HttpErrorResponse): JsValue = Json.obj(
      "category" -> o.category,
      "code" -> o.code,
      "caption" -> o.caption
    )
  }

  implicit val writesUser: Writes[User] =
    Json.writes[User]

  implicit val writesGetUserResponse: Writes[GetUserResponse] =
    Json.writes[GetUserResponse]

  implicit val writesGetUsersResponse: Writes[GetUsersResponse] =
    Json.writes[GetUsersResponse]

  implicit val writesCreateUserResponse: Writes[CreateUserResponse] =
    Json.writes[CreateUserResponse]

  implicit val writesDeleteUserResponse: Writes[DeleteUserResponse] =
    Json.writes[DeleteUserResponse]

}

class JsonModelReadImplicits {

  //case class User(id: String, name: String, age: Int)
  implicit val readsUser: Reads[User] = (
    (__ \ "id").read[String] ~
      (__ \ "name").read[String] ~
      (__ \ "age").read[Int]
    ) (User.apply _)

}
