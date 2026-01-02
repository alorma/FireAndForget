package com.alorma.fireandforget

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform