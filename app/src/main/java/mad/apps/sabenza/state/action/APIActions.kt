package mad.apps.sabenza.state.action

import mad.apps.sabenza.data.model.me.Role
import mad.apps.sabenza.framework.redux.Action
import mad.apps.sabenza.middleware.MiddlewareError

class MiddlewareErrorAction(val throwable: Throwable, val errorTag : MiddlewareError = MiddlewareError.UNKNOWN) : Action<MiddlewareError>("api_error", errorTag)

class InvalidRoleAction(val requiredRole : Role, val currentRole: Role?, val invalidAction : Action<*>) : Action<Triple<Role, Role?, Action<*>>>("invalid_role_for_action", Triple(requiredRole, currentRole, invalidAction))