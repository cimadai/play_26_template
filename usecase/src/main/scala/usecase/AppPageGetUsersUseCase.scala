package usecase

import com.google.inject.Inject
import domain.TeamID
import domain.repositories.ITeamRepository
import domain.requests.GetUsersRequest

import scala.concurrent.{ExecutionContext, Future}

class AppPageGetUsersUseCase @Inject()
(
  implicit val ec: ExecutionContext,
  teamRepository: ITeamRepository
) {

  def getUsers(teamId: TeamID, req: GetUsersRequest)
  : Future[domain.responses.GetUsersResponse] = {

    teamRepository
      .getUserRepository(teamId)
      .listUsers(req.pageNum, req.pageSize)
      .map(domain.responses.GetUsersResponse)

  }
}
