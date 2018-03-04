package interface.controller

import com.google.inject.Inject
import domain.requests._
import play.api.mvc._

import scala.concurrent.ExecutionContext

class AppPageIndexPageController @Inject()
(
  implicit val ec: ExecutionContext
) extends BodyParserBase {

  def parse: BodyParser[IndexPageRequest] =
    httpRequestParser.map { request =>
      IndexPageRequest(parseTeamID(request.domain))
    }

}
