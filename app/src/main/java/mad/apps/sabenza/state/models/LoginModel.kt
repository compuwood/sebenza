package mad.apps.sabenza.state.models

import mad.apps.sabenza.data.rpc.calls.me.Me
import mad.apps.sabenza.framework.redux.RxModel

data class LoginModel(val username : String = "",
                      val password : String = "",
                      val inProgress : Boolean = false,
                      val isLoggedIn : Boolean = false,
                      val isSuccess: Boolean = false,
                      val hasError : Boolean = false,
                      val unsuccessfulAttempts : Int = 0,
                      val me : Me? = null,
                      val error : String = "") : RxModel()