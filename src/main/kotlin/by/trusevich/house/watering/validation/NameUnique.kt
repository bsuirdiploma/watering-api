package by.trusevich.house.watering.validation

import by.trusevich.house.watering.repository.WateringRepository
import javax.validation.Constraint
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext
import javax.validation.Payload
import kotlin.annotation.AnnotationTarget.FIELD
import kotlin.annotation.AnnotationTarget.PROPERTY_GETTER
import kotlin.reflect.KClass

/**
 * Validates, that the watering name is unique in database
 */
@Retention
@Target(PROPERTY_GETTER, FIELD)
@MustBeDocumented
@Constraint(validatedBy = [NameUniqueValidator::class])
annotation class NameUnique(
    @Suppress("unused") val message: String = "Watering with such name already exists",
    @Suppress("unused") val groups: Array<KClass<*>> = [],
    @Suppress("unused") val payload: Array<KClass<out Payload>> = []
)

class NameUniqueValidator(private val wateringRepository: WateringRepository) : ConstraintValidator<NameUnique, String?> {

    override fun initialize(constraint: NameUnique) = Unit

    override fun isValid(name: String?, context: ConstraintValidatorContext) =
        name == null || !wateringRepository.existsByName(name)
}
