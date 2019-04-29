package by.trusevich.house.watering.repository

import by.trusevich.house.watering.model.Watering
import by.trusevich.house.core.repository.BaseRepository

interface WateringRepository : BaseRepository<Watering> {

    fun existsByName(name: String): Boolean

    fun findByThermometerPortNameAndCollectorPinNumber(thermometerPortName: String, collectorPinNumber: Int): Watering
}
