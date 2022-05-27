package com.ivy.sample.demo.converter

sealed class ConvEvent {
    data class SetConversion(val conversion: ConvType) : ConvEvent()

    data class SetValue(val value: Float) : ConvEvent()

    object Convert : ConvEvent()
}