package com.ivy.sample.demo.weather.domain

import com.ivy.frp.action.FPAction
import com.ivy.frp.monad.Res
import com.ivy.frp.monad.mapError
import com.ivy.frp.monad.mapSuccess
import com.ivy.frp.monad.tryOp
import com.ivy.sample.demo.weather.domain.data.Temperature
import com.ivy.sample.demo.weather.domain.data.TemperatureUnit
import com.ivy.sample.demo.weather.io.WeatherService
import javax.inject.Inject

class CurrentTempAct @Inject constructor(
    private val weatherService: WeatherService,
    private val convertTempAct: ConvertTempAct
) : FPAction<TemperatureUnit, Res<String, Temperature>>() {
    override suspend fun TemperatureUnit.compose(): suspend () -> Res<String, Temperature> = tryOp(
        operation = weatherService::getWeather
    ) mapSuccess { response ->
        ConvertTempAct.Input(
            temperature = response.temperature,
            toUnit = this //TemperatureUnit
        )
    } mapSuccess convertTempAct mapError {
        "Failed to fetch weather: ${it.message}"
    }
}