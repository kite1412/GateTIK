package kite1412.gatetik.feature.monitoring.desktop.usermanagement

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.viewModelScope
import kite1412.gatetik.datastore.GateTikDataStore
import kite1412.gatetik.domain.Authentication
import kite1412.gatetik.domain.model.PaginatedListResult
import kite1412.gatetik.domain.repository.UserRepository
import kite1412.gatetik.feature.monitoring.desktop.DesktopBaseViewModel
import kite1412.gatetik.model.User
import kite1412.gatetik.model.UserRole
import kite1412.gatetik.model.UserStatus
import kite1412.gatetik.ui.util.LoadState
import kite1412.gatetik.util.onError
import kite1412.gatetik.util.onSuccess
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class DesktopUserManagementViewModel(
    authentication: Authentication,
    dataStore: GateTikDataStore,
    private val userRepository: UserRepository
) : DesktopBaseViewModel(authentication, dataStore) {
    private val userStore = mutableStateMapOf<UserRepository.GetParams, PaginatedListResult<User>>()
    var searchText by mutableStateOf("")
        private set
    var selectedRole by mutableStateOf<UserRole?>(null)
        private set
    var selectedStatus by mutableStateOf<UserStatus?>(null)
        private set
    var perPage by mutableIntStateOf(15)
        private set
    var users by mutableStateOf<LoadState<List<User>>>(LoadState.Loading("Memuat daftar pengguna"))
        private set
    private val params = combine(
        flow = snapshotFlow { selectedRole },
        flow2 = snapshotFlow { selectedStatus },
        flow3 = snapshotFlow { searchText },
        flow4 = snapshotFlow { perPage }
    ) { role, status, searchText, perPage ->
        val params = UserRepository.GetParams(
            role = role,
            status = status,
            search = searchText,
            perPage = perPage
        )
        updateUsersOnParamsChange(params)
        params
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = null
        )

    val pagination = combine(
        flow = snapshotFlow { userStore },
        flow2 = params
    ) { userStore, params ->
        userStore[params]?.pagination
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    fun onSearchTextChange(text: String) {
        searchText = text
    }

    fun onRoleFilterChange(role: UserRole?) {
        selectedRole = role
    }

    fun onStatusFilterChange(status: UserStatus?) {
        selectedStatus = status
    }

    fun updatePerPage(perPage: Int) {
        this.perPage = perPage
    }

    private suspend fun updateUsersOnParamsChange(params: UserRepository.GetParams) {
        if (userStore[params] != null)
            users = LoadState.Success(userStore[params]!!.data)
        else {
            users = LoadState.Loading("Memuat daftar pengguna")
            userRepository.getAll(params)
                .onSuccess {
                    users = LoadState.Success(it.data)
                    userStore[params] = it
                }
                .onError {
                    users = LoadState.Error("Gagal memuat daftar pengguna")
                }
        }
    }
}
