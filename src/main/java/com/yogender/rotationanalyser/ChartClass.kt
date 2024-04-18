package com.yogender.rotationanalyser

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import co.yml.charts.axis.AxisData
import co.yml.charts.common.model.Point
import co.yml.charts.ui.linechart.LineChart
import co.yml.charts.ui.linechart.model.GridLines
import co.yml.charts.ui.linechart.model.IntersectionPoint
import co.yml.charts.ui.linechart.model.Line
import co.yml.charts.ui.linechart.model.LineChartData
import co.yml.charts.ui.linechart.model.LinePlotData
import co.yml.charts.ui.linechart.model.LineStyle
import co.yml.charts.ui.linechart.model.LineType
import co.yml.charts.ui.linechart.model.SelectionHighlightPoint
import co.yml.charts.ui.linechart.model.SelectionHighlightPopUp
import co.yml.charts.ui.linechart.model.ShadowUnderLine

@Composable
fun ChartClass(viewModel: RotationViewModel) {
    var context = LocalContext.current
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        if (viewModel.databaseList.size > 0) {
            Column {
                LineChartScreen(viewModel.databaseList.map { it.pitch }, "Pitch")
                LineChartScreen(viewModel.databaseList.map { it.roll }, "Roll")
                LineChartScreen(viewModel.databaseList.map { it.yaw }, "Yaw")
            }
        }
        else{
            Toast.makeText(context, "The database is empty", Toast.LENGTH_SHORT).show()
        }
    }
}

@Composable
fun LineChartScreen(data: List<Float>, angleName: String) {
    val steps = 5

    // Generate x-axis data as index of the data points
//    val xAxisData = AxisData.Builder()
//        .axisStepSize(180.dp)
//        .backgroundColor(Color.Transparent)
//        .steps(data.size - 1)
//        .labelData { i -> i.toString() }
//        .labelAndAxisLinePadding(15.dp)
//        .axisLineColor(MaterialTheme.colorScheme.tertiary)
//        .build()
// Generate x-axis data as index of the data points
    val xAxisData = AxisData.Builder()
        .axisStepSize((data.size / 10).toFloat().dp) // Adjust the step size according to the data size
        .backgroundColor(Color.Transparent)
        .steps(data.size - 1)
        .labelData { i -> i.toString() }
        .labelAndAxisLinePadding(15.dp)
        .axisLineColor(MaterialTheme.colorScheme.tertiary)
        .build()

    // Generate y-axis data
    val yAxisData = AxisData.Builder()
        .steps(steps * 2) // Double the steps to accommodate negative values
        .backgroundColor(Color.Transparent)
        .labelAndAxisLinePadding(20.dp)
        .labelData { i ->
            // Adjust labels to include negative values
            val yScale = 100 / steps
            ((i - steps) * yScale).toString()
        }
        .axisLineColor(MaterialTheme.colorScheme.tertiary)
        .build()

    // Create points data using angle values
    val pointsData = data.mapIndexed { index, value ->
        Point(index.toFloat(), value)
    }

    val lineChartData = LineChartData(
        linePlotData = LinePlotData(
            lines = listOf(
                Line(
                    dataPoints = pointsData,
                    LineStyle(
                        color = Color.Blue,
                        lineType = LineType.SmoothCurve(isDotted = false)
                    ),
                    IntersectionPoint(color = Color.Red),
                    SelectionHighlightPoint(color = MaterialTheme.colorScheme.primary),
                    ShadowUnderLine(
                        alpha = 0.5f,
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Cyan,
                                Color.Transparent
                            )
                        )
                    ),
                    SelectionHighlightPopUp()
                )
            ),
        ),
        backgroundColor = MaterialTheme.colorScheme.surface,
        xAxisData = xAxisData,
        yAxisData = yAxisData,
        gridLines = GridLines(color = MaterialTheme.colorScheme.outlineVariant)
    )
    Text(
        text = angleName,
        modifier = Modifier.padding(start = 16.dp, top = 8.dp),
        style = MaterialTheme.typography.labelLarge,
        color = Color.Black
    )
    LineChart(
        modifier = Modifier
            .height(200.dp)
            .fillMaxWidth(),
        lineChartData = lineChartData
    )

    // Add the name of the angle to the chart

}

