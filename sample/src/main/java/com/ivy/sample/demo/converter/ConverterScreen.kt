package com.ivy.sample.demo.converter

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import arrow.core.None
import arrow.core.Some
import com.google.accompanist.insets.systemBarsPadding
import com.ivy.frp.view.FRP
import com.ivy.frp.view.navigation.Screen

object ConverterScreen : Screen

@Composable
fun BoxWithConstraintsScope.ConverterScreen(screen: ConverterScreen) {
    FRP<ConvState, ConvEvent, ConverterViewModel> { state, onEvent ->
        UI(state, onEvent)
    }
}

@Composable
private fun UI(
    state: ConvState,

    onEvent: (ConvEvent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
            .systemBarsPadding()
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        Row {
            Button(
                modifier = Modifier.background(
                    if (state.conversion == ConvType.METERS_TO_FEET) Color.Yellow else Color.Blue
                ),
                onClick = {
                    onEvent(ConvEvent.SetConversion(ConvType.METERS_TO_FEET))
                }
            ) {
                Text(text = "METERS TO FEET")
            }

            Spacer(Modifier.width(24.dp))

            Button(
                modifier = Modifier.background(
                    if (state.conversion == ConvType.FEET_TO_METERS) Color.Yellow else Color.Blue
                ),
                onClick = { onEvent(ConvEvent.SetConversion(ConvType.FEET_TO_METERS)) }
            ) {
                Text(text = "FEET TO METERS")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = 48.dp)
                .background(Color.Gray),
            value = state.value.toString(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            onValueChange = { input ->
                input.toFloatOrNull()?.let { onEvent(ConvEvent.SetValue(it)) }
            }
        )

        Spacer(Modifier.height(16.dp))

        Button(onClick = { onEvent(ConvEvent.Convert) }) {
            Text(text = "Convert")
        }

        Spacer(Modifier.height(32.dp))

        when (val result = state.result) {
            None -> {}
            is Some -> Text(
                text = "Result: ${result.value}${
                    when (state.conversion) {
                        ConvType.METERS_TO_FEET -> "ft"
                        ConvType.FEET_TO_METERS -> "m"
                    }
                }",
                style = TextStyle(
                    color = Color.Yellow,
                    fontSize = 18.sp
                )
            )
        }

        Spacer(modifier = Modifier.weight(1f))
    }
}

@Preview
@Composable
private fun Preview() {
    UI(
        state = ConvState(
            conversion = ConvType.METERS_TO_FEET,
            value = 1f,
            result = Some("3.28")
        ),
        onEvent = {}
    )
}