package interface.controller

import play.api.mvc.Request

trait IHttpController {
  /**
    * [[play.api.mvc.Request]]] をパースして Right([[domain.requests.IDomainRequest]]) を返す。
    * パースに失敗したら Left([[interface.InterfaceError]]) を返す。
    *
    * @param request Outside(PlayFramework)が認識するHttpRequest
    * @return
    */
  def parse()(implicit request: Request[_]): Either[interface.InterfaceError, domain.requests.IDomainRequest]
}


