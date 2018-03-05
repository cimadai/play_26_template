package interface.controller

import domain.TeamID
import play.api.Configuration
import play.api.libs.streams.Accumulator
import play.api.mvc.{BodyParser, RequestHeader}

trait BodyParserBase {

  protected def parseTeamID(domain: String)(implicit config: Configuration): TeamID = {
    config.getOptional[String]("database.database") match {
      case Some(database) if database != "postgres" =>
        TeamID(database)
      case _ =>
        if (domain.contains(".")) {
          TeamID(domain.substring(0, domain.indexOf(".")))
        } else {
          TeamID("postgres")
        }
    }
  }

  protected val httpRequestParser: BodyParser[RequestHeader] = BodyParser("RequestHeader") { request =>
    Accumulator.done(Right(request))
  }
}

