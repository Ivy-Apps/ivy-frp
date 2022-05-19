package com.ivy.sample.demo.weather.domain

import com.ivy.frp.action.FPAction
import com.ivy.frp.asParamTo
import com.ivy.frp.then
import com.ivy.sample.demo.weather.domain.data.Temperature
import com.ivy.sample.demo.weather.domain.data.TemperatureUnit
import javax.inject.Inject

class ConvertTempAct @Inject constructor() : FPAction<ConvertTempAct.Input, Temperature>() {

    override suspend fun Input.compose(): suspend () -> Temperature =
        this asParamTo ::convertValue then { convertedValue ->
            Temperature(convertedValue, toUnit)
        }

    private fun convertValue(input: Input): Float = with(input.temperature) {
        if (unit == input.toUnit) value else {
            when (input.toUnit) {
                TemperatureUnit.CELSIUS -> fahrenheitToCelsius(value)
                TemperatureUnit.FAHRENHEIT -> celsiusToFahrenheit(value)
            }
        }
    }

    //Y°F = (X°C × 9/5) + 32
    private fun celsiusToFahrenheit(celsius: Float): Float = (celsius * 9 / 5) + 32

    //X°C = (Y°F - 32) / (9/5)
    private fun fahrenheitToCelsius(fahrenheit: Float): Float = (fahrenheit - 32) / (9 / 5)

    data class Input(
        val temperature: Temperature,
        val toUnit: TemperatureUnit
    )
}