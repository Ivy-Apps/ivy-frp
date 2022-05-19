package com.ivy.sample.demo.weather.io

import com.ivy.sample.demo.weather.domain.data.TemperatureUnit
import com.ivy.sample.demo.weather.domain.data.UserPreference
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface UserPreferenceService {
    @GET("/preference/temp-unit")
    suspend fun getTempUnit(): UserPreference

    @POST("/preference/temp-unit")
    suspend fun updateTempUnit(
        @Query("unit") temperatureUnit: TemperatureUnit
    )
}