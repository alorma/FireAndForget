package com.alorma.fireandforget.shared

import com.alorma.fireandforget.CounterFireAndForget
import com.alorma.fireandforget.FireAndForgetRunner

class FireMultiple(
  fireAndForgetRunner: FireAndForgetRunner,
  counter: Int = 3,
) : CounterFireAndForget(
  fireAndForgetRunner = fireAndForgetRunner,
  name = "fire_multiple",
  counter = counter,
)
