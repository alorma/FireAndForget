package com.alorma.fireandforget.shared

import com.alorma.fireandforget.FireAndForget
import com.alorma.fireandforget.FireAndForgetRunner

class InMemoryFireAndForgetRunner: FireAndForgetRunner() {

  val map: MutableMap<String, Boolean> = mutableMapOf()

  override fun isEnabled(fireAndForget: FireAndForget): Boolean {
    return map[fireAndForget.name] ?: fireAndForget.defaultValue
  }

  override fun disable(fireAndForget: FireAndForget) {
    map[fireAndForget.name] = false
  }

  override fun reset(fireAndForget: FireAndForget) {
    map.remove(fireAndForget.name)
  }
}