package mad.apps.sabenza.state.action

import mad.apps.sabenza.data.model.employer.Employer
import mad.apps.sabenza.framework.redux.Action

class EmployerUpdatedAction(val employer : Employer) : Action<Employer>("employer_updated", employer)

class AddOrUpdateEmployerAction(val employer : Employer) : Action<Employer>("add_or_update_employer", employer)