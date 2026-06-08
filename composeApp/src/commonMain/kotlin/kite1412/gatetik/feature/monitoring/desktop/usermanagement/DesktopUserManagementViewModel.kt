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
import kite1412.gatetik.domain.model.UserCreate
import kite1412.gatetik.domain.model.UserUpdate
import kite1412.gatetik.domain.repository.UserRepository
import kite1412.gatetik.feature.monitoring.desktop.DesktopBaseViewModel
import kite1412.gatetik.feature.monitoring.desktop.PollingResult
import kite1412.gatetik.feature.monitoring.desktop.usermanagement.compositionlocal.RemoteImageResolver
import kite1412.gatetik.model.User
import kite1412.gatetik.model.UserRole
import kite1412.gatetik.model.UserStatus
import kite1412.gatetik.ui.util.LoadState
import kite1412.gatetik.ui.util.UiEvent
import kite1412.gatetik.util.onError
import kite1412.gatetik.util.onSuccess
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DesktopUserManagementViewModel(
    authentication: Authentication,
    dataStore: GateTikDataStore,
    private val userRepository: UserRepository
) : DesktopBaseViewModel(authentication, dataStore) {
    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    private val userStore = mutableStateMapOf<UserRepository.GetParams, PaginatedListResult<User>>()
    private var currentPage by mutableIntStateOf(1)
    var perPage by mutableIntStateOf(15)
        private set
    var searchText by mutableStateOf("")
        private set
    var selectedRole by mutableStateOf<UserRole?>(null)
        private set
    var selectedStatus by mutableStateOf<UserStatus?>(null)
        private set
    var users by mutableStateOf<LoadState<List<User>>>(LoadState.Loading("Memuat daftar pengguna"))
        private set

    private val params = snapshotFlow {
        UserRepository.GetParams(
            role = selectedRole,
            status = selectedStatus,
            search = searchText,
            perPage = perPage,
            page = currentPage
        )
    }
        .distinctUntilChanged()
        .onEach(::updateUsersOnParamsChange)
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
    val ktmResolver = RemoteImageResolver { payload ->
        runCatching { payload.toString().toInt() }
            .getOrNull()
            ?.let { studentId ->
                userRepository.previewKtm(studentId)
                    .onSuccess {
                        return@let it
                    }

                null
            }
    }

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
        updateCurrentPage(1)
    }

    fun updateCurrentPage(page: Int) {
        this.currentPage = page
    }

    init {
        initPolling(::pollData)
    }

    suspend fun pollData() = params.first()?.let { params ->
        updateUsersOnParamsChange(
            params = params,
            useCached = false,
            restartState = false
        )
        PollingResult.Success
    } ?: PollingResult.Error("Gagal memperbarui data pengguna")

    fun refreshUsers() {
        viewModelScope.launch {
            pollData()
            showSnackbar("Data dimuat ulang")
        }
    }

    fun editUser(data: UserUpdate) {
        viewModelScope.launch {
            updateUser(
                data = data,
                successMessage = "Berhasil mengedit pengguna",
                errorMessage = "Gagal mengedit pengguna, harap coba lagi"
            )
        }
    }

    fun addUser(data: UserCreate) {
        viewModelScope.launch {
            userRepository.addUser(data)
                .onSuccess { user ->
                    userStore.forEach { (params, res) ->
                        if (
                            params.role == null ||
                            params.role == user.role ||
                            params.status == null ||
                            user.status == params.status
                        ) {
                            userStore[params] = res.copy(
                                data = listOf(user) + res.data
                            )
                            if (params == this@DesktopUserManagementViewModel.params.first())
                                updateUsersOnParamsChange(params)
                        }
                    }

                    showSnackbar("Berhasil menambahkan pengguna")
                }
                .onError {
                    showSnackbar("Gagal menambahkan pengguna, harap coba lagi")
                }
        }
    }

    fun activateUser(user: User) {
        viewModelScope.launch {
            with(user) {
                updateUser(
                    data = UserUpdate(
                        id = id,
                        fullName = fullName,
                        email = email,
                        password = null,
                        npmNip = institutionNumber,
                        phoneNumber = phoneNumber,
                        role = role,
                        status = UserStatus.ACTIVE,
                        ktmPath = ktmPath
                    ),
                    successMessage = "Berhasil mengaktivasi pengguna",
                    errorMessage = "Gagal mengaktivasi pengguna"
                )
            }
        }
    }

    fun deleteUser(user: User) {
        viewModelScope.launch {
            userRepository.deleteUser(user.id)
                .onSuccess {
                    userStore.forEach { (params, res) ->
                        val index = res.data.indexOfFirst { it.id == user.id }
                        if (index != -1) userStore[params] = res.copy(
                            data = res.data.toMutableList().apply {
                                removeAt(index)
                            }
                        )

                        if (params == this@DesktopUserManagementViewModel.params.first())
                            updateUsersOnParamsChange(params)
                    }

                    showSnackbar("Berhasil menghapus pengguna")
                }
                .onError {
                    showSnackbar("Gagal menghapus pengguna, harap coba lagi")
                }
        }
    }

    private suspend fun updateUser(
        data: UserUpdate,
        successMessage: String,
        errorMessage: String
    ) {
        userRepository.updateUser(data)
            .onSuccess { user ->
                userStore.forEach { (params, res) ->
                    val index = res.data.indexOfFirst { it.id == user.id }

                    if (index != -1) userStore[params] = res.copy(
                        data = res.data.toMutableList().apply {
                            if (
                                (params.role != null && user.role != params.role) ||
                                (params.status != null && user.status != params.status)
                            )
                                removeAt(index)
                            else set(index, user)
                        }
                    ) else {
                        if (params.role == user.role || params.status == user.status)
                            userStore[params] = res.copy(
                                data = listOf(user) + res.data
                            )
                    }

                    if (params == this@DesktopUserManagementViewModel.params.first())
                        updateUsersOnParamsChange(params)
                }

                showSnackbar(successMessage)
            }
            .onError {
                showSnackbar(errorMessage)
            }
    }

    private suspend fun updateUsersOnParamsChange(
        params: UserRepository.GetParams,
        useCached: Boolean = true,
        restartState: Boolean = true
    ) {
        if (userStore[params] != null && useCached)
            users = LoadState.Success(userStore[params]!!.data)
        else {
            if (restartState) users = LoadState.Loading("Memuat daftar pengguna")
            userRepository.getAll(params)
                .onSuccess {
                    val sorted = it.data
                        .sortedByDescending { u -> u.createdAt }
                    users = LoadState.Success(sorted)
                    userStore[params] = it.copy(
                        data = sorted
                    )
                    userStore.forEach { (params, res) ->
                        val sortedIds = sorted.map { u -> u.id }.toSet()
                        val intersects = res.data.filter { u -> u.id in sortedIds }

                        if (intersects.isNotEmpty()) {
                            val updates = sorted.associateBy { u -> u.id }

                            userStore[params] = res.copy(
                                data = res.data.map { user ->
                                    updates[user.id] ?: user
                                }
                            )
                        }
                    }
                }
                .onError {
                    users = LoadState.Error("Gagal memuat daftar pengguna")
                }
        }
    }

    private suspend fun showSnackbar(message: String) {
        _uiEvent.emit(UiEvent.ShowSnackbar(message))
    }
}
