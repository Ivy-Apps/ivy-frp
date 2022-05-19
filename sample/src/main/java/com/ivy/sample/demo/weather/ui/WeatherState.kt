package com.ivy.sample.demo.weather.ui

import com.ivy.sample.demo.weather.domain.data.TemperatureUnit

sealed class WeatherState {
    object Loading : WeatherState()

    data class Error(val errReason: String) : WeatherState()

    data class Success(
        val tempUnit: TemperatureUnit,
        val temp: Float
    ) : WeatherState()
}

