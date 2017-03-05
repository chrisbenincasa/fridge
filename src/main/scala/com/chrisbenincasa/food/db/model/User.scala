package com.chrisbenincasa.food.db.model

import org.apache.shiro.crypto.SecureRandomNumberGenerator
import org.apache.shiro.crypto.hash.Sha256Hash
import slick.jdbc.JdbcProfile
import scala.concurrent.{ExecutionContext, Future}

case class User(
  id: Option[Long],
  name: String,
  email: String
)

case class UserCredential(
  userId: Long,
  password: String,
  salt: Array[Byte]
)

class Users(val driver: JdbcProfile) {
  import driver.api._

  type TableType = UsersTable

  private val credentials = new UserCredentials(driver)

  class UsersTable(tag: Tag) extends Table[User](tag, "users") {
    def id = column[Long]("id", O.AutoInc, O.PrimaryKey)
    def name = column[String]("name")
    def email = column[String]("email")

    def * = (id.?, name, email) <> (User.tupled, User.unapply)
  }

  val query = TableQuery[UsersTable]

  def findById(id: Long) = {
    query.filter(_.id === id).result.headOption
  }

  def findByEmail(email: String) = {
    (for {
      user <- query.filter(_.email === email)
      creds <- credentials.query if creds.userId === user.id
    } yield {
      (user, creds)
    }).result.headOption
  }

  def newUser(name: String, email: String, password: String)(implicit executionContext: ExecutionContext): DBIO[Long] = {
    val user = User(None, name, email)

    ((query returning query.map(_.id)) += user).flatMap(userId => {
      val salt = new SecureRandomNumberGenerator().nextBytes().getBytes
      val hashedPassword = new Sha256Hash(password, salt, 1024).toBase64 // TODO: REPLACE WITH THING
      val creds = UserCredential(userId, hashedPassword, salt)

      (credentials.query += creds).map(_ => userId)
    })
  }
}

class UserCredentials(val driver: JdbcProfile) {
  import driver.api._

  type TableType = UserCredentialsTable

  class UserCredentialsTable(tag: Tag) extends Table[UserCredential](tag, "user_credentials") {
    def userId = column[Long]("user_id")
    def password = column[String]("password")
    def salt = column[Array[Byte]]("salt")
    def * = (userId, password, salt) <> (UserCredential.tupled, UserCredential.unapply)

    def user = foreignKey("userId", userId, new Users(driver).query)(_.id, onUpdate = ForeignKeyAction.Restrict, onDelete = ForeignKeyAction.Cascade)
  }

  val query = TableQuery[UserCredentialsTable]
}
