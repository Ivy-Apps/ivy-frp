package com.ivy.sample.demo.weather.ui

import com.ivy.sample.demo.weather.domain.data.TemperatureUnit

sealed class WeatherEvent {
    object LoadWeather : WeatherEvent()

    data class UpdateTempUnit(val unit: TemperatureUnit) : WeatherEvent()
}