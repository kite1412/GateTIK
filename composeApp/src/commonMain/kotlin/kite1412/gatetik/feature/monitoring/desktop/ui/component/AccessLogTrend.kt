package kite1412.gatetik.feature.monitoring.desktop.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LocalContentColor
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
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberAxisLabelComponent
import com.patrykandpatrick.vico.compose.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.compose.cartesian.data.CartesianValueFormatter
import com.patrykandpatrick.vico.compose.cartesian.data.lineModel
import com.patrykandpatrick.vico.compose.cartesian.layer.LineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLine
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoScrollState
import com.patrykandpatrick.vico.compose.common.Fill
import com.patrykandpatrick.vico.compose.common.data.ExtraStore
import com.patrykandpatrick.vico.compose.common.vicoTheme
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
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlin.math.roundToInt

private val StartAxisValueFormatter = CartesianValueFormatter { _, value, _ ->
    value.roundToInt().toString()
}

private val BottomAxisLabelKey = ExtraStore.Key<List<AccessLogTrend>>()

private val BottomAxisValueFormatter = CartesianValueFormatter { context, value, _ ->
    val labels = context.model.extraStore[BottomAxisLabelKey]
    labels[value.toInt()].run {
        (if (isYesterday) "Kemarin, " else "") + hour
    }
}

@Composable
fun AccessLogTrend(
    accessLogs: LoadState<List<AccessLog>>,
    modifier: Modifier = Modifier,
    headerTrailing: (@Composable () -> Unit)? = null
) {
    val modelProducer = remember { CartesianChartModelProducer() }
    val trends = accessLogs.data?.toTrends()

    LaunchedEffect(trends) {
        trends?.let { trends ->
            if (trends.isNotEmpty()) modelProducer.runTransaction(trends)
        }
    }
    AccessLogTrend(
        totalLogGroups = trends?.distinctBy { it.count }?.size ?: 0,
        trends = trends,
        accessLogs = accessLogs,
        modelProducer = modelProducer,
        modifier = modifier,
        headerTrailing = headerTrailing
    )
}

@Composable
private fun AccessLogTrend(
    totalLogGroups: Int,
    trends: List<AccessLogTrend>?,
    accessLogs: LoadState<List<AccessLog>>,
    modelProducer: CartesianChartModelProducer,
    modifier: Modifier = Modifier,
    headerTrailing: (@Composable () -> Unit)? = null
) {
    GlassBox(
        modifier = modifier
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
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
                headerTrailing?.invoke()
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .background(Color.Transparent),
                contentAlignment = Alignment.Center
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
                                text = "Memuat tren akses log",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    },
                    error = {
                        Text(
                            text = "Gagal memuat tren akses log",
                            style = MaterialTheme.typography.titleSmall
                        )
                    },
                    success = {
                        if (trends?.isNotEmpty() == true) Chart(
                            totalLogGroups = totalLogGroups,
                            modelProducer = modelProducer,
                            modifier = Modifier.fillMaxSize()
                        ) else Text(
                            text = "Tidak ada akses log",
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                )
            }
        }
    }
}

data class AccessLogTrend(
    val isYesterday: Boolean,
    val hour: String,
    val count: Int
)

@Composable
private fun Chart(
    totalLogGroups: Int,
    modelProducer: CartesianChartModelProducer,
    modifier: Modifier = Modifier
) {
    val labelStyle = MaterialTheme.typography.bodySmall
    val axisLabelComponent = rememberAxisLabelComponent(
        style = labelStyle.copy(
            color = LocalContentColor.current,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold
        )
    )

    CartesianChartHost(
        rememberCartesianChart(
            rememberLineCartesianLayer(
                lineProvider = LineCartesianLayer.LineProvider.series(
                    vicoTheme.lineCartesianLayerColors.map { color ->
                        LineCartesianLayer.rememberLine(
                            fill = LineCartesianLayer.LineFill.single(Fill(color)),
                            interpolator = LineCartesianLayer.Interpolator.catmullRom(),
                            areaFill = LineCartesianLayer.AreaFill.single(
                                fill = Fill(color.copy(alpha = 0.1f))
                            )
                        )
                    }
                )
            ),
            startAxis = VerticalAxis.rememberStart(
                valueFormatter = StartAxisValueFormatter,
                itemPlacer = remember {
                    count(
                        count = { totalLogGroups + 1 }
                    )
                },
                label = axisLabelComponent
            ),
            bottomAxis = HorizontalAxis.rememberBottom(
                valueFormatter = BottomAxisValueFormatter,
                label = axisLabelComponent
            )
        ),
        modelProducer = modelProducer,
        modifier = modifier,
        scrollState = rememberVicoScrollState(scrollEnabled = false)
    )
}

private suspend fun CartesianChartModelProducer.runTransaction(trends: List<AccessLogTrend>) {
    runTransaction {
        lineModel {
            series(trends.map { it.count })
        }
        extras {
            it[BottomAxisLabelKey] = trends
        }
    }
}

private fun List<AccessLog>.toTrends(): List<AccessLogTrend> {
    val now = now()
    val nowDate = now.toLocalDateTime().date

    val last24Hours = now.minus(
        value = 24,
        unit = DateTimeUnit.HOUR,
        timeZone = TimeZone.currentSystemDefault()
    )

    return filter { it.createdAt >= last24Hours }
        .groupBy {
            val dateTime = it.createdAt.toLocalDateTime()
            dateTime.date to dateTime.hour
        }
        .map { (key, accessLogs) ->
            val (date, hour) = key

            AccessLogTrend(
                isYesterday = date < nowDate,
                hour = "%02d:00".format(hour),
                count = accessLogs.size
            )
        }
        .sortedWith(
            compareByDescending<AccessLogTrend> { it.isYesterday }
                .thenBy { it.hour }
        )
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
              AccessLogTrend(
                  totalLogGroups = trends.size,
                  trends = trends,
                  accessLogs = LoadState.Success(mockAccessLogs),
                  modelProducer = modelProducer,
                  modifier = Modifier.padding(p)
              )
        }
    }
}