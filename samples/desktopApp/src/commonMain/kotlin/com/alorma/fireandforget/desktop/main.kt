package com.alorma.fireandforget.desktop

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.alorma.fireandforget.shared.App

fun main() = application {
  Window(
    onCloseRequest = ::exitApplication,
    title = "Fire and Forget Desktop"
  ) {
    App()
  }
}