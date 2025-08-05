package mad.apps.sabenza.state.util

import mad.apps.sabenza.data.model.employer.Employer
import mad.apps.sabenza.state.models.EmployerModel
import zendesk.suas.GetState

object EmployerStateUtil {
    fun hasEmployer(state: GetState): Boolean {
        val employerModel = state.state.getState(EmployerModel::class.java)
        if (employerModel != null && employerModel.hasEmployer) {
            return employerModel.hasEmployer
        }
        return false
    }

    fun getEmployer(state: GetState): Employer? {
        if (hasEmployer(state)) {
            return state.state.getState(EmployerModel::class.java)!!.employer
        }
        return null
    }
}