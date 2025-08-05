package mad.apps.sabenza.middleware.employer

import io.reactivex.Single
import mad.apps.sabenza.data.api.EmployerAPI
import mad.apps.sabenza.data.model.employer.Employer
import mad.apps.sabenza.data.model.me.Role
import mad.apps.sabenza.framework.rx.NetworkTransformer
import mad.apps.sabenza.framework.service.MiddlewareService
import mad.apps.sabenza.middleware.MiddlewareError
import mad.apps.sabenza.middleware.rx.MiddlewareSingleObserver
import mad.apps.sabenza.state.action.AddOrUpdateEmployerAction
import mad.apps.sabenza.state.action.EmployerUpdatedAction
import mad.apps.sabenza.state.action.LoginOrSignUpSuccessAction
import mad.apps.sabenza.state.util.EmployerStateUtil
import zendesk.suas.Action
import zendesk.suas.Continuation
import zendesk.suas.Dispatcher
import zendesk.suas.GetState

interface EmployerService : MiddlewareService

class EmployerServiceImpl(val employerAPI: EmployerAPI) : EmployerService {

    override fun onAction(action: Action<*>, state: GetState, dispatcher: Dispatcher, continuation: Continuation) {
        when(action){
            is LoginOrSignUpSuccessAction -> {
                if (action.me.role == Role.EMPLOYER) {
                    refreshEmployer(dispatcher)
                }
                continuation.next(action)
            }

            is AddOrUpdateEmployerAction -> {
                addOrUpdateEmployer(dispatcher, action, state)
                continuation.next(action)
            }

            else -> continuation.next(action)
        }
    }

    private fun addOrUpdateEmployer(dispatcher: Dispatcher, action: AddOrUpdateEmployerAction, state: GetState) {
        val hasEmployer = EmployerStateUtil.hasEmployer(state)
        val employer = action.employer

        val single : Single<Employer>

        if (!hasEmployer) {
            single = employerAPI.addEmployer(employer)
        } else {
            single = employerAPI.updateEmployer(employer)
        }

        single.compose(NetworkTransformer())
                .subscribe(object :MiddlewareSingleObserver<Employer>(dispatcher, MiddlewareError.FAILED_TO_ADD_OR_UPDATE_EMPLOYER) {
                    override fun onSuccess(t: Employer) {
                        dispatcher.dispatch(EmployerUpdatedAction(t))
                    }
                })

    }

    private fun refreshEmployer(dispatcher: Dispatcher) {
        employerAPI.fetchEmployer()
                .compose(NetworkTransformer())
                .subscribe(object : MiddlewareSingleObserver<Employer>(dispatcher, MiddlewareError.FAILED_TO_REFRESH_EMPLOYER) {
                    override fun onSuccess(t: Employer) {
                        dispatcher.dispatch(EmployerUpdatedAction(t))
                    }
                })
    }

}