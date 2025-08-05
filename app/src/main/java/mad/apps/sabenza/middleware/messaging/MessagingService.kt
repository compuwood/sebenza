package mad.apps.sabenza.middleware.messaging

import mad.apps.sabenza.data.api.MessagingAPI
import mad.apps.sabenza.data.model.messaging.SebenzaMessage
import mad.apps.sabenza.framework.rx.NetworkTransformer
import mad.apps.sabenza.framework.service.MiddlewareService
import mad.apps.sabenza.middleware.rx.MiddlewareSingleObserver
import mad.apps.sabenza.state.action.LoginOrSignUpSuccessAction
import mad.apps.sabenza.state.action.RefreshMessagesAction
import zendesk.suas.Action
import zendesk.suas.Continuation
import zendesk.suas.Dispatcher
import zendesk.suas.GetState

interface MessagingService : MiddlewareService

class MessagingServiceImpl(val messagingAPI: MessagingAPI) : MessagingService {

    override fun onAction(action: Action<*>, state: GetState, dispatcher: Dispatcher, continuation: Continuation) {
        when (action) {
            is LoginOrSignUpSuccessAction -> {
                updateMessages(dispatcher)
                continuation.next(action)
            }

            is RefreshMessagesAction -> {
                updateMessages(dispatcher)
                continuation.next(action)
            }


            else -> continuation.next(action)
        }
    }

    private fun updateMessages(dispatcher: Dispatcher) {
        messagingAPI.getMessages()
                .compose(NetworkTransformer())
                .subscribe(object : MiddlewareSingleObserver<List<SebenzaMessage>>(dispatcher) {
                    override fun onSuccess(t: List<SebenzaMessage>) {

                    }
                })
    }

}