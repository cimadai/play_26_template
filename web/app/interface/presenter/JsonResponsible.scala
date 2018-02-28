package interface.presenter

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

  protected def renderJsonOk(obj: domain.responses.IDomainSuccess)(implicit request: RequestHeader, jsonModelFormatImplicits: JsonModelWriteImplicits): Result = {
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

  protected def renderJsonError(obj: domain.errors.DomainError)(implicit request: RequestHeader, jsonModelFormatImplicits: JsonModelWriteImplicits): Result = {
    renderJsonError(obj.status, toJsObject(obj))
  }

  private def toJsObject(obj: domain.responses.IDomainResponse)(implicit jsonModelFormatImplicits: JsonModelWriteImplicits): JsObject = {
    import jsonModelFormatImplicits._
    obj match {
      case o: interface.JsonParseError => Json.toJson(o).as[JsObject]
      case o: domain.errors.NotFoundSuchUser => Json.toJson(o).as[JsObject]
      case o: domain.responses.GetUsersResponse => Json.toJson(o).as[JsObject]
    }
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

  protected def renderJsonError(obj: domain.errors.DomainError, jsObject: JsObject)(implicit request: RequestHeader, jsonModelFormatImplicits: JsonModelWriteImplicits): Result = {
    renderJsonError(obj.status, jsObject ++ toJsObject(obj))
  }
}
