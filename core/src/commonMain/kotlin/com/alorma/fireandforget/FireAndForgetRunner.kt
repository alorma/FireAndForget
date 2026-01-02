package com.alorma.fireandforget

abstract class FireAndForgetRunner {
  fun isEnabled(fireAndForget: FireAndForget): Boolean {
    val enabled = checkEnabled(fireAndForget)
    if (enabled && fireAndForget.autoDisable) {
      disable(fireAndForget)
    }
    return enabled
  }

  protected abstract fun checkEnabled(fireAndForget: FireAndForget): Boolean
  abstract fun disable(fireAndForget: FireAndForget)
  abstract fun reset(fireAndForget: FireAndForget)
}