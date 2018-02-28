package interface.presenter

import com.google.inject.Inject
import controllers.AssetsFinder
import interface.Types.{OutsideHttpRequest, OutsideHttpResponse}
import play.api.i18n.I18nSupport
import play.api.mvc.InjectedController

/**
  * Interface層でのエラーをHTTP Clientに返却するプレゼンター
  *
  * @param implicitFactory リクエスト毎に決まるLANGに基づくJsonModelFormatterのFactory
  */
class InterfaceErrorPagePresenter @Inject()
(
  val implicitFactory: JsonModelWriteImplicitsFactory
) extends InjectedController
  with I18nJsonFormatImplicitsSupport {

  def present(err: interface.InterfaceError)(implicit request: OutsideHttpRequest): OutsideHttpResponse = {
    val messageGen = request2messageGen(request)
    BadRequest(messageGen.toMessage(err))
  }

}

/**
  * IndexPageをHTTP Clientに返却するプレゼンター
  */
class AppPageIndexPagePresenter @Inject()
(
  implicit val env: play.api.Environment,
  implicit val assetsFinder: AssetsFinder
) extends InjectedController with I18nSupport {

  def present(done: domain.responses.Done)(implicit request: OutsideHttpRequest): OutsideHttpResponse = {
    request2User(request).map(user => {
      // use user
      Ok(framework.views.html.Application.index(request.flash))
    }).getOrElse(NotFound(""))
  }

  def request2User(request: OutsideHttpRequest): Option[domain.User] = {
    Some(domain.User("example", 30))
  }

}
