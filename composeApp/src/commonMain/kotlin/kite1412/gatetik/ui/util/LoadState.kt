package kite1412.gatetik.ui.util

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kite1412.gatetik.designsystem.theme.Black70
import kite1412.gatetik.designsystem.theme.White55
import kite1412.gatetik.ui.compositionlocal.LocalDarkMode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

sealed interface LoadState<out T> {
    data class Loading(
        val message: String = ""
    ) : LoadState<Nothing>
    data class Success<T>(
        val data: T
    ) : LoadState<T>
    data class Error(
        val message: String
    ) : LoadState<Nothing>
}

fun <T> Flow<LoadState<T>>.stateIn(
    scope: CoroutineScope,
    started: SharingStarted = SharingStarted.WhileSubscribed(5000),
    initialValue: LoadState<T> = LoadState.Loading()
): StateFlow<LoadState<T>> = stateIn(
    scope = scope,
    started = started,
    initialValue = initialValue
)

@Composable
fun <T> UiLoadState(
    state: LoadState<T>,
    modifier: Modifier = Modifier,
    onRetry: (() -> Unit)? = null,
    successContent: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        when (state) {
            is LoadState.Loading -> LoadingState(message = state.message)
            is LoadState.Success -> successContent()
            is LoadState.Error -> ErrorState(
                message = state.message,
                onRetry = onRetry
            )
        }
    }
}

@Composable
fun <T> LoadState(
    state: LoadState<T>,
    loading: @Composable (message: String) -> Unit,
    error: @Composable (message: String) -> Unit,
    success: @Composable (data: T) -> Unit
) {
    when (state) {
        is LoadState.Error -> error(state.message)
        is LoadState.Loading -> loading(state.message)
        is LoadState.Success<T> -> success(state.data)
    }
}

@Composable
fun LoadingState(
    message: String,
    modifier: Modifier = Modifier
) {
    val color = if (LocalDarkMode.current) White55 else Black70

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(
            color = color
        )
        Text(
            text = message,
            style = MaterialTheme.typography.bodySmall,
            color = color
        )
    }
}

@Composable
private fun ErrorState(
    message: String,
    modifier: Modifier = Modifier,
    onRetry: (() -> Unit)? = null
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(message)
        onRetry?.let { onRetry ->
            Button(onClick = onRetry) {
                Text("Retry")
            }
        }
    }
}