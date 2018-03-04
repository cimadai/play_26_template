package interface.controller

import com.google.inject.Inject
import domain.requests.GetUserRequest
import play.api.mvc.{BodyParser, PlayBodyParsers}

import scala.concurrent.ExecutionContext

class GetUserController @Inject()
(
  implicit val ec: ExecutionContext
) extends BodyParserBase {

  def parse(userId: String)(implicit parser: PlayBodyParsers): BodyParser[GetUserRequest] =
    httpRequestParser.map { request =>
      GetUserRequest(parseTeamID(request.domain), userId)
    }
}

