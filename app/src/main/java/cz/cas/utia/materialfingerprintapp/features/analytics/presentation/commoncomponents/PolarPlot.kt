package cz.cas.utia.materialfingerprintapp.features.analytics.presentation.commoncomponents

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import cz.cas.utia.materialfingerprintapp.features.analytics.presentation.filter.getAxisName
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

// draw PolarPlot basic objects (two circles and 16 (or different amount) axes) to existing canvas
fun DrawScope.drawBasicPolarPlot(
    axesAmount: Int,
    center: Offset,
    maxRadius: Float,
    circleColor: Color,
    axisColor: Color,
    axisLabels: List<String>? = null,
    showAxisLabels: Boolean = false
) {
    // draw outer circle
    drawCircle(
        color = circleColor,
        center = center,
        radius = maxRadius,
        style = Stroke(width = 2f)
    )

    // draw circle in zero
    val zeroRadius = maxRadius / 2f
    drawCircle(
        color = circleColor,
        center = center,
        radius = zeroRadius,
        style = Stroke(width = 4f)
    )

    // Draw axes and labels
    for (i in 0 until axesAmount) {
        val angle = (2 * PI * i / axesAmount).toFloat()
        val endX = center.x + cos(angle) * maxRadius
        val endY = center.y + sin(angle) * maxRadius

        drawLine(
            color = axisColor,
            start = center,
            end = Offset(endX, endY)
        )

        // show axis labels
        if (showAxisLabels) {
            val label = axisLabels!!.getOrNull(i) ?: "Axis $i"
            val labelX = center.x + cos(angle) * (maxRadius * 0.75f)
            val labelY = center.y + sin(angle) * (maxRadius * 0.75f)
            drawContext.canvas.nativeCanvas.drawText(
                label,
                labelX,
                labelY,
                Paint().apply {
                    color = circleColor.toArgb()
                    textSize = 40f // todo az budou opravdova jmena os tak kdyztak zmensit at se to tam vejde
                    textAlign = Paint.Align.CENTER
                }
            )
        }
    }
}

// draw the values (the plot itself)
fun DrawScope.drawPolarPath(
    axisValues: List<Float>,
    axesAmount: Int,
    center: Offset,
    maxRadius: Float,
    color: Color,
    pointRadius: Float,
    activeAxis: Int? = null,
    isInteractive: Boolean = false
) {
    val path = Path().apply {
        for (i in 0 until axesAmount) {
            val angle = (2 * PI * i / axesAmount).toFloat()
            val value = axisValues[i] * (maxRadius / 300f)
            val pointX = center.x + cos(angle) * value
            val pointY = center.y + sin(angle) * value

            if (i == 0) moveTo(pointX, pointY) else lineTo(pointX, pointY)

            drawCircle(
                color = if (isInteractive && i == activeAxis) Color.Red else color,
                center = Offset(pointX, pointY),
                radius = pointRadius
            )
        }
        close()
    }

    drawPath(path, color.copy(alpha = 0.2f), style = Fill) // draw the inside of the polar plot (the filling)
    drawPath(path, color, style = Stroke(width = 5f)) // draw the borders of the polar plot
}

@Composable
fun PolarPlotCanvas(
    axisValues: List<Float>,
    axisLabels: List<String>? = null,
    showAxisLabels: Boolean = false,
    circleColor: Color,
    axisColor: Color,
    backgroundColor: Color,
    firstPlotColor: Color,
    secondAxisValues: List<Float>? = null,
    secondPlotColor: Color? = null,
    pointRadius: Float = 10f,
    activeAxis: Int? = null,
    isInteractive: Boolean = false,
    canvasSizeState: MutableState<Size>? = null,
    modifier: Modifier = Modifier
) {
    val axesAmount = axisValues.size

    Canvas(
        modifier = modifier
            .background(color = backgroundColor),
        onDraw = {
            // update canvas size if needed
            canvasSizeState?.value = size

            val center = Offset(size.width / 2f, size.height / 2f)
            val maxRadius = size.width / 2f

            drawBasicPolarPlot(
                axesAmount = axesAmount,
                center = center,
                maxRadius = maxRadius,
                circleColor = circleColor,
                axisColor = axisColor,
                axisLabels = axisLabels,
                showAxisLabels = showAxisLabels
            )

            drawPolarPath(
                axisValues = axisValues,
                axesAmount = axesAmount,
                center = center,
                maxRadius = maxRadius,
                color = firstPlotColor,
                pointRadius = pointRadius,
                activeAxis = activeAxis,
                isInteractive = isInteractive
            )

            // Optionally draw the second set of axis values
            secondAxisValues?.let { secondValues ->
                drawPolarPath(
                    axisValues = secondValues,
                    axesAmount = axesAmount,
                    center = center,
                    maxRadius = maxRadius,
                    color = secondPlotColor!!,
                    pointRadius = pointRadius,
                    isInteractive = false
                )
            }
        }
    )
}

@Composable
fun NonInteractivePolarPlot(
    firstAxisValues: List<Float>,
    showAxisLabels: Boolean,
    secondAxisValues: List<Float>? = null,
    firstPlotColor: Color,
    secondPlotColor: Color,
    maxPlotSize: Dp = 400.dp,
) {
    val axesAmount = firstAxisValues.size
    val axisLabels = List(axesAmount) { axisId -> getAxisName(axisId) }

    val circleColor: Color = MaterialTheme.colorScheme.primary
    val axisColor: Color = MaterialTheme.colorScheme.secondary //todo stejne barvy jako u apply filter? Dát si je někam do configu a tahat je odtamtud d osdilenych composablí
    val backgroundColor: Color = MaterialTheme.colorScheme.background

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .sizeIn(
                    maxWidth = maxPlotSize,
                    maxHeight = maxPlotSize
                )
                .aspectRatio(1f)
        ) {
            PolarPlotCanvas(
                axisValues = firstAxisValues,
                axisLabels = axisLabels,
                circleColor = circleColor,
                axisColor = axisColor,
                backgroundColor = backgroundColor,
                firstPlotColor = firstPlotColor,
                secondAxisValues = secondAxisValues,
                secondPlotColor = secondPlotColor,
                pointRadius = 10f,
                showAxisLabels = showAxisLabels,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
fun PolarPlotLegendRow(
    rectangleColor: Color,
    text: String,
    textStyle: TextStyle = MaterialTheme.typography.bodySmall
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Canvas(
            modifier = Modifier.size(12.dp)
        ) {
            drawRect(color = rectangleColor)
        }
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = text,
            style = textStyle
        )
    }
}
