package com.alorma.fireandforget.shared

import com.alorma.fireandforget.FireAndForgetMultiple
import com.alorma.fireandforget.FireAndForgetRunner

class FireMultiple(
  fireAndForgetRunner: FireAndForgetRunner,
  counter: Int = 3,
) : FireAndForgetMultiple(
  fireAndForgetRunner = fireAndForgetRunner,
  name = "fire_multiple",
  counter = counter,
)
