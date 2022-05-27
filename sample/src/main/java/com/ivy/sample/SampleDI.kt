package com.ivy.sample

import com.ivy.frp.view.navigation.Navigation
import com.ivy.sample.demo.weather.domain.data.TemperatureUnit
import com.ivy.sample.demo.weather.domain.data.UserPreference
import com.ivy.sample.demo.weather.io.UserPreferenceService
import com.ivy.sample.demo.weather.io.WeatherService
import com.ivy.sample.demo.weather.io.data.WeatherResponse
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SampleDI {
    @Singleton
    @Provides
    fun provideNavigation(): Navigation = Navigation()

    //-------------- Weather Example --------------------
    @Provides
    fun provideUserPrefService() = object : UserPreferenceService {
        override suspend fun getTempUnit(): UserPreference {
            TODO("Not yet implemented")
        }

        override suspend fun updateTempUnit(temperatureUnit: TemperatureUnit) {
            TODO("Not yet implemented")
        }
    }

    @Provides
    fun provideWeatherService() = object : WeatherService {
        override suspend fun getWeather(): WeatherResponse {
            TODO("Not yet implemented")
        }

    }
    //-------------- Weather Example --------------------
}