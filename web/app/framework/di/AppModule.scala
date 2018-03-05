package framework.di

import java.util.concurrent.ConcurrentHashMap
import javax.inject.Singleton

import com.google.inject.{AbstractModule, Inject}
import com.zaxxer.hikari.{HikariConfig, HikariDataSource}
import domain.TeamID
import domain.repositories.ITeamRepository
import interface.gateway.DatabaseTeamRepository
import play.api.{Configuration, Environment}
import slick.jdbc.{JdbcBackend, PostgresProfile}
import slick.jdbc.PostgresProfile.api._

@Singleton
class DatabaseFactory @Inject()(conf: Configuration) {

  private val databases = new ConcurrentHashMap[TeamID, Database]()

  def getDatabase(teamId: TeamID): JdbcBackend#DatabaseDef = {
    databases.computeIfAbsent(teamId, new java.util.function.Function[TeamID, Database] {
      override def apply(v1: TeamID): PostgresProfile.api.Database = {
        val host = conf.getOptional[String]("database.host").getOrElse("localhost")
        val port = conf.getOptional[Int]("database.port").getOrElse(5432)
        val user = conf.getOptional[String]("database.user").getOrElse("postgres")
        val password = conf.getOptional[String]("database.password").getOrElse("postgres")
        val driver = conf.getOptional[String]("database.driver").getOrElse("")

        val hconf = new HikariConfig()
        hconf.setDriverClassName(driver)
        hconf.setJdbcUrl(s"jdbc:postgresql://$host:$port/${teamId.id}") // ?sslmode=require if ssl required
        hconf.setUsername(user)
        hconf.setPassword(password)

        // Pool configuration
        hconf.setConnectionTimeout(conf.getOptional[Long]("database.connectionTimeout").getOrElse(5000))
        hconf.setValidationTimeout(conf.getOptional[Long]("database.validationTimeout").getOrElse(5000))
        hconf.setIdleTimeout(conf.getOptional[Long]("database.idleTimeout").getOrElse(600000))
        hconf.setMaxLifetime(conf.getOptional[Long]("database.maxLifetime").getOrElse(1800000))
        hconf.setLeakDetectionThreshold(conf.getOptional[Long]("database.leakDetectionThreshold").getOrElse(0))
        hconf.setInitializationFailFast(conf.getOptional[Boolean]("database.initializationFailFast").getOrElse(false))
        //    conf.getOptional[String]("connectionTestQuery").foreach(hconf.setConnectionTestQuery)
        //    conf.getOptional[String]("connectionInitSql").foreach(hconf.setConnectionInitSql)
        val numThreads = conf.getOptional[Int]("database.numThreads").getOrElse(5)
        hconf.setMaximumPoolSize(conf.getOptional[Int]("database.maxConnections").getOrElse(numThreads))
        hconf.setMinimumIdle(conf.getOptional[Int]("database.minConnections").getOrElse(numThreads))
        hconf.setPoolName(conf.getOptional[String]("database.poolName").getOrElse(teamId.id))
        hconf.setRegisterMbeans(conf.getOptional[Boolean]("database.registerMbeans").getOrElse(false))

        val ds = new HikariDataSource(hconf)
        Database.forDataSource(ds, Some(numThreads))
      }
    })
  }

}

class AppModule(environment: Environment, configuration: Configuration) extends AbstractModule {

  override def configure(): Unit = {
    val _ = bind(classOf[ITeamRepository]).to(classOf[DatabaseTeamRepository])
  }

}

