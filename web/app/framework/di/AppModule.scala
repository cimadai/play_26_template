package framework.di

import com.google.inject.{AbstractModule, Inject}
import domain.TeamID
import domain.repositories.ITeamRepository
import interface.gateway.DatabaseTeamRepository
import play.api.{Configuration, Environment}
import slick.jdbc.JdbcBackend
import slick.jdbc.PostgresProfile.api._

class DatabaseFactory @Inject()(configuration: Configuration) {

  def getDatabase(teamId: TeamID): JdbcBackend#DatabaseDef = {
    val host = configuration.getOptional[String]("database.host").getOrElse("localhost")
    val port = configuration.getOptional[Int]("database.port").getOrElse(5432)
    val user = configuration.getOptional[String]("database.user").getOrElse("postgres")
    val password = configuration.getOptional[String]("database.password").getOrElse("postgres")
    val driver = configuration.getOptional[String]("slick.dbs.default.db.driver").getOrElse("")

    Database.forURL(
      url = s"jdbc:postgresql://$host:$port/${teamId.id}",
      user = user,
      password = password,
      driver = driver
    )
  }

}

class AppModule(environment: Environment, configuration: Configuration) extends AbstractModule {

  override def configure(): Unit = {
    val _ = bind(classOf[ITeamRepository]).to(classOf[DatabaseTeamRepository])
  }

}

