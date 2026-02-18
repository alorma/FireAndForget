package com.alorma.fireandforget.multiplatform.settings

import com.alorma.fireandforget.CounterFireAndForget
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

  override fun getCounter(counterFireAndForget: CounterFireAndForget): Int? {
    val key = buildCounterKey(counterFireAndForget)
    return if (settings.hasKey(key)) {
      settings.getInt(key, 0)
    } else {
      null
    }
  }

  override fun setCounter(counterFireAndForget: CounterFireAndForget, value: Int) {
    settings[buildCounterKey(counterFireAndForget)] = value
  }

  private fun buildKey(fireAndForget: FireAndForget): String {
    return "fire-and-forget-${fireAndForget.name}"
  }

  private fun buildCounterKey(counterFireAndForget: CounterFireAndForget): String {
    return "fire-and-forget-counter-${counterFireAndForget.name}"
  }
}