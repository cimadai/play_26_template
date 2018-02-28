package interface.gateway

import com.google.inject.Inject
import domain.TeamID
import domain.repositories.{ITeamRepository, IUserRepository}
import framework.di.DatabaseFactory
import interface.gateway.DatabaseProfile.profile.api._
import interface.gateway.dao.UserTable
import slick.lifted.TableQuery

import scala.concurrent.Future

class DatabaseTeamRepository @Inject()
(
  dbFactory: DatabaseFactory
) extends ITeamRepository {

  def getUserRepository(teamId: TeamID): IUserRepository =
    new UserRepository(dbFactory.getDatabase(teamId))

}

class UserRepository (db: Database)
  extends domain.repositories.IUserRepository {
  private val userQuery = TableQuery[UserTable]

  def listUsers(pageNum: Int, pageSize: Int): Future[Iterable[domain.User]] = {
    db.run(
      userQuery.drop((pageNum - 1) * pageSize).take(pageSize).result
    )
  }

}
