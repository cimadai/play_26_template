package domain.responses

import domain.IDomainModel

// ドメイン内のレスポンスを表すルートモデル
trait IDomainResponse extends IDomainModel

// ドメイン内のレスポンスのうち、成功を表すルートモデル
trait IDomainSuccess extends IDomainResponse

// ページレスポンス
case class Done() extends IDomainSuccess

sealed trait IAPIResponse

// APIレスポンス
case class GetUserResponse(user: domain.User) extends IAPIResponse
case class GetUsersResponse(users: Iterable[domain.User]) extends IAPIResponse
case class CreateUserResponse(created: Int) extends IAPIResponse
case class DeleteUserResponse(deleted: Int) extends IAPIResponse

