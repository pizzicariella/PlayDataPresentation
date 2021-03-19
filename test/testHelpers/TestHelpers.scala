package testHelpers

import play.api.mvc.{AnyContentAsEmpty, DefaultActionBuilder, DefaultMessagesActionBuilderImpl,
  DefaultMessagesControllerComponents, MessagesControllerComponents}
import play.api.test.Helpers.{stubBodyParser, stubControllerComponents}

object TestHelpers {
  //Method taken from scalatestplus-play version 4.0.0 which could not be used because not compatible with Play 2.6
  def stubMessagesControllerComponents(): MessagesControllerComponents = {
    val stub = stubControllerComponents()
    new DefaultMessagesControllerComponents(
      new DefaultMessagesActionBuilderImpl(stubBodyParser(AnyContentAsEmpty), stub.messagesApi)(stub.executionContext),
      DefaultActionBuilder(stub.actionBuilder.parser)(stub.executionContext), stub.parsers,
      stub.messagesApi, stub.langs, stub.fileMimeTypes, stub.executionContext
    )
  }
}
