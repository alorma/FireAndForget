package com.alorma.fireandforget.shared

import com.alorma.fireandforget.FireAndForget
import com.alorma.fireandforget.FireAndForgetRunner

class FireTwo(
  fireAndForgetRunner: FireAndForgetRunner,
) : FireAndForget(
  fireAndForgetRunner = fireAndForgetRunner,
  name = "fire_two",
  autoDisable = true, // Automatically disables on first access
)