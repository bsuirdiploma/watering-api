package by.trusevich.house.watering.validation

import org.springframework.scheduling.support.CronTrigger
import javax.validation.Constraint
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext
import javax.validation.Payload
import kotlin.annotation.AnnotationTarget.FIELD
import kotlin.annotation.AnnotationTarget.PROPERTY_GETTER
import kotlin.annotation.AnnotationTarget.TYPE_PARAMETER
import kotlin.annotation.AnnotationTarget.VALUE_PARAMETER
import kotlin.reflect.KClass

/**
 * Validates, that cron string is a valid cron
 */
@Retention
@Target(PROPERTY_GETTER, FIELD, VALUE_PARAMETER)
@MustBeDocumented
@Constraint(validatedBy = [CroneValidator::class])
annotation class CronValid(
    @Suppress("unused") val message: String = "Entered cron is invalid",
    @Suppress("unused") val groups: Array<KClass<*>> = [],
    @Suppress("unused") val payload: Array<KClass<out Payload>> = []
)

class CroneValidator : ConstraintValidator<CronValid, String?> {

    override fun initialize(constraint: CronValid) = Unit

    override fun isValid(cron: String?, context: ConstraintValidatorContext) = cron == null ||
            try {
                CronTrigger(cron)
                true
            } catch (e: Exception) {
                false
            }
}
