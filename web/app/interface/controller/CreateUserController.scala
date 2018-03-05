package interface.controller

import com.google.inject.Inject
import domain._
import domain.requests._
import interface.JsonParseError
import interface.presenter.{InterfaceErrorJsonPresenter, JsonModelReadImplicits}
import play.api.Configuration
import play.api.mvc._

import scala.concurrent.ExecutionContext

class CreateUserController @Inject()
(
  implicit val ec: ExecutionContext,
  implicit val config: Configuration,
  val readImplicits: JsonModelReadImplicits,
  jsonErrorPresenter: InterfaceErrorJsonPresenter
) extends BodyParserBase{

  import readImplicits._

  def parse(implicit parser: PlayBodyParsers): BodyParser[CreateUserRequest] =
    BodyParser("") {
      parser.using { implicit request =>
        parser.json.map { json =>
          (json \ "user").validate[User].fold(
            // JsonのパースエラーはPlayFrameworkの都合上どうしてもここで返さないといけない
            err => Left(jsonErrorPresenter.present(JsonParseError(err.toMap))),
            user => Right(CreateUserRequest(parseTeamID(request.domain), user))
          )
        }
      }.andThen(a => a.map(b => b.fold(
        left => Left(left),
        {
          case Right(req) => Right(req)
          case Left(err) => Left(err)
        }
      )))
    }
}

