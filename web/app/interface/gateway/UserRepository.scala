package interface.gateway

import scala.concurrent.Future

class UserRepository extends domain.repositories.IUserRepository {

  def listUsers(pageNum: Int, pageSize: Int): Future[Iterable[domain.User]] = {
    // TODO: 実装する
    Future.successful(Iterable(domain.User("test")))
  }

}
