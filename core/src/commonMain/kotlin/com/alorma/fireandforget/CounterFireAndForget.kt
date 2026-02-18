package com.alorma.fireandforget

open class CounterFireAndForget(
  fireAndForgetRunner: FireAndForgetRunner,
  name: String,
  val counter: Int,
) : FireAndForget(
  fireAndForgetRunner = fireAndForgetRunner,
  name = name,
  defaultValue = true,
  autoDisable = false,
) {
  override fun isEnabled(): Boolean {
    return fireAndForgetRunner.isEnabledCounter(this)
  }

  override fun reset() {
    fireAndForgetRunner.resetCounter(this)
  }
}
