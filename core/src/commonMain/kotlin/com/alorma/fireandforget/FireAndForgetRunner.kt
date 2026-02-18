package com.alorma.fireandforget

abstract class FireAndForgetRunner {
  fun isEnabled(fireAndForget: FireAndForget): Boolean {
    val enabled = checkEnabled(fireAndForget)
    if (enabled && fireAndForget.autoDisable) {
      disable(fireAndForget)
    }
    return enabled
  }

  fun isEnabledCounter(counterFireAndForget: CounterFireAndForget): Boolean {
    val currentCounter = getCounter(counterFireAndForget)
    
    // Initialize counter if not set
    val counter = if (currentCounter == null) {
      setCounter(counterFireAndForget, counterFireAndForget.counter)
      counterFireAndForget.counter
    } else {
      currentCounter
    }
    
    // Return false if counter is 0 or less
    if (counter <= 0) {
      return false
    }
    
    // Decrement counter
    val newCounter = counter - 1
    setCounter(counterFireAndForget, newCounter)
    
    // Return true because we had at least one call left
    return true
  }

  protected abstract fun checkEnabled(fireAndForget: FireAndForget): Boolean
  abstract fun disable(fireAndForget: FireAndForget)
  abstract fun reset(fireAndForget: FireAndForget)
  
  protected abstract fun getCounter(counterFireAndForget: CounterFireAndForget): Int?
  protected abstract fun setCounter(counterFireAndForget: CounterFireAndForget, value: Int)
  
  fun resetCounter(counterFireAndForget: CounterFireAndForget) {
    setCounter(counterFireAndForget, counterFireAndForget.counter)
  }
}