package interface

import play.api.libs.json.{JsPath, JsonValidationError}
import play.api.mvc.AnyContent

object Types {

  // 外界(HTTP)からPlay frameworkに入ってくるリクエスト
  type OutsideHttpRequest = play.api.mvc.Request[AnyContent]

  // 外界(HTTP)に対してPlay frameworkから返却するレスポンス
  type OutsideHttpResponse = play.api.mvc.Result

}

// interface層での失敗を表すルートモデル
abstract class InterfaceError(status: Int, category: Int, code: Int)
  extends domain.errors.DomainError(status, category, code)

// OutsideRequestのJsonParseに失敗した時のエラー
case class JsonParseError(errors: Map[JsPath, Seq[JsonValidationError]]) extends InterfaceError(400, domain.errors.ErrorCategory.GeneralApi, 1)

