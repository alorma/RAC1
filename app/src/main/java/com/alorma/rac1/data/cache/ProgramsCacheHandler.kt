package com.alorma.rac1.data.cache

import com.alorma.rac1.data.TimeProvider
import com.alorma.rac1.data.TimeToLive
import com.alorma.rac1.data.net.ProgramDto
import com.alorma.rac1.domain.ProgramItem
import io.reactivex.Single
import javax.inject.Inject

class ProgramsCacheHandler @Inject constructor(
        private val timeProvider: TimeProvider,
        private val timeToLive: TimeToLive) {

    private var savedTime: Long = 0
    private val savedItems: MutableList<ProgramItem> = mutableListOf()

    fun save(items: List<ProgramItem>) {
        savedItems.clear()
        savedItems.addAll(items)
        savedTime = timeProvider.now()
    }

    fun get(): Single<List<ProgramItem>> = Single.defer {
        val items = getItems()
        if (items.isEmpty()) {
            Single.error(Exception())
        } else {
            Single.just(items)
        }
    }

    private fun getItems(): List<ProgramItem> {
        if (savedTime > 0 && !timeToLive.isValid(savedTime)) {
            savedItems.clear()
        }
        return savedItems.toList()
    }
}