package interface.controller

import com.google.inject.Inject
import domain.requests._
import play.api.mvc._

import scala.concurrent.ExecutionContext

class DeleteUserController @Inject()
(
  implicit val ec: ExecutionContext
) extends BodyParserBase {

  def parse(userId: String)(implicit parser: PlayBodyParsers): BodyParser[DeleteUserRequest] =
    httpRequestParser.map { request =>
      DeleteUserRequest(parseTeamID(request.domain), userId)
    }
}


