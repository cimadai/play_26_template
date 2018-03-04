package interface.controller

import domain.TeamID
import play.api.libs.streams.Accumulator
import play.api.mvc.{BodyParser, RequestHeader}

trait BodyParserBase {

  protected def parseTeamID(domain: String): TeamID = {
    if (domain.contains(".")) {
      TeamID(domain.substring(0, domain.indexOf(".")))
    } else {
      TeamID("postgres")
    }
  }

  protected val httpRequestParser: BodyParser[RequestHeader] = BodyParser("RequestHeader") { request =>
    Accumulator.done(Right(request))
  }
}

