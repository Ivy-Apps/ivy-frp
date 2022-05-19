package com.ivy.sample.demo.weather.ui

import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.runtime.Composable
import com.ivy.frp.view.FRP
import com.ivy.frp.view.navigation.Screen

data class WeatherScreen(
    val title: String
) : Screen

@Composable
fun BoxWithConstraintsScope.WeatherScreen(screen: WeatherScreen) {
    FRP<WeatherState, WeatherEvent, WeatherViewModel>(
        initialEvent = WeatherEvent.LoadWeather
    ) { state, onEvent ->
        UI(title = screen.title, state = state, onEvent = onEvent)
    }
}

@Composable
private fun UI(
    title: String,
    state: WeatherState,

    onEvent: (WeatherEvent) -> Unit
) {
    //UI goes here.....
    //When Celsius is clicked:
    // onEvent(WeatherEvent.UpdateTempUnit(TemperatureUnit.CELSIUS))
}

