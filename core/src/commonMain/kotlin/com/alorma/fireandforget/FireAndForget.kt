package com.alorma.fireandforget

open class FireAndForget(
  val fireAndForgetRunner: FireAndForgetRunner,
  val name: String,
  val defaultValue: Boolean = true,
  val autoDisable: Boolean = false,
) {
  open fun isEnabled() = fireAndForgetRunner.isEnabled(this)

  open fun disable() = fireAndForgetRunner.disable(this)

  open fun reset() = fireAndForgetRunner.reset(this)
}