package mad.apps.sabenza.ui.employee.signup.addskills

import mad.apps.sabenza.framework.rx.NetworkTransformer
import mad.apps.sabenza.framework.rx.state.RxSingleStateObserver
import mad.apps.sabenza.framework.rx.state.RxStateBinder
import mad.apps.sabenza.framework.ui.BasePresenter
import mad.apps.sabenza.state.action.LinkSkillsToEmployeeAction
import mad.apps.sabenza.state.models.SkillsModel
import mad.apps.sabenza.ui.employee.signup.paymentinfo.PaymentInfoScreen
import mad.apps.sabenza.ui.util.MissingFeature
import zendesk.suas.Store

class AddSkillsPresenter(store: Store) : BasePresenter<AddSkillsViewInterface>(store), AddSkillsPresenterInterface {

    override fun gotoNextScreen() {
        routeToNewTop(PaymentInfoScreen.provideViewController())
    }

    override fun saveSkills(skillsList: List<String>) {
        val availableSkills = store.state.getState(SkillsModel::class.java)!!.availableSkills
        val skills = skillsList.map {
            val skillDescription = it
            availableSkills.find { it.description == skillDescription }
        }.filterNotNull().map {
            Pair(it, 1)
        }

        RxStateBinder.dispatchAndBindForResult(LinkSkillsToEmployeeAction(skills), store, SkillsModel::class.java)
                .compose(NetworkTransformer())
                .subscribe(object : RxSingleStateObserver<SkillsModel>() {
                    override fun onSuccess(t: SkillsModel) {
                        gotoNextScreen()
                    }
                })

        MissingFeature.show(view.getContext(), "We need to add experience in the presenter")
    }


    override fun getCuratedSkillsList(): List<String> {
        return store.state.getState(SkillsModel::class.java)!!
                .availableSkills
                .map { it.description!! }
    }

}