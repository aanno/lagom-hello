package de.breitbandig.lagomhellostream.impl

import com.lightbend.lagom.scaladsl.api.ServiceCall
import de.breitbandig.lagomhellostream.api.LagomhelloStreamService
import de.breitbandig.lagomhello.api.LagomhelloService

import scala.concurrent.Future

/**
  * Implementation of the LagomhelloStreamService.
  */
class LagomhelloStreamServiceImpl(lagomhelloService: LagomhelloService) extends LagomhelloStreamService {
  def stream = ServiceCall { hellos =>
    Future.successful(hellos.mapAsync(8)(lagomhelloService.hello(_).invoke()))
  }
}
