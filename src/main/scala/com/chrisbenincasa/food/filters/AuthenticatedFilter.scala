package com.chrisbenincasa.food.filters

import com.twitter.finagle.http.{Request, Response, Status}
import com.twitter.finagle.{Service, SimpleFilter}
import com.twitter.util.Future
import org.apache.shiro.SecurityUtils

class AuthenticatedFilter extends SimpleFilter[Request, Response] {
  override def apply(request: Request, service: Service[Request, Response]): Future[Response] = {
    val session = SecurityUtils.getSubject.getSession
    val isAuthenticated = SecurityUtils.getSubject.isAuthenticated

    if (isAuthenticated) {
      service(request)
    } else {
      Future.value(Response(Status.Unauthorized))
    }
  }
}
