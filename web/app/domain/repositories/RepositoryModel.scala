package domain.repositories

import scala.concurrent.Future

trait IUserRepository {
  def listUsers(pageNum: Int, pageSize: Int): Future[Iterable[domain.User]]
}

