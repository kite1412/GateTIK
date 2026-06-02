package kite1412.portaltik.feature.monitoring.desktop.usermanagement

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kite1412.portaltik.datastore.PortalTikDataStore
import kite1412.portaltik.domain.Authentication
import kite1412.portaltik.feature.monitoring.desktop.DesktopBaseViewModel
import kite1412.portaltik.model.User
import kite1412.portaltik.model.UserRole
import kite1412.portaltik.model.UserStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class DesktopUserManagementViewModel(
    authentication: Authentication,
    dataStore: PortalTikDataStore
) : DesktopBaseViewModel(authentication, dataStore) {

    var searchText by mutableStateOf("")
        private set

    var selectedRole by mutableStateOf<UserRole?>(null)
        private set

    var selectedStatus by mutableStateOf<UserStatus?>(null)
        private set

    private val _users = MutableStateFlow(
        listOf(
            User(1, "John Doe", "student@example.com", UserRole.STUDENT, UserStatus.ACTIVE, "STU001"),
            User(2, "Jane Smith", "student2@example.com", UserRole.STUDENT, UserStatus.PENDING, "STU002"),
            User(1, "John Doe", "student@example.com", UserRole.STUDENT, UserStatus.ACTIVE, "STU001"),
            User(2, "Jane Smith", "student2@example.com", UserRole.STUDENT, UserStatus.PENDING, "STU002"),
            User(1, "John Doe", "student@example.com", UserRole.STUDENT, UserStatus.ACTIVE, "STU001"),
            User(2, "Jane Smith", "student2@example.com", UserRole.STUDENT, UserStatus.PENDING, "STU002"),
            User(1, "John Doe", "student@example.com", UserRole.STUDENT, UserStatus.ACTIVE, "STU001"),
            User(2, "Jane Smith", "student2@example.com", UserRole.STUDENT, UserStatus.PENDING, "STU002"),
            User(1, "John Doe", "student@example.com", UserRole.STUDENT, UserStatus.ACTIVE, "STU001"),
            User(2, "Jane Smith", "student2@example.com", UserRole.STUDENT, UserStatus.PENDING, "STU002"),
            User(1, "John Doe", "student@example.com", UserRole.STUDENT, UserStatus.ACTIVE, "STU001"),
            User(2, "Jane Smith", "student2@example.com", UserRole.STUDENT, UserStatus.PENDING, "STU002"),
            User(1, "John Doe", "student@example.com", UserRole.STUDENT, UserStatus.ACTIVE, "STU001"),
            User(2, "Jane Smith", "student2@example.com", UserRole.STUDENT, UserStatus.PENDING, "STU002"),
            User(1, "John Doe", "student@example.com", UserRole.STUDENT, UserStatus.ACTIVE, "STU001"),
            User(2, "Jane Smith", "student2@example.com", UserRole.STUDENT, UserStatus.PENDING, "STU002"),
        )
    )
    val users = _users.asStateFlow()

    fun onSearchTextChange(text: String) {
        searchText = text
    }

    fun onRoleFilterChange(role: UserRole?) {
        selectedRole = role
    }

    fun onStatusFilterChange(status: UserStatus?) {
        selectedStatus = status
    }
}
