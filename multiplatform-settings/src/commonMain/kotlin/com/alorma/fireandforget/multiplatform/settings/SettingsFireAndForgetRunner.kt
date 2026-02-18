package com.alorma.fireandforget.multiplatform.settings

import com.alorma.fireandforget.FireAndForget
import com.alorma.fireandforget.FireAndForgetMultiple
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

  override fun getCounter(fireAndForgetMultiple: FireAndForgetMultiple): Int? {
    val key = buildCounterKey(fireAndForgetMultiple)
    return if (settings.hasKey(key)) {
      settings.getInt(key, 0)
    } else {
      null
    }
  }

  override fun setCounter(fireAndForgetMultiple: FireAndForgetMultiple, value: Int) {
    settings[buildCounterKey(fireAndForgetMultiple)] = value
  }

  private fun buildKey(fireAndForget: FireAndForget): String {
    return "fire-and-forget-${fireAndForget.name}"
  }

  private fun buildCounterKey(fireAndForgetMultiple: FireAndForgetMultiple): String {
    return "fire-and-forget-counter-${fireAndForgetMultiple.name}"
  }
}