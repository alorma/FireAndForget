package com.alorma.fireandforget.shared

import com.alorma.fireandforget.FireAndForget
import com.alorma.fireandforget.FireAndForgetMultiple
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

  override fun getCounter(fireAndForgetMultiple: FireAndForgetMultiple): Int? {
    return counterMap[fireAndForgetMultiple.name]
  }

  override fun setCounter(fireAndForgetMultiple: FireAndForgetMultiple, value: Int) {
    counterMap[fireAndForgetMultiple.name] = value
  }
}