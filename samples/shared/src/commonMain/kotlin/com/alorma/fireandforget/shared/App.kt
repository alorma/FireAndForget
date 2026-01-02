package com.alorma.fireandforget.shared

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.alorma.fireandforget.FireAndForget
import com.alorma.fireandforget.mukltiplatform.settings.SettingsFireAndForgetRunner
import com.russhwolf.settings.Settings

@Composable
fun App() {
  MaterialTheme {
    val settings = Settings()
    val runner = SettingsFireAndForgetRunner(settings)

    val fireOne = FireOne(runner)
    val fireTwo = FireTwo(runner)

    Scaffold(
      modifier = Modifier.fillMaxSize(),
    ) { paddingValues ->
      Column(
        modifier = Modifier.padding(paddingValues),
        verticalArrangement = Arrangement.spacedBy(16.dp),
      ) {
        FireAndForgetSampleRow(fireOne)
        FireAndForgetSampleRow(fireTwo)
      }
    }
  }
}

@Composable
fun FireAndForgetSampleRow(
  fireAndForget: FireAndForget,
  modifier: Modifier = Modifier,
) {
  Row(
    modifier = modifier,
    verticalAlignment = Alignment.CenterVertically,
  ) {
    Text(
      text = fireAndForget.name,
    )

    Switch(
      modifier = Modifier.weight(1f),
      checked = fireAndForget.isEnabled(),
      onCheckedChange = { state ->
        if (!state) {
          fireAndForget.disable()
        }
      },
    )
  }
}
