package dcos.metronome.eventbus.impl

import akka.actor.{Actor, ActorLogging, Stash}
import dcos.metronome.behavior.{ActorBehavior, Behavior}

class EventSubscriber(

  val behavior: Behavior
) extends Actor with Stash with ActorLogging with ActorBehavior {

  override def receive = around {
    //TODO: need to somehow grab the request context here
    ???
  }

}
