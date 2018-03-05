package interface.controller

import com.google.inject.Inject
import domain.requests.GetUsersRequest
import play.api.Configuration
import play.api.mvc.{BodyParser, PlayBodyParsers}

import scala.concurrent.ExecutionContext

class GetUsersController @Inject()
(
  implicit val ec: ExecutionContext,
  implicit val config: Configuration
) extends BodyParserBase {

  def parse(implicit parser: PlayBodyParsers): BodyParser[GetUsersRequest] =
    httpRequestParser.map { request =>
      val pageNum = math.max(1, request.getQueryString("pageNum").map(_.toInt).getOrElse(1))
      val pageSize = request.getQueryString("pageSize").map(_.toInt).getOrElse(10)
      GetUsersRequest(parseTeamID(request.domain), pageNum, pageSize)
    }
}

