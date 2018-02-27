package interface.presenter

import domain.errors.DomainError
import interface.Types.OutsideHttpRequest
import play.api.i18n.{I18nSupport, Messages}
import play.api.libs.functional.syntax._
import play.api.libs.json._

class ErrorMessageGenerator(messages: Messages) {
  def toMessage(error: domain.errors.DomainError): String = error match {
    case e: interface.JsonParseError =>
      val errorMessage = e.errors.map(e =>
        s"${e._1.toString()} -> ${e._2.map(ve => messages(ve.message)).mkString(",")}"
      ).mkString(",")
      if (errorMessage.isEmpty) {
        messages("cannot.parse.json", messages("invalid.json"))
      } else {
        messages("cannot.parse.json", errorMessage)
      }
    case e: domain.errors.NotFoundSuchUser => messages("not.found.such.user", e.userApiId)
  }
}

trait I18nJsonFormatImplicitsSupport extends I18nSupport {
  val implicitFactory: JsonModelWriteImplicitsFactory

  implicit def request2messageGen(implicit request: OutsideHttpRequest): ErrorMessageGenerator =
    new ErrorMessageGenerator(request2Messages(request))

  implicit def request2jsonImplicits(implicit request: OutsideHttpRequest): JsonModelWriteImplicits =
    implicitFactory.generateWrites(request2Messages(request))
}


class JsonModelWriteImplicitsFactory {
  def generateWrites(messages: Messages) = new JsonModelWriteImplicits(new ErrorMessageGenerator(messages))
}

class JsonModelWriteImplicits(messageGen: ErrorMessageGenerator) {

  private class ErrorWrites[T <: DomainError] extends Writes[T] {
    override def writes(o: T): JsValue = Json.obj(
      "category" -> o.category,
      "code" -> o.code,
      "caption" -> messageGen.toMessage(o)
    )
  }

  implicit val writesNotFoundSuchUser: Writes[domain.errors.NotFoundSuchUser] = new ErrorWrites[domain.errors.NotFoundSuchUser]

  implicit val writesJsonParseError: Writes[interface.JsonParseError] = new ErrorWrites[interface.JsonParseError]

  implicit val writesUser: Writes[domain.User] = Json.writes[domain.User]

  implicit val writesGetUsersResponse: Writes[domain.responses.GetUsersResponse] = Json.writes[domain.responses.GetUsersResponse]

}

class JsonModelReadImplicits {
  implicit val readsGetUsersRequest: Reads[domain.requests.GetUsersRequest] = (
    (__ \ "pageNum").read[Int].orElse(Reads.pure(0)) ~
      (__ \ "pageSize").read[Int].orElse(Reads.pure(0))
    ) (domain.requests.GetUsersRequest.apply _)
}
