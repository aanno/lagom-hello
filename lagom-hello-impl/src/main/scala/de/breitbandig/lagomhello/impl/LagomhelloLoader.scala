package de.breitbandig.lagomhello.impl

import com.lightbend.lagom.scaladsl.api.ServiceLocator
import com.lightbend.lagom.scaladsl.api.ServiceLocator.NoServiceLocator
import com.lightbend.lagom.scaladsl.persistence.cassandra.CassandraPersistenceComponents
import com.lightbend.lagom.scaladsl.server._
import com.lightbend.lagom.scaladsl.devmode.LagomDevModeComponents
import play.api.libs.ws.ahc.AhcWSComponents
import de.breitbandig.lagomhello.api.LagomhelloService
import com.lightbend.lagom.scaladsl.broker.kafka.LagomKafkaComponents
import com.softwaremill.macwire._

class LagomhelloLoader extends LagomApplicationLoader {

  override def load(context: LagomApplicationContext): LagomApplication =
    new LagomhelloApplication(context) {
      override def serviceLocator: ServiceLocator = NoServiceLocator
    }

  override def loadDevMode(context: LagomApplicationContext): LagomApplication =
    new LagomhelloApplication(context) with LagomDevModeComponents

  override def describeServices = List(
    readDescriptor[LagomhelloService]
  )
}

abstract class LagomhelloApplication(context: LagomApplicationContext)
  extends LagomApplication(context)
    with CassandraPersistenceComponents
    with LagomKafkaComponents
    with AhcWSComponents {

  // Bind the service that this server provides
  override lazy val lagomServer = serverFor[LagomhelloService](wire[LagomhelloServiceImpl])

  // Register the JSON serializer registry
  override lazy val jsonSerializerRegistry = LagomhelloSerializerRegistry

  // Register the lagom-hello persistent entity
  persistentEntityRegistry.register(wire[LagomhelloEntity])
}
