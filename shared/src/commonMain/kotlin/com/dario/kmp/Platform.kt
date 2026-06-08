package com.dario.kmp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform