package interface.presenter

import com.google.inject.Inject
import controllers.AssetsFinder
import domain.TeamID
import play.api.i18n.I18nSupport
import play.api.mvc.{InjectedController, RequestHeader, Result}

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

  def present(err: interface.InterfaceError)(implicit request: RequestHeader): Result = {
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

  def present(teamId: TeamID, done: domain.responses.Done)(implicit request: RequestHeader): Result = {
    Ok(framework.views.html.Application.index())
  }
//
//  def request2User(teamId: TeamID): Option[domain.User] = {
//    Some(domain.User("aaa", "example", 30))
//  }

}
