package interface.presenter

import domain.errors.DomainError
import domain.responses._
import interface.{HttpErrorResponse, InterfaceError}
import org.slf4j.LoggerFactory
import play.api.libs.json.{JsNumber, JsObject, Json}
import play.api.mvc.{RequestHeader, Result, Results}

object JsonResponsible {

  object ThreadUtils {
    val OK_STACK_DEPTH: Int = 4
    val ERROR_STACK_DEPTH: Int = 6

    def calledFrom(stackDepth: Int): String = {
      val steArray = Thread.currentThread().getStackTrace
      if (steArray.size <= stackDepth) {
        ""
      } else {
        val ste = steArray(stackDepth)
        s"(${ste.getFileName}:${ste.getLineNumber})"
      }
    }
  }

}

/**
  * Json返却用トレイトクラス
  */
trait JsonResponsible extends Results {

  import JsonResponsible._

  private val accessLogger = LoggerFactory.getLogger("access")

  protected def renderJsonOk()(implicit request: RequestHeader): Result = {
    accessLogger.debug(
      s"message:Ajax OK\t" +
        s"status:[200]\t" +
        s"method:[${request.method}]\t" +
        s"uri:[${request.uri.replaceAll("\\?.*", "")}]\t" +
        s"code:[${ThreadUtils.calledFrom(ThreadUtils.OK_STACK_DEPTH)}]"
    )
    Ok(Json.obj("ret_code" -> 0))
  }

  protected def renderJsonOk(obj: domain.responses.IAPIResponse)(implicit request: RequestHeader, jsonModelFormatImplicits: JsonModelWriteImplicits): Result = {
    Ok(toJsObject(obj))
  }

  protected def renderJsonOk(jsObject: JsObject)(implicit request: RequestHeader): Result = {
    accessLogger.debug(
      s"message:Ajax OK\t" +
        s"status:[200]\t" +
        s"method:[${request.method}]\t" +
        s"uri:[${request.uri.replaceAll("\\?.*", "")}]\t" +
        s"code:[${ThreadUtils.calledFrom(ThreadUtils.OK_STACK_DEPTH)}]"
    )
    Ok(jsObject + ("ret_code" -> JsNumber(0)))
  }

  private def toJsObject(obj: domain.responses.IAPIResponse)(implicit jsonModelFormatImplicits: JsonModelWriteImplicits): JsObject = {
    import jsonModelFormatImplicits._
    obj match {
      case o: GetUserResponse => Json.toJson(o).as[JsObject]
      case o: GetUsersResponse => Json.toJson(o).as[JsObject]
      case o: CreateUserResponse => Json.toJson(o).as[JsObject]
      case o: DeleteUserResponse => Json.toJson(o).as[JsObject]
    }
  }

  protected def renderJsonError(obj: DomainError)(implicit request: RequestHeader, jsonModelFormatImplicits: JsonModelWriteImplicits): Result = {
    renderJsonError(jsonModelFormatImplicits.messageGen.toHttpErrorResponse(obj))
  }

  protected def renderJsonError(obj: InterfaceError)(implicit request: RequestHeader, jsonModelFormatImplicits: JsonModelWriteImplicits): Result = {
    renderJsonError(jsonModelFormatImplicits.messageGen.toHttpErrorResponse(obj))
  }
  protected def renderJsonError(obj: HttpErrorResponse)(implicit request: RequestHeader, jsonModelFormatImplicits: JsonModelWriteImplicits): Result = {
    import jsonModelFormatImplicits._
    renderJsonError(obj.status, Json.toJson(obj).as[JsObject])
  }

  private def renderJsonError(status: Int, jsObject: JsObject)(implicit request: RequestHeader): Result = {
    accessLogger.error(
      s"message:Ajax NG\t" +
        s"status:[$status]\t" +
        s"method:[${request.method}]\t" +
        s"uri:[${request.uri.replaceAll("\\?.*", "")}]\t" +
        s"error:[${jsObject.toString()}]\t" +
        s"code:[${ThreadUtils.calledFrom(ThreadUtils.ERROR_STACK_DEPTH)}]"
    )
    new Status(status)(jsObject + ("ret_code" -> JsNumber(1)))
  }

}
