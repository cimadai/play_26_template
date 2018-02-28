package interface.gateway

object DatabaseProfile {
  // 使用するデータベースによってここを置き換える
  //val profile: slick.jdbc.JdbcProfile = slick.jdbc.H2Profile
  //val profile: slick.jdbc.JdbcProfile = slick.jdbc.MySQLProfile
  val profile: slick.jdbc.JdbcProfile = slick.jdbc.PostgresProfile
}

