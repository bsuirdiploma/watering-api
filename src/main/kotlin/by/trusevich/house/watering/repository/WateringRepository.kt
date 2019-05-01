package by.trusevich.house.watering.repository

import by.trusevich.house.core.repository.BaseRepository
import by.trusevich.house.watering.model.Watering

interface WateringRepository : BaseRepository<Watering> {

    fun existsByName(name: String): Boolean
}
