package com.ivy.sample.demo.weather.io

import com.ivy.sample.demo.weather.io.data.WeatherResponse
import retrofit2.http.GET

interface WeatherService {
    @GET("/api/weather")
    suspend fun getWeather(): WeatherResponse
}