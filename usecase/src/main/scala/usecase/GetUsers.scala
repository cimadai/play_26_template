package usecase

import com.google.inject.Inject
import domain.TeamID
import domain.repositories.ITeamRepository
import domain.requests.GetUsersRequest

import scala.concurrent.{ExecutionContext, Future}

class GetUsers @Inject()
(
  implicit val ec: ExecutionContext,
  teamRepository: ITeamRepository
) {

  def getUsers(req: GetUsersRequest)
  : Future[domain.responses.GetUsersResponse] = {

    teamRepository
      .getUserRepository(req.teamId)
      .listUsers(req)
      .map(domain.responses.GetUsersResponse)

  }
}
