package domain.responses

import domain.IDomainModel

// ドメイン内のレスポンスを表すルートモデル
trait IDomainResponse extends IDomainModel

// ドメイン内のレスポンスのうち、成功を表すルートモデル
trait IDomainSuccess extends IDomainResponse

// ページレスポンス
case class Done()

// APIレスポンス
case class GetUsersResponse(users: Iterable[domain.User]) extends IDomainSuccess

