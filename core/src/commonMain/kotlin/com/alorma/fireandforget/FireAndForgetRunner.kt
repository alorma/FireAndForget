package com.alorma.fireandforget

abstract class FireAndForgetRunner {
  abstract fun isEnabled(fireAndForget: FireAndForget): Boolean
  abstract fun disable(fireAndForget: FireAndForget)
  abstract fun reset(fireAndForget: FireAndForget)
}