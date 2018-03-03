package interface.presenter

import com.google.inject.Inject
import play.api.i18n.MessagesApi
import play.api.mvc.{InjectedController, RequestHeader, Result}

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

  def present(err: interface.InterfaceError)(implicit request: RequestHeader): Result = {
    renderJsonError(err)
  }

}

class JsonPresenter @Inject()
(
  override implicit val messagesApi: MessagesApi,
  implicit val implicitFactory: JsonModelWriteImplicitsFactory
)
  extends InjectedController
    with JsonResponsible
    with I18nJsonFormatImplicitsSupport {

  def present(response: domain.responses.IAPIResponse)(implicit request: RequestHeader): Result = {
    renderJsonOk(response)
  }

  def present(response: domain.errors.DomainError)(implicit request: RequestHeader): Result = {
    renderJsonError(response)
  }

}
