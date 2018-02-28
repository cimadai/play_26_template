package framework.controllers

import javax.inject.Inject

import domain.TeamID
import domain.requests.{GetUsersRequest, IndexPageRequest}
import interface.controller.{AppPageGetUsersController, AppPageIndexPageController}
import interface.presenter.{AppPageIndexPagePresenter, InterfaceErrorPagePresenter, JsonPresenter}
import play.api.i18n.LangImplicits
import play.api.libs.streams.Accumulator
import play.api.mvc._
import usecase.AppPageGetUsersUseCase

import scala.concurrent.{ExecutionContext, Future}

trait TeamIdParsable {

  private def parseTeamID(domain: String): TeamID = {
    if (domain.contains(".")) {
      TeamID(domain.substring(0, domain.indexOf(".")))
    } else {
      TeamID("postgres")
    }
  }

  protected val teamIdParser: BodyParser[TeamID] = BodyParser("teamIdParser") { request =>
    Accumulator.done(Right(parseTeamID(request.domain)))
  }
}

class AppPage @Inject()
(
  implicit val ec: ExecutionContext,
  indexPageController: AppPageIndexPageController,
  indexPagePresenter: AppPageIndexPagePresenter,
  getUsersController: AppPageGetUsersController,
  getUsersUseCase: AppPageGetUsersUseCase,
  errorPagePresenter: InterfaceErrorPagePresenter,
  jsonPresenter: JsonPresenter
) extends InjectedController with LangImplicits with TeamIdParsable {

  def index: Action[TeamID] = Action(teamIdParser).async { implicit request =>
    Future.successful(indexPageController.parse() match {
      case Left(err) =>
        errorPagePresenter.present(err)
      case Right(_: IndexPageRequest) =>
        indexPagePresenter.present(request.body, domain.responses.Done())
    })
  }

  def getUsers: Action[TeamID] = Action(teamIdParser).async { implicit request =>
    getUsersController.parse() match {
      case Left(err) =>
        Future.successful(errorPagePresenter.present(err))
      case Right(req: GetUsersRequest) =>
        getUsersUseCase
          .getUsers(request.body, req)
          .map(jsonPresenter.present)
    }
  }

}
