package mad.apps.sabenza.state.models

import mad.apps.sabenza.data.model.employer.Employer
import mad.apps.sabenza.framework.redux.RxModel

data class EmployerModel(
        val employer : Employer? = null,
        val hasEmployer : Boolean = false
) : RxModel()