package interface.controller

import com.google.inject.Inject
import domain._
import domain.requests._
import interface.presenter.JsonModelReadImplicits
import play.api.libs.streams.Accumulator
import play.api.mvc._

import scala.concurrent.ExecutionContext

trait TeamIdParsable {

  protected def parseTeamID(domain: String): TeamID = {
    if (domain.contains(".")) {
      TeamID(domain.substring(0, domain.indexOf(".")))
    } else {
      TeamID("postgres")
    }
  }

  protected val teamIdParser: BodyParser[TeamID] = BodyParser("teamIdParser") { request =>
    Accumulator.done(Right(parseTeamID(request.domain)))
  }
}

class AppPageIndexPageController @Inject()
(
  implicit val ec: ExecutionContext
) extends TeamIdParsable {

  def parse: BodyParser[(TeamID, IndexPageRequest)] = {
    teamIdParser.map(teamId => {
      (teamId, IndexPageRequest())
    })
  }

}

class GetUserController @Inject()
(
  implicit val ec: ExecutionContext
) extends TeamIdParsable {

  def parse(userId: String)(implicit parser: PlayBodyParsers): BodyParser[(TeamID, GetUserRequest)] = {
    teamIdParser.map(teamId => {
      (teamId, GetUserRequest(userId))
    })
  }
}

class GetUsersController @Inject()
(
  implicit val ec: ExecutionContext
) extends TeamIdParsable {

  def parse(implicit parser: PlayBodyParsers): BodyParser[(TeamID, GetUsersRequest)] = {
    parser.using { request =>
      val pageNum = math.max(1, request.getQueryString("pageNum").map(_.toInt).getOrElse(1))
      val pageSize = request.getQueryString("pageSize").map(_.toInt).getOrElse(10)

      teamIdParser.map(teamId => {
        (teamId, GetUsersRequest(pageNum, pageSize))
      })
    }
  }
}

class CreateUserController @Inject()
(
  implicit val ec: ExecutionContext,
  val readImplicits: JsonModelReadImplicits
) extends TeamIdParsable {

  import readImplicits._

  def parse(implicit parser: PlayBodyParsers): BodyParser[(TeamID, CreateUserRequest)] = {
    parser.using { request =>
      parser.json[CreateUserRequest].map { req =>
        (parseTeamID(request.domain), req)
      }
    }
  }
}

class DeleteUserController @Inject()
(
  implicit val ec: ExecutionContext
) extends TeamIdParsable {

  def parse(userId: String)(implicit parser: PlayBodyParsers): BodyParser[(TeamID, DeleteUserRequest)] = {
    teamIdParser.map(teamId => {
      (teamId, DeleteUserRequest(userId))
    })
  }
}

class GetAndDeleteUserController @Inject()
(
  implicit val ec: ExecutionContext
) extends TeamIdParsable {

  def parse(userId: String)(implicit parser: PlayBodyParsers): BodyParser[(TeamID, GetUserRequest, DeleteUserRequest)] = {
    teamIdParser.map(teamId => {
      (teamId, GetUserRequest(userId), DeleteUserRequest(userId))
    })
  }
}
