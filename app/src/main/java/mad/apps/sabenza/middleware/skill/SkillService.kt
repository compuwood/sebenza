package mad.apps.sabenza.middleware.skill

import io.reactivex.Observable
import mad.apps.sabenza.data.api.SabenzaAPI
import mad.apps.sabenza.data.api.SkillsAPI
import mad.apps.sabenza.data.model.employee.EmployeeSkill
import mad.apps.sabenza.data.rpc.calls.employee.Skill
import mad.apps.sabenza.framework.rx.NetworkTransformer
import mad.apps.sabenza.framework.rx.observer.EnhancedSingleObserver
import mad.apps.sabenza.framework.service.MiddlewareService
import mad.apps.sabenza.middleware.MiddlewareError
import mad.apps.sabenza.middleware.rx.MiddlewareSingleObserver
import mad.apps.sabenza.state.action.*
import mad.apps.sabenza.state.models.EmployeeModel
import zendesk.suas.Action
import zendesk.suas.Continuation
import zendesk.suas.Dispatcher
import zendesk.suas.GetState

interface SkillService : MiddlewareService

class SkillServiceImpl(
        val sabenzaAPI: SabenzaAPI, val skillsAPI: SkillsAPI
) : SkillService {

    override fun onAction(action: Action<*>, state: GetState, dispatcher: Dispatcher, continuation: Continuation) {
        when (action) {
            is RefreshSkillsAction -> {
                refreshSkills(dispatcher)
                continuation.next(action)
            }

            is LoginOrSignUpSuccessAction -> {
                refreshSkills(dispatcher)
                refreshEmployeeSkills(dispatcher)
                continuation.next(action)
            }

            is RefreshEmployeeSkillAction -> {
                refreshEmployeeSkills(dispatcher)
                continuation.next(action)
            }

            is LinkSkillToEmployeeAction -> {
                if (state.state.getState(EmployeeModel::class.java)!!.hasEmployee) {
                    linkSkillToEmployee(dispatcher, state.state.getState(EmployeeModel::class.java)!!, action)
                    continuation.next(action)
                } else {
                    continuation.next(NoEmployeeLinkedAction())
                }
            }

            is LinkSkillsToEmployeeAction -> {
                if (state.state.getState(EmployeeModel::class.java)!!.hasEmployee) {
                    linkSkillsToEmployee(dispatcher, state.state.getState(EmployeeModel::class.java)!!, action)
                    continuation.next(action)
                } else {
                    continuation.next(NoEmployeeLinkedAction())
                }
            }

            is UnlinkSkillToEmployeeAction -> {
                if (state.state.getState(EmployeeModel::class.java)!!.hasEmployee) {
                    unlinkSkillToEmployee(dispatcher, action.skill)
                    continuation.next(action)
                } else {
                    continuation.next(NoEmployeeLinkedAction())
                }
            }

            else -> continuation.next(action)

        }
    }

    private fun refreshEmployeeSkills(dispatcher: Dispatcher) {
        skillsAPI.fetchSkillsForEmployee()
                .compose(NetworkTransformer())
                .subscribe(object : EnhancedSingleObserver<List<EmployeeSkill>>() {
                    override fun onSuccess(t: List<EmployeeSkill>) {
                        dispatcher.dispatch(EmployeeSkillsUpdatedAction(t))
                    }

                    override fun onError(e: Throwable) {
                        dispatcher.dispatch(MiddlewareErrorAction(throwable = e, errorTag = MiddlewareError.FAILED_TO_REFRESH_EMPLOYEE_SKILLS))
                    }
                })
    }

    private fun unlinkSkillToEmployee(dispatcher: Dispatcher, skill: Skill) {
        skillsAPI.deleteSkillFromAnEmployee(skillId = "eq." + skill.id.toString())
                .compose(NetworkTransformer())
                .subscribe(object : EnhancedSingleObserver<Any>() {
                    override fun onSuccess(t: Any) {
                        dispatcher.dispatch(RefreshEmployeeSkillAction())
                    }

                    override fun onError(e: Throwable) {
                        dispatcher.dispatch(MiddlewareErrorAction(throwable = e))
                    }

                })

    }

    private fun linkSkillsToEmployee(dispatcher: Dispatcher, employeeModel: EmployeeModel, action: LinkSkillsToEmployeeAction) {
        var observable: Observable<EmployeeSkill> = Observable.empty()
        for (skillPair in action.skills) {
            observable = observable.mergeWith(skillsAPI.addSkillToEmployee(
                    employeeId = employeeModel.employee!!.id.toString(),
                    skillId = skillPair.first.id.toString(),
                    yearsExperience = skillPair.second.toString()).toObservable())
        }

        observable.toList()
                .compose(NetworkTransformer())
                .subscribe(object : MiddlewareSingleObserver<List<EmployeeSkill>>(dispatcher) {
                    override fun onSuccess(t: List<EmployeeSkill>) {
                        dispatcher.dispatch(RefreshEmployeeSkillAction())
                    }
                })
    }

    private fun linkSkillToEmployee(dispatcher: Dispatcher, employeeModel: EmployeeModel, action: LinkSkillToEmployeeAction) {
        skillsAPI.addSkillToEmployee(
                employeeId = employeeModel.employee!!.id.toString(),
                skillId = action.skill.id.toString(),
                yearsExperience = action.yearsExperience.toString())
                .compose(NetworkTransformer())
                .subscribe(object : EnhancedSingleObserver<EmployeeSkill>() {
                    override fun onError(e: Throwable) {
                        dispatcher.dispatch(MiddlewareErrorAction(e, MiddlewareError.FAILED_TO_LINK_SKILL_TO_EMPLOYEE))
                    }

                    override fun onSuccess(t: EmployeeSkill) {
                        dispatcher.dispatch(RefreshEmployeeSkillAction())
                    }
                })
    }

    private fun refreshSkills(dispatcher: Dispatcher) {
        skillsAPI.fetchAllSkills()
                .compose(NetworkTransformer<List<Skill>>())
                .subscribe(object : EnhancedSingleObserver<List<Skill>>() {
                    override fun onError(e: Throwable) {
                        dispatcher.dispatch(MiddlewareErrorAction(throwable = e, errorTag = MiddlewareError.FAILED_TO_REFRESH_SKILLS))
                    }

                    override fun onSuccess(t: List<Skill>) {
                        dispatcher.dispatch(NewSkillsAvailableAction(skills = t))
                    }
                })
    }
}