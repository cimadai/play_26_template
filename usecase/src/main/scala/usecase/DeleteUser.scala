package usecase

import com.google.inject.Inject
import domain.TeamID
import domain.repositories.ITeamRepository
import domain.requests.DeleteUserRequest

import scala.concurrent.{ExecutionContext, Future}

class DeleteUser @Inject()
(
  implicit val ec: ExecutionContext,
  teamRepository: ITeamRepository
) {

  def deleteUser(req: DeleteUserRequest)
  : Future[domain.responses.DeleteUserResponse] = {

    teamRepository
      .getUserRepository(req.teamId)
      .deleteUser(req)
      .map(domain.responses.DeleteUserResponse)

  }
}
