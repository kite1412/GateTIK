package kite1412.gatetik.feature.monitoring.desktop.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.compose.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.compose.cartesian.axis.VerticalAxis.ItemPlacer.Companion.count
import com.patrykandpatrick.vico.compose.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.compose.cartesian.data.CartesianValueFormatter
import com.patrykandpatrick.vico.compose.cartesian.data.lineModel
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.common.data.ExtraStore
import kite1412.gatetik.Logger
import kite1412.gatetik.designsystem.component.GlassBox
import kite1412.gatetik.designsystem.theme.GateTikTheme
import kite1412.gatetik.model.AccessLog
import kite1412.gatetik.network.mock.mockAccessLogs
import kite1412.gatetik.ui.preview.PreviewDesktopDark
import kite1412.gatetik.ui.util.LoadState
import kite1412.gatetik.ui.util.data
import kite1412.gatetik.util.now
import kite1412.gatetik.util.toLocalDateTime
import kotlinx.coroutines.runBlocking
import kotlin.math.roundToInt

private val StartAxisValueFormatter = CartesianValueFormatter { _, value, _ ->
    value.roundToInt().toString()
}

private val BottomAxisLabelKey = ExtraStore.Key<List<String>>()

private val BottomAxisValueFormatter = CartesianValueFormatter { context, value, _ ->
    val labels = context.model.extraStore[BottomAxisLabelKey]
    labels[value.toInt()]
}

@Composable
fun AccessTrend(
    accessLogs: LoadState<List<AccessLog>>,
    modifier: Modifier = Modifier
) {
    val modelProducer = remember { CartesianChartModelProducer() }
    val trends = accessLogs.data?.toTrends()

    LaunchedEffect(accessLogs.data) {
        trends?.let { trends ->
            modelProducer.runTransaction(trends)
        }
    }
    AccessTrend(
        totalLogGroups = trends?.size ?: 0,
        accessLogs = accessLogs,
        modelProducer = modelProducer,
        modifier = modifier
    )
}

@Composable
private fun AccessTrend(
    totalLogGroups: Int,
    accessLogs: LoadState<List<AccessLog>>,
    modelProducer: CartesianChartModelProducer,
    modifier: Modifier = Modifier
) {
    GlassBox(
        modifier = modifier
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Column {
                Text(
                    text = "TREN AKSES",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    ),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "24 jam terakhir",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .background(Color.Transparent)
            ) {
                LoadState(
                    state = accessLogs,
                    loading =  {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircularProgressIndicator()
                            Text(
                                text = "Memuat trend akses log",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    },
                    error = {
                        Text(
                            text = "Gagal memuat trend akses log",
                            style = MaterialTheme.typography.titleSmall
                        )
                    },
                    success = {
                        Chart(
                            totalLogGroups = totalLogGroups,
                            modelProducer = modelProducer,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                )
            }
        }
    }
}

data class AccessLogTrend(
    val hour: String,
    val count: Int
)

@Composable
private fun Chart(
    totalLogGroups: Int,
    modelProducer: CartesianChartModelProducer,
    modifier: Modifier = Modifier
) {
    CartesianChartHost(
        rememberCartesianChart(
            rememberLineCartesianLayer(),
            startAxis = VerticalAxis.rememberStart(
                valueFormatter = StartAxisValueFormatter,
                itemPlacer = remember { count(count = { totalLogGroups + 1 } ) }
            ),
            bottomAxis = HorizontalAxis.rememberBottom(
                valueFormatter = BottomAxisValueFormatter
            ),
        ),
        modelProducer = modelProducer,
        modifier = modifier
    )
}

private suspend fun CartesianChartModelProducer.runTransaction(trends: List<AccessLogTrend>) {
    runTransaction {
        lineModel {
            series(trends.map { it.count })
        }
        extras {
            it[BottomAxisLabelKey] = trends.map { trend ->
                trend.hour
            }
        }
    }
}

private fun List<AccessLog>.toTrends(): List<AccessLogTrend> {
    val today = now().toLocalDateTime().date

    return filter {
        it.createdAt.toLocalDateTime().date == today
    }
        .groupBy {
            it.createdAt.toLocalDateTime().time.hour
        }
        .map { (hour, accessLogs) ->
            AccessLogTrend(
                hour = "$hour:00",
                count = accessLogs.size
            )
                .also {
                    Logger.i(
                        tag = "AccessTrend",
                        message = it.toString()
                    )
                }
        }
        .sortedBy { it.hour }
}

@PreviewDesktopDark
@Composable
private fun AccessTrendPreview() {
    val modelProducer = remember { CartesianChartModelProducer() }
    val trends = mockAccessLogs.toTrends()
    runBlocking {
        modelProducer.runTransaction(trends)
    }

    GateTikTheme {
        Scaffold { p ->
              AccessTrend(
                  totalLogGroups = trends.size,
                  accessLogs = LoadState.Success(mockAccessLogs),
                  modelProducer = modelProducer,
                  modifier = Modifier.padding(p)
              )
        }
    }
}