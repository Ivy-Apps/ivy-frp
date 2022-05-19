package com.ivy.sample.demo.weather.ui

import com.ivy.frp.lambda
import com.ivy.frp.monad.Res
import com.ivy.frp.monad.mapSuccess
import com.ivy.frp.monad.thenIfSuccess
import com.ivy.frp.then
import com.ivy.frp.thenInvokeAfter
import com.ivy.frp.viewmodel.FRPViewModel
import com.ivy.sample.demo.weather.domain.CurrentTempAct
import com.ivy.sample.demo.weather.domain.UpdateUserPreferenceAct
import com.ivy.sample.demo.weather.domain.UserPreferenceAct
import com.ivy.sample.demo.weather.domain.data.TemperatureUnit
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val userPreferenceAct: UserPreferenceAct,
    private val updateUserPreferenceAct: UpdateUserPreferenceAct,
    private val currentTempAct: CurrentTempAct,
) : FRPViewModel<WeatherState, WeatherEvent>() {
    override val _state: MutableStateFlow<WeatherState> = MutableStateFlow(WeatherState.Loading)

    override suspend fun handleEvent(event: WeatherEvent): suspend () -> WeatherState =
        when (event) {
            is WeatherEvent.LoadWeather -> loadWeather()
            is WeatherEvent.UpdateTempUnit -> updateTempUnit(event)
        }

    private suspend fun loadWeather(): suspend () -> WeatherState = suspend {
        updateState { WeatherState.Loading }
        Unit
    } then userPreferenceAct mapSuccess {
        it.temperatureUnit //CurrentTempAct expects TemperatureUnit
    } then ::loadTempFor

    private suspend fun updateTempUnit(
        event: WeatherEvent.UpdateTempUnit
    ): suspend () -> WeatherState = suspend {
        updateState { WeatherState.Loading }
        event.unit
    } then updateUserPreferenceAct mapSuccess {
        event.unit
    } then ::loadTempFor

    private suspend fun loadTempFor(tempRes: Res<String, TemperatureUnit>) =
        tempRes.lambda() thenIfSuccess currentTempAct thenInvokeAfter {
            when (it) {
                is Res.Ok -> WeatherState.Success(
                    temp = it.data.value,
                    tempUnit = it.data.unit
                )
                is Res.Err -> WeatherState.Error(errReason = it.error)
            }
        }
}