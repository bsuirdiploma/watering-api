package by.trusevich.house.watering.util

import java.util.concurrent.ScheduledFuture
import javax.persistence.EntityNotFoundException

private val SCHEDULERS_HOLDER = mutableMapOf<Long, ScheduledFuture<*>>()

fun removeScheduler(wateringId: Long) = SCHEDULERS_HOLDER[wateringId]?.let {
    it.cancel(false)

    SCHEDULERS_HOLDER.remove(wateringId)

} ?: throw EntityNotFoundException()

fun saveScheduler(wateringId: Long, scheduledTask: ScheduledFuture<*>) =
    SCHEDULERS_HOLDER.put(wateringId, scheduledTask)