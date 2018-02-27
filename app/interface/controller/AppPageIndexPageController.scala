package interface.controller

import com.google.inject.Inject
import domain.requests.GetUsersRequest
import interface.presenter.JsonModelReadImplicits
import play.api.libs.json.Reads
import play.api.mvc.InjectedController


trait JsonParsable {
  /**
    * Request内に含まれるjson文字列をパースし、インスタンスを生成する。
    *
    * @param request HTTP Request
    */
  protected def parseAs[T](request: interface.Types.OutsideHttpRequest)
                          (implicit reads: Reads[T])
  : Either[interface.JsonParseError, T] = {

    request.body.asJson match {
      case Some(jsValue) => jsValue.validate[T].fold(err => {
        Left(interface.JsonParseError(err.toMap))
      }, obj => Right(obj))
      case _ => Left(interface.JsonParseError(Map.empty))
    }

  }
}

class AppPageIndexPageController() extends IHttpController {

  def parse()
           (implicit request: interface.Types.OutsideHttpRequest)
  : Either[interface.InterfaceError, domain.requests.IndexPageRequest] = {

    Right(domain.requests.IndexPageRequest())

  }

}

class AppPageGetUsersController @Inject()
(
  val readImplicits: JsonModelReadImplicits
) extends InjectedController with JsonParsable {

  def parse()(implicit request: interface.Types.OutsideHttpRequest)
  : Either[interface.InterfaceError, domain.requests.GetUsersRequest] = {

    Right(GetUsersRequest(
      pageNum = request.getQueryString("pageNum").map(_.toInt).getOrElse(0),
      pageSize = request.getQueryString("pageSize").map(_.toInt).getOrElse(0)
    ))

  }
}
