package dcos.metronome.api.v1.controllers

import akka.event.EventStream
import dcos.metronome.api.{ApiConfig, Authorization}
import mesosphere.marathon.plugin.auth.{Authenticator, Authorizer}
import mesosphere.util.Logging
import spray.http.RemoteAddress

class EventsController(

   val authenticator: Authenticator,
   val authorizer: Authorizer,
   val config: ApiConfig
) extends Authorization {

  def eventsSSE(eventType: Option[String]) = AuthorizedAction.async { implicit request =>

    eventType match {
      case Some(filter) =>
      case None =>
    }

    ???
  }


}

object EventsController extends Logging {

  def eventStreamLogic(eventStream: EventStream, bufferSize: Int, remoteAddress: RemoteAddress) = {
    //TODO: effectively create a new actor that subscribes to the event stream and publishes correctly
  }

}

/*
object EventsController extends StrictLogging {
  /**
    * An event source which:
    * - Yields all MarathonEvent's for the event bus whilst a leader.
    * - is leader aware. The stream completes if this instance abdicates.
    * - publishes an EventStreamAttached when the stream is materialized
    * - publishes an EventStreamDetached when the stream is completed or fails
    * @param eventStream the event stream to subscribe to
    * @param bufferSize the size of events to buffer, if there is no demand.
    * @param remoteAddress the remote address
    */
  def eventStreamLogic(eventStream: EventStream, leaderEvents: Source[LeadershipTransition, Any], bufferSize: Int, remoteAddress: RemoteAddress) = {

    // Used to propagate a "stream close" signal when we see a LeadershipState.Standy event
    val leaderLossKillSwitch =
      leaderEvents.collect { case evt @ LeadershipTransition.Standby => evt }

    EnrichedSource.eventBusSource(classOf[MarathonEvent], eventStream, bufferSize, OverflowStrategy.fail)
      .via(EnrichedFlow.stopOnFirst(leaderLossKillSwitch))
      .watchTermination()(Keep.both)
      .mapMaterializedValue {
        case (cancellable, completed) =>
          eventStream.publish(EventStreamAttached(remoteAddress = remoteAddress.toString()))
          logger.info(s"EventStream attached: $remoteAddress")

          completed.onComplete { _ =>
            eventStream.publish(EventStreamDetached(remoteAddress = remoteAddress.toString()))
            logger.info(s"EventStream detached: $remoteAddress")
          }(ExecutionContexts.callerThread)
          cancellable
      }
  }
}
 */