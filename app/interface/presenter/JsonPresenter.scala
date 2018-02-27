package interface.presenter

import com.google.inject.Inject
import interface.Types.{OutsideHttpRequest, OutsideHttpResponse}
import play.api.i18n.MessagesApi
import play.api.mvc.InjectedController

// Json Clientに結果を返すためのプレゼンター

/**
  * Interface層でのエラーをJson Clientに返却するプレゼンター
  *
  * @param implicitFactory リクエスト毎に決まるLANGに基づくJsonModelFormatterのFactory
  */
class InterfaceErrorJsonPresenter @Inject()
(
  val implicitFactory: JsonModelWriteImplicitsFactory
) extends InjectedController
  with JsonResponsible
  with I18nJsonFormatImplicitsSupport {

  def present(err: interface.InterfaceError)(implicit request: OutsideHttpRequest): OutsideHttpResponse = {
    renderJsonError(err)
  }

}

class JsonPresenter @Inject()(
                               override implicit val messagesApi: MessagesApi,
                               implicit val implicitFactory: JsonModelWriteImplicitsFactory
                             )
  extends InjectedController
    with JsonResponsible
    with I18nJsonFormatImplicitsSupport {

  def present(responseModel: domain.responses.IDomainResponse)(implicit request: OutsideHttpRequest): OutsideHttpResponse = {
    responseModel match {
      case ret: domain.responses.IDomainSuccess => renderJsonOk(ret)
      case err: domain.errors.DomainError => renderJsonError(err)
    }
  }
}

