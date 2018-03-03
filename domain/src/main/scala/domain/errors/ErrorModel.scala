package domain.errors

import domain.responses.IDomainResponse

// ドメイン内のレスポンスのうち、失敗を表すルートモデル
abstract sealed class DomainError(
                            val category: Int,
                            val code: Int) extends IDomainResponse {
  override def equals(other: Any): Boolean = other match {
    case that: DomainError =>
      (that canEqual this) &&
        category == that.category &&
        code == that.code
    case _ => false
  }

  /* domainとcodeの組で等価性を判定する。captionの違いは無視する。 */
  def canEqual(other: Any): Boolean = other match {
    case _: DomainError => true
    case _ => false
  }
}


object ErrorCategory {
  val InterfaceAdapter = 0
  val GeneralApi = 1
  val UserApi = 2
}

case class NotFoundSuchUser(id: String)
  extends DomainError(ErrorCategory.UserApi, 1)

case class CannotCreateUserBecauseSameIdAlreadyExists(id: String)
  extends DomainError(ErrorCategory.UserApi, 2)

