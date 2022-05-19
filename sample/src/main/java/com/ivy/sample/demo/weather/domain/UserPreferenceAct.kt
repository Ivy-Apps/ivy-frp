package com.ivy.sample.demo.weather.domain

import com.ivy.frp.action.FPAction
import com.ivy.frp.monad.Res
import com.ivy.frp.monad.mapError
import com.ivy.frp.monad.tryOp
import com.ivy.sample.demo.weather.domain.data.UserPreference
import com.ivy.sample.demo.weather.io.UserPreferenceService
import javax.inject.Inject

class UserPreferenceAct @Inject constructor(
    private val userPreferenceService: UserPreferenceService
) : FPAction<Unit, Res<String, UserPreference>>() {
    override suspend fun Unit.compose(): suspend () -> Res<String, UserPreference> = tryOp(
        operation = userPreferenceService::getTempUnit
    ) mapError {
        "Failed to fetch user's preference: ${it.message}"
    }
}