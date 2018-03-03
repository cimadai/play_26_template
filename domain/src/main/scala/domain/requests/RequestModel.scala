package domain.requests

import domain.{IDomainModel, User}

// ドメイン内のリクエストを表すルートモデル
trait IDomainRequest extends IDomainModel

// ページリクエスト
case class IndexPageRequest() extends IDomainRequest

case class TestSendMessagePageRequest(sendTo: Int, userId: String, messageType: Int, title: String, body: String) extends IDomainRequest

// APIリクエスト
case class GetUserRequest(id: String) extends IDomainRequest
case class GetUsersRequest(pageNum: Int, pageSize: Int) extends IDomainRequest
case class CreateUserRequest(user: User) extends IDomainRequest
case class DeleteUserRequest(id: String) extends IDomainRequest

case class UserLoginRequest() extends IDomainRequest

