package interface.gateway.dao

import interface.gateway.DatabaseProfile.profile.api.{Rep, Table, _}
import domain.User
import slick.lifted._

class UserTable(tag: Tag) extends Table[User](tag, "users") {
  def id: Rep[String] = column[String]("id", O.Length(255))
  def name: Rep[String] = column[String]("name", O.Length(255))
  def age: Rep[Int] = column[Int]("age")

  def * : ProvenShape[User] = (id, name, age) <> (User.tupled, User.unapply)
}

