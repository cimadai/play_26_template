package domain.requests

import domain.{IDomainModel, TeamID, User}

// ドメイン内のリクエストを表すルートモデル
trait IDomainRequest extends IDomainModel

// ページリクエスト
case class IndexPageRequest(teamId: TeamID) extends IDomainRequest

case class TestSendMessagePageRequest(sendTo: Int, userId: String, messageType: Int, title: String, body: String) extends IDomainRequest

// APIリクエスト
case class GetUserRequest(teamId: TeamID, id: String) extends IDomainRequest
case class GetUsersRequest(teamId: TeamID, pageNum: Int, pageSize: Int) extends IDomainRequest
case class CreateUserRequest(teamId: TeamID, user: User) extends IDomainRequest
case class DeleteUserRequest(teamId: TeamID, id: String) extends IDomainRequest

case class UserLoginRequest() extends IDomainRequest

