package usecase

import com.google.inject.Inject

import scala.concurrent.{ExecutionContext, Future}

class AppPageGetUsersUseCase @Inject()
(
  implicit val ec: ExecutionContext,
  usersRepository: domain.repositories.IUserRepository
) {

  def getUsers(req: domain.requests.GetUsersRequest): Future[domain.responses.GetUsersResponse] = {

    usersRepository.listUsers(req.pageNum, req.pageSize)
      .map(domain.responses.GetUsersResponse)

  }
}
