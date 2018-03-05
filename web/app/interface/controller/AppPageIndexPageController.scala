package interface.controller

import com.google.inject.Inject
import domain.requests._
import play.api.Configuration
import play.api.mvc._

import scala.concurrent.ExecutionContext

class AppPageIndexPageController @Inject()
(
  implicit val ec: ExecutionContext,
  implicit val config: Configuration
) extends BodyParserBase {

  def parse: BodyParser[IndexPageRequest] =
    httpRequestParser.map { request =>
      IndexPageRequest(parseTeamID(request.domain))
    }

}
