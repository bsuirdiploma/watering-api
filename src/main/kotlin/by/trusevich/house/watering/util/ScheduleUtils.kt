package by.trusevich.house.watering.util

import java.util.concurrent.ScheduledFuture

private val SCHEDULERS_HOLDER = mutableMapOf<Long, ScheduledFuture<*>>()

fun removeScheduler(wateringId: Long) = SCHEDULERS_HOLDER[wateringId]?.let {
    it.cancel(false)

    SCHEDULERS_HOLDER.remove(wateringId)
}

fun saveScheduler(wateringId: Long, scheduledTask: ScheduledFuture<*>) =
    SCHEDULERS_HOLDER.put(wateringId, scheduledTask)