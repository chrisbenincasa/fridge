package com.chrisbenincasa.food

import com.chrisbenincasa.food.controllers._
import com.chrisbenincasa.food.modules.Modules
import com.twitter.finagle.http.{Request, Response}
import com.twitter.finatra.http.HttpServer
import com.twitter.finatra.http.filters.{CommonFilters, LoggingMDCFilter, TraceIdMDCFilter}
import com.twitter.finatra.http.routing.HttpRouter
import com.twitter.inject.annotations.Lifecycle
import java.nio.file.Paths
import org.apache.shiro.SecurityUtils
import org.apache.shiro.config.IniSecurityManagerFactory

object ServerMain extends Server

class Server extends HttpServer {

  override def warmupComplete(): Unit = {
    info(s"CWD: ${Paths.get("").toAbsolutePath.toString}")
  }

  @Lifecycle
  protected override def postInjectorStartup(): Unit = {
    super.postInjectorStartup()
    val securityManager = injector.instance[org.apache.shiro.mgt.SecurityManager]
    SecurityUtils.setSecurityManager(securityManager)
  }

  @Lifecycle override protected
  def warmup(): Unit = {
    super.warmup()
//
//    val factory = new IniSecurityManagerFactory("classpath:shiro.ini")
//    val securityManager = factory.getInstance()
//
//    SecurityUtils.setSecurityManager(securityManager)
  }

  override def modules = Modules()

  override def defaultFinatraHttpPort = ":9999"

  override def configureHttp(router: HttpRouter) {
    router.
      filter[LoggingMDCFilter[Request, Response]].
      filter[TraceIdMDCFilter[Request, Response]].
      filter[CommonFilters].
      add[FoodController].
      add[AdminController].
      add[AuthController]
  }

}
