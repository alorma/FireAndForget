package com.alorma.fireandforget.shared

import com.alorma.fireandforget.CounterFireAndForget
import com.alorma.fireandforget.FireAndForget
import com.alorma.fireandforget.FireAndForgetRunner

class InMemoryFireAndForgetRunner: FireAndForgetRunner() {

  val map: MutableMap<String, Boolean> = mutableMapOf()
  val counterMap: MutableMap<String, Int> = mutableMapOf()

  override fun checkEnabled(fireAndForget: FireAndForget): Boolean {
    return map[fireAndForget.name] ?: fireAndForget.defaultValue
  }

  override fun disable(fireAndForget: FireAndForget) {
    map[fireAndForget.name] = false
  }

  override fun reset(fireAndForget: FireAndForget) {
    map.remove(fireAndForget.name)
  }

  override fun getCounter(counterFireAndForget: CounterFireAndForget): Int? {
    return counterMap[counterFireAndForget.name]
  }

  override fun setCounter(counterFireAndForget: CounterFireAndForget, value: Int) {
    counterMap[counterFireAndForget.name] = value
  }
}