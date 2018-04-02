package com.alorma.rac1.data

class SystemTimeProvider : TimeProvider {
    override fun now(): Long = System.currentTimeMillis()
}
