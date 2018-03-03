package interface

import play.api.libs.json.{JsPath, JsonValidationError}

case class HttpErrorResponse(status: Int, category: Int, code: Int, caption: String)

// interface層での失敗を表すルートモデル
sealed abstract class InterfaceError(val category: Int, val code: Int)

// OutsideRequestのJsonParseに失敗した時のエラー
case class InvalidJsonFormat()
  extends InterfaceError(domain.errors.ErrorCategory.GeneralApi, 1)
case class JsonParseError(errors: Map[JsPath, Seq[JsonValidationError]])
  extends InterfaceError(domain.errors.ErrorCategory.GeneralApi, 2)

