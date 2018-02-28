package domain.repositories

import domain.TeamID

import scala.concurrent.Future

trait ITeamRepository {
  def getUserRepository(teamId: TeamID): IUserRepository
}

trait IUserRepository {
  def listUsers(pageNum: Int, pageSize: Int): Future[Iterable[domain.User]]
}

