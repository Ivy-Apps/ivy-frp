package com.ivy.sample.demo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.BoxWithConstraints
import com.ivy.frp.view.navigation.Navigation
import com.ivy.frp.view.navigation.NavigationRoot
import com.ivy.frp.view.navigation.onScreenStart
import com.ivy.sample.demo.converter.ConverterScreen
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RootActivity : ComponentActivity() {

    @Inject
    lateinit var navigation: Navigation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            BoxWithConstraints {
                NavigationRoot(navigation = navigation) { screen ->
                    when (screen) {
                        is ConverterScreen -> ConverterScreen(screen = screen)
                    }

                    onScreenStart {
                        navigation.navigateTo(ConverterScreen)
                    }
                }
            }
        }
    }
}