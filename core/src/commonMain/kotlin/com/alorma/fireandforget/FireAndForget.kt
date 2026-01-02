package com.alorma.fireandforget

abstract class FireAndForget(
  val fireAndForgetRunner: FireAndForgetRunner,
  val name: String,
  val defaultValue: Boolean = true,
) {
  fun isEnabled() = fireAndForgetRunner.isEnabled(this)

  fun disable() = fireAndForgetRunner.disable(this)

  fun reset() = fireAndForgetRunner.reset(this)
}