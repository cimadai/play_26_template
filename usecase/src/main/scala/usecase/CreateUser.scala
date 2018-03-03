package usecase

import com.google.inject.Inject
import domain.TeamID
import domain.errors.CannotCreateUserBecauseSameIdAlreadyExists
import domain.repositories.ITeamRepository
import domain.requests.{CreateUserRequest, GetUserRequest}
import domain.responses.CreateUserResponse

import scala.concurrent.{ExecutionContext, Future}

class CreateUser @Inject()
(
  implicit val ec: ExecutionContext,
  teamRepository: ITeamRepository
) {

  def createUser(teamId: TeamID, req: CreateUserRequest)
  : Future[Either[CannotCreateUserBecauseSameIdAlreadyExists, CreateUserResponse]] = {

    val userRepo = teamRepository.getUserRepository(teamId)

    userRepo
      .getUser(GetUserRequest(req.user.id))
      .flatMap {
        case Some(_) =>
          Future.successful(
            Left(CannotCreateUserBecauseSameIdAlreadyExists(req.user.id))
          )
        case None =>
          userRepo
            .createUser(req)
            .map(ret =>
              Right(domain.responses.CreateUserResponse(ret))
            )
      }

  }
}
