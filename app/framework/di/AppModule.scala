package framework.di

import com.google.inject.{AbstractModule, Inject, Provider}
import domain.repositories.IUserRepository
import interface.gateway.UserRepository
import play.api.db.slick.DatabaseConfigProvider
import play.api.{Application, Configuration, Environment}
import slick.jdbc.PostgresProfile.api._
import slick.jdbc.{JdbcBackend, JdbcProfile}

class DatabaseProvider @Inject()
(
  implicit val application: Application
) extends Provider[Database] {
  override def get(): JdbcBackend#DatabaseDef = {
    //"""Use DatabaseConfigProvider#get[P] or SlickApi#dbConfig[P]("default") on injected instances""".stripMargin,
    DatabaseConfigProvider.get[JdbcProfile](application).db
  }
}

class AppModule(environment: Environment, configuration: Configuration) extends AbstractModule {
  override def configure(): Unit = {
    bind(classOf[Database]).toProvider(classOf[DatabaseProvider])

    val _ = bind(classOf[IUserRepository]).to(classOf[UserRepository])
  }
}
