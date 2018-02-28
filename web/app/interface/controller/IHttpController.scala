package interface.controller

trait IHttpController {
  /**
    * [[interface.Types.OutsideHttpRequest]] をパースして Right([[domain.requests.IDomainRequest]]) を返す。
    * パースに失敗したら Left([[interface.InterfaceError]]) を返す。
    *
    * @param request Outside(PlayFramework)が認識するHttpRequest
    * @return
    */
  def parse()(implicit request: interface.Types.OutsideHttpRequest): Either[interface.InterfaceError, domain.requests.IDomainRequest]
}


