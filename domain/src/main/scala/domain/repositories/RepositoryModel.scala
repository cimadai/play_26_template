package domain.repositories

import domain.TeamID
import domain.requests.{CreateUserRequest, DeleteUserRequest, GetUserRequest, GetUsersRequest}

import scala.concurrent.Future

trait ITeamRepository {
  def getUserRepository(teamId: TeamID): IUserRepository
}

trait IUserRepository {
  def getUser(request: GetUserRequest): Future[Option[domain.User]]
  def listUsers(request: GetUsersRequest): Future[Iterable[domain.User]]
  def createUser(request: CreateUserRequest): Future[Int]
  def deleteUser(request: DeleteUserRequest): Future[Int]
}

