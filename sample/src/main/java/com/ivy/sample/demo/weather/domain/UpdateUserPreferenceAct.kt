package com.ivy.sample.demo.weather.domain

import com.ivy.frp.action.FPAction
import com.ivy.frp.asParamTo
import com.ivy.frp.monad.Res
import com.ivy.frp.monad.mapError
import com.ivy.frp.monad.tryOp
import com.ivy.sample.demo.weather.domain.data.TemperatureUnit
import com.ivy.sample.demo.weather.io.UserPreferenceService
import javax.inject.Inject

class UpdateUserPreferenceAct @Inject constructor(
    private val userPreferenceService: UserPreferenceService
) : FPAction<TemperatureUnit, Res<String, Unit>>() {
    override suspend fun TemperatureUnit.compose(): suspend () -> Res<String, Unit> = tryOp(
        operation = this asParamTo userPreferenceService::updateTempUnit
    ) mapError {
        "Failed to update user preference: ${it.message}"
    }
}