package framework.controllers

import javax.inject.Inject

import interface.controller.{AppPageGetUsersController, AppPageIndexPageController}
import interface.presenter.{AppPageIndexPagePresenter, InterfaceErrorPagePresenter, JsonPresenter}
import play.api.i18n.LangImplicits
import play.api.mvc._
import usecase.AppPageGetUsersUseCase

import scala.concurrent.{ExecutionContext, Future}

class AppPage @Inject()
(
  implicit val ec: ExecutionContext,
  indexPageController: AppPageIndexPageController,
  indexPagePresenter: AppPageIndexPagePresenter,
  getUsersController: AppPageGetUsersController,
  getUsersUseCase: AppPageGetUsersUseCase,
  errorPagePresenter: InterfaceErrorPagePresenter,
  jsonPresenter: JsonPresenter
) extends InjectedController with LangImplicits {

  def index: Action[AnyContent] = Action.async { implicit request =>
    Future.successful(indexPageController.parse() match {
      case Left(err) =>
        errorPagePresenter.present(err)
      case Right(_: domain.requests.IndexPageRequest) =>
        indexPagePresenter.present(domain.responses.Done())
    })
  }

  def getUsers: Action[AnyContent] = Action.async { implicit request =>
    getUsersController.parse() match {
      case Left(err) =>
        Future.successful(errorPagePresenter.present(err))
      case Right(req: domain.requests.GetUsersRequest) =>
        getUsersUseCase
          .getUsers(req)
          .map(jsonPresenter.present)
    }
  }

}
