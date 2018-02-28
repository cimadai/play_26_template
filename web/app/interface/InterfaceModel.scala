package interface

import play.api.libs.json.{JsPath, JsonValidationError}

// interface層での失敗を表すルートモデル
abstract class InterfaceError(status: Int, category: Int, code: Int)
  extends domain.errors.DomainError(status, category, code)

// OutsideRequestのJsonParseに失敗した時のエラー
case class JsonParseError(errors: Map[JsPath, Seq[JsonValidationError]]) extends InterfaceError(400, domain.errors.ErrorCategory.GeneralApi, 1)

