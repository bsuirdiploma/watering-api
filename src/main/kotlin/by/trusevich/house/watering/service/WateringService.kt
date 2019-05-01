package by.trusevich.house.watering.service

import by.trusevich.house.core.service.AbstractCrudService
import by.trusevich.house.core.util.lazyLogger
import by.trusevich.house.watering.model.Watering
import by.trusevich.house.watering.repository.WateringRepository
import by.trusevich.house.watering.util.removeScheduler
import by.trusevich.house.watering.util.saveScheduler
import org.apache.commons.lang3.time.DateUtils.addMinutes
import org.springframework.data.repository.findByIdOrNull
import org.springframework.scheduling.TaskScheduler
import org.springframework.scheduling.support.CronTrigger
import org.springframework.stereotype.Service
import java.util.Date
import javax.persistence.EntityNotFoundException

@Service
class WateringService(
    private val wateringRepository: WateringRepository,
    private val taskScheduler: TaskScheduler,
    private val comPortService: ComPortService
) : AbstractCrudService<Watering>(wateringRepository) {

    private val log by lazyLogger()

    override fun create(model: Watering): Watering {
        val scheduledTask =
            taskScheduler.schedule(WateringSchedule(model, taskScheduler), CronTrigger(model.wateringCron!!))!!

        return super.create(model).also {
            saveScheduler(it.id!!, scheduledTask)
        }
    }

    override fun delete(id: Long) {
        removeScheduler(id)
        super.delete(id)
    }

    fun setWateringSchedule(id: Long, cron: String, duration: Int) {
        wateringRepository.findByIdOrNull(id)?.let {
            removeScheduler(id)

            create(it.apply { wateringCron = cron; wateringDuration = duration })
        } ?: throw EntityNotFoundException()
    }

    inner class WateringSchedule(private val model: Watering, private val taskScheduler: TaskScheduler) : Runnable {
        override fun run() = model.run {
            comPortService.sendMessage(wateringPortName!!, "open")
            log.info("Opened watering point at $wateringPortName")

            val timeToStop = addMinutes(Date(), wateringDuration!!)

            taskScheduler.schedule(
                { comPortService.sendMessage(wateringPortName!!, "close") },
                timeToStop
            )
            log.info("Created scheduled job to stop watering at $timeToStop for port $wateringPortName")
        }
    }
}
