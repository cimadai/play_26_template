package framework.controllers

import javax.inject.Inject

import domain.requests._
import interface.controller._
import interface.presenter.{AppPageIndexPagePresenter, InterfaceErrorPagePresenter, JsonPresenter}
import play.api.i18n.LangImplicits
import play.api.mvc._
import usecase.{CreateUser, DeleteUser, GetUser, GetUsers}

import scala.concurrent.{ExecutionContext, Future}

class AppPage @Inject()
(
  indexPageController: AppPageIndexPageController,
  indexPagePresenter: AppPageIndexPagePresenter,
  getUserUseCase: GetUser,
  getUsersUseCase: GetUsers,
  createUserUseCase: CreateUser,
  deleteUserUseCase: DeleteUser,
  getUserController: GetUserController,
  getUsersController: GetUsersController,
  createUserController: CreateUserController,
  deleteUserController: DeleteUserController,
  errorPagePresenter: InterfaceErrorPagePresenter,
  jsonPresenter: JsonPresenter,
  implicit val ec: ExecutionContext
) extends InjectedController with LangImplicits {
  implicit def parser: PlayBodyParsers = parse

  def index: Action[IndexPageRequest] = Action(indexPageController.parse).async { implicit request =>
    Future.successful(indexPagePresenter.present(request.body))
  }

  def createUser: Action[CreateUserRequest] = Action(createUserController.parse).async { implicit request =>
    createUserUseCase
      .createUser(request.body)
      .map(_.fold(
        error =>
          jsonPresenter.present(error),
        ret =>
          jsonPresenter.present(ret)
      ))
  }

  def getUser(userId: String): Action[GetUserRequest] = Action(getUserController.parse(userId)).async { implicit request =>
    getUserUseCase
      .getUser(request.body)
      .map(_.fold(
        notFound =>
          jsonPresenter.present(notFound),
        resp =>
          jsonPresenter.present(resp)
      ))
  }

  def getUsers: Action[GetUsersRequest] = Action(getUsersController.parse).async { implicit request =>
      getUsersUseCase
        .getUsers(request.body)
        .map(jsonPresenter.present)
  }

  def deleteUser(userId: String): Action[DeleteUserRequest] = Action(deleteUserController.parse(userId)).async { implicit request =>
    val getUserRequest = GetUserRequest(request.body.teamId, request.body.id)
    getUserUseCase
      .getUser(getUserRequest)
      .flatMap(_.fold(
        notFound =>
          Future.successful(jsonPresenter.present(notFound)),
        _ =>
          deleteUserUseCase
            .deleteUser(request.body)
            .map(jsonPresenter.present)
      ))
  }
}
