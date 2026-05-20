package kite1412.portaltik

import androidx.lifecycle.ViewModel
import kite1412.portaltik.model.User

class PortalTikViewModel : ViewModel() {
    var signedInUser: User? = null
        private set
}