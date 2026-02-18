package com.alorma.fireandforget

abstract class FireAndForgetRunner {
  fun isEnabled(fireAndForget: FireAndForget): Boolean {
    val enabled = checkEnabled(fireAndForget)
    if (enabled && fireAndForget.autoDisable) {
      disable(fireAndForget)
    }
    return enabled
  }

  fun isEnabledMultiple(fireAndForgetMultiple: FireAndForgetMultiple): Boolean {
    val currentCounter = getCounter(fireAndForgetMultiple)
    
    // Initialize counter if not set
    val counter = if (currentCounter == null) {
      setCounter(fireAndForgetMultiple, fireAndForgetMultiple.counter)
      fireAndForgetMultiple.counter
    } else {
      currentCounter
    }
    
    // Return false if counter is 0 or less
    if (counter <= 0) {
      return false
    }
    
    // Decrement counter
    val newCounter = counter - 1
    setCounter(fireAndForgetMultiple, newCounter)
    
    // Return true because we had at least one call left
    return true
  }

  protected abstract fun checkEnabled(fireAndForget: FireAndForget): Boolean
  abstract fun disable(fireAndForget: FireAndForget)
  abstract fun reset(fireAndForget: FireAndForget)
  
  protected abstract fun getCounter(fireAndForgetMultiple: FireAndForgetMultiple): Int?
  protected abstract fun setCounter(fireAndForgetMultiple: FireAndForgetMultiple, value: Int)
  
  fun resetCounter(fireAndForgetMultiple: FireAndForgetMultiple) {
    setCounter(fireAndForgetMultiple, fireAndForgetMultiple.counter)
  }
}