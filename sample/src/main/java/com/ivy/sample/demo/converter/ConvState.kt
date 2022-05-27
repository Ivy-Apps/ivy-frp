package com.ivy.sample.demo.converter

import arrow.core.Option

data class ConvState(
    val conversion: ConvType,
    val value: Float,
    val result: Option<String>
)
