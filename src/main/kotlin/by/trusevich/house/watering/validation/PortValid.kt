package by.trusevich.house.watering.validation

import by.trusevich.house.watering.service.ComPortService
import by.trusevich.house.core.util.lazyLogger
import javax.validation.Constraint
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext
import javax.validation.Payload
import kotlin.annotation.AnnotationTarget.FIELD
import kotlin.annotation.AnnotationTarget.PROPERTY_GETTER
import kotlin.reflect.KClass

/**
 * Validates, that the watering zone name is unique in database
 */
@Retention
@Target(PROPERTY_GETTER, FIELD)
@MustBeDocumented
@Constraint(validatedBy = [PortValidator::class])
annotation class PortValid(
    @Suppress("unused") val message: String = "Port is not available",
    @Suppress("unused") val groups: Array<KClass<*>> = [],
    @Suppress("unused") val payload: Array<KClass<out Payload>> = []
)

class PortValidator(private val comPortService: ComPortService) : ConstraintValidator<PortValid, String?> {

    private val log by lazyLogger()

    override fun initialize(constraint: PortValid) = Unit

    override fun isValid(name: String?, context: ConstraintValidatorContext) =
        name == null || try {
            comPortService.disconnectFromPort(comPortService.connectToPort(name))
        } catch (e: Exception) {
            log.warn("Unable to connect to port $name", e)
            false
        }
}
