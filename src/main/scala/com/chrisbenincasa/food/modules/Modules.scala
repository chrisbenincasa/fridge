package com.chrisbenincasa.food.modules

import com.chrisbenincasa.food.DbExecutionContext
import com.chrisbenincasa.food.auth.WebSecurityRealm
import com.chrisbenincasa.food.config.FoodConfig
import com.chrisbenincasa.food.config.CustomReaders._
import com.chrisbenincasa.food.db.{DbAccess, DbProvider, H2Helpers}
import com.google.inject.{Module, Provides}
import com.twitter.inject.TwitterModule
import com.typesafe.config.ConfigFactory
import java.util.concurrent.Executors
import javax.inject.Singleton
import javax.sql.DataSource
import net.ceedubs.ficus.Ficus._
import net.ceedubs.ficus.readers.ArbitraryTypeReader._
import org.apache.shiro.authc.credential.{CredentialsMatcher, HashedCredentialsMatcher}
import org.apache.shiro.cache.{CacheManager, MemoryConstrainedCacheManager}
import org.apache.shiro.crypto.hash.Sha256Hash
import org.apache.shiro.guice.ShiroModule
import slick.jdbc.DriverDataSource
import scala.concurrent.ExecutionContext

object Modules {
  def apply(): Seq[Module] = Seq(
    new ExecutionContextModule,
    new ConfigModule,
    new H2Module,
    new AuthModule
  )
}

class ExecutionContextModule extends TwitterModule {
  @Provides
  @Singleton
  def executionContext: ExecutionContext = {
    scala.concurrent.ExecutionContext.Implicits.global
  }

  protected override def configure(): Unit = {
    bind(classOf[scala.concurrent.ExecutionContext]).
      annotatedWith(classOf[DbExecutionContext]).
      toInstance(scala.concurrent.ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(10)))
  }
}

class ConfigModule extends TwitterModule {
  @Provides
  @Singleton
  def config: FoodConfig = {
    ConfigFactory.load().as[FoodConfig]("food")
  }
}

class H2Module extends TwitterModule {
  @Provides
  @Singleton
  def dataSource(config: FoodConfig): DataSource = {
    new DriverDataSource(
      url = H2Helpers.url(config.db.url),
      user = config.db.user,
      password = config.db.password,
      driverObject = config.db.driver
    )
  }

  @Provides
  @Singleton
  def db(config: FoodConfig, dataSource: DataSource)
    (implicit @DbExecutionContext executionContext: ExecutionContext): DbAccess[_] = {
    val profile = config.db.driver match {
      case _: org.h2.Driver => slick.jdbc.H2Profile
      case x => throw new IllegalArgumentException(s"Unknown driver of type ${x.getClass.getSimpleName}")
    }

    new DbAccess(new DbProvider(dataSource, profile))
  }
}

class AuthModule extends ShiroModule {
  override def configureShiro(): Unit = {
    try {
      bind(classOf[CacheManager]).to(classOf[MemoryConstrainedCacheManager])
      bind(classOf[MemoryConstrainedCacheManager])
      bindRealm().to(classOf[WebSecurityRealm])
    } catch {
      case e: NoSuchElementException => addError(e)
    }
  }

  @Provides
  def matcher(): CredentialsMatcher = {
    val matcher = new HashedCredentialsMatcher(Sha256Hash.ALGORITHM_NAME)
    matcher.setHashIterations(1024)
    matcher.setStoredCredentialsHexEncoded(false)
    matcher
  }

  @Provides
  def realm(db: DbAccess[_], matcher: CredentialsMatcher)(implicit executionContext: ExecutionContext): WebSecurityRealm = {
    val realm = new WebSecurityRealm(db)
    realm.setCredentialsMatcher(matcher)
    realm
  }
}
