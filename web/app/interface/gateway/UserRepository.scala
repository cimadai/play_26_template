package interface.gateway

import com.google.inject.Inject
import domain.TeamID
import domain.repositories.{ITeamRepository, IUserRepository}
import domain.requests.{CreateUserRequest, DeleteUserRequest, GetUserRequest, GetUsersRequest}
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

  def getUser(request: GetUserRequest): Future[Option[domain.User]] = {
    db.run(
      userQuery.filter(_.id === request.id).take(1).result.headOption
    )
  }

  /**
    * ページとページサイズを指定してユーザー一覧を取得
    * @param request ユーザー取得リクエスト
    * @return ユーザー一覧
    */
  def listUsers(request: GetUsersRequest): Future[Iterable[domain.User]] = {
    db.run(
      userQuery.drop((request.pageNum - 1) * request.pageSize).take(request.pageSize).result
    )
  }

  /**
    * 指定されたユーザーを作成
    * @param request ユーザー作成リクエスト
    * @return 作成したレコード数
    */
  def createUser(request: CreateUserRequest): Future[Int] = {
    db.run(
      userQuery += request.user
    )
  }

  /**
    * 指定されたユーザーIDを削除
    * @param request ユーザー削除リクエスト
    * @return 削除したレコード数
    */
  def deleteUser(request: DeleteUserRequest): Future[Int] = {
    db.run(
      userQuery.filter(_.id === request.id).delete
    )
  }

}
