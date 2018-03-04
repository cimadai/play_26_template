package usecase

import com.google.inject.Inject
import domain.TeamID
import domain.errors.NotFoundSuchUser
import domain.repositories.ITeamRepository
import domain.requests.GetUserRequest
import domain.responses.GetUserResponse

import scala.concurrent.{ExecutionContext, Future}

class GetUser @Inject()
(
  implicit val ec: ExecutionContext,
  teamRepository: ITeamRepository
) {

  def getUser(req: GetUserRequest)
  : Future[Either[NotFoundSuchUser, GetUserResponse]] = {

    teamRepository
      .getUserRepository(req.teamId)
      .getUser(req)
      .map {
        case Some(user) => Right(GetUserResponse(user))
        case None => Left(NotFoundSuchUser(req.id))
      }
  }
}
