package com.alorma.fireandforget

abstract class FireAndForget(
  val fireAndForgetRunner: FireAndForgetRunner,
  val name: String,
  val defaultValue: Boolean = true,
  val autoDisable: Boolean = false,
) {
  fun isEnabled(): Boolean {
    val enabled = fireAndForgetRunner.isEnabled(this)
    if (enabled && autoDisable) {
      fireAndForgetRunner.disable(this)
    }
    return enabled
  }

  fun disable() = fireAndForgetRunner.disable(this)

  fun reset() = fireAndForgetRunner.reset(this)
}