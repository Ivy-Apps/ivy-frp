package com.ivy.sample.demo.converter

import arrow.core.None
import arrow.core.Some
import com.ivy.frp.asParamTo
import com.ivy.frp.then
import com.ivy.frp.thenInvokeAfter
import com.ivy.frp.viewmodel.FRPViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import java.text.DecimalFormat
import javax.inject.Inject

@HiltViewModel
class ConverterViewModel @Inject constructor() : FRPViewModel<ConvState, ConvEvent>() {
    companion object {
        const val METER_FEET_CONST = 3.28084f
    }

    //set initial state
    override val _state: MutableStateFlow<ConvState> = MutableStateFlow(
        ConvState(
            conversion = ConvType.METER_TO_FEET,
            value = 1f,
            result = None
        )
    )

    override suspend fun handleEvent(event: ConvEvent): suspend () -> ConvState = when (event) {
        is ConvEvent.SetConversion -> event asParamTo ::setConversion then ::convert
        is ConvEvent.SetValue -> event asParamTo ::setValue
        is ConvEvent.Convert -> stateVal() asParamTo ::convert
    }

    private suspend fun setConversion(event: ConvEvent.SetConversion) =
        updateState { it.copy(conversion = event.conversion) }

    private suspend fun setValue(event: ConvEvent.SetValue) =
        updateState { it.copy(value = event.value) }

    private suspend fun convert(
        state: ConvState
    ) = state.value asParamTo when (stateVal().conversion) {
        ConvType.METER_TO_FEET -> ::convertMeterToFeet
        ConvType.FEET_TO_METER -> ::convertFeetToMeter
    } then ::formatResult thenInvokeAfter { result ->
        updateState { it.copy(result = Some(result)) }
    }

    private fun convertMeterToFeet(meters: Float): Float = meters * METER_FEET_CONST
    private fun convertFeetToMeter(ft: Float): Float = ft / METER_FEET_CONST

    private fun formatResult(result: Float): String =
        DecimalFormat("###,###.##").format(result)
}