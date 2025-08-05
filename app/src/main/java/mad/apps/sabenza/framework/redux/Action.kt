package mad.apps.sabenza.framework.redux

import zendesk.suas.Action

abstract class Action<E>(actionType: String, data: E) : Action<E>(actionType, data)