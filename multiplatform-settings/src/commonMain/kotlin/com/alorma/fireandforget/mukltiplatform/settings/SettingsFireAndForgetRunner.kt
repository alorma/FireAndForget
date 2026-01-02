package com.alorma.fireandforget.mukltiplatform.settings

import com.alorma.fireandforget.FireAndForget
import com.alorma.fireandforget.FireAndForgetRunner
import com.russhwolf.settings.Settings
import com.russhwolf.settings.set

class SettingsFireAndForgetRunner(
  val settings: Settings,
) : FireAndForgetRunner() {

  override fun checkEnabled(fireAndForget: FireAndForget): Boolean {
    return settings.getBoolean(
      key = buildKey(fireAndForget = fireAndForget),
      defaultValue = fireAndForget.defaultValue,
    )
  }

  override fun disable(fireAndForget: FireAndForget) {
    settings[buildKey(fireAndForget)] = false
  }

  override fun reset(fireAndForget: FireAndForget) {
    settings.remove(buildKey(fireAndForget))
  }

  private fun buildKey(fireAndForget: FireAndForget): String {
    return "fire-and-forget-${fireAndForget.name}"
  }
}