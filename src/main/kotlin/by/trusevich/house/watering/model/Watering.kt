package by.trusevich.house.watering.model

import by.trusevich.house.watering.validation.PortValid
import by.trusevich.house.core.model.BaseEntity
import by.trusevich.house.watering.validation.CronValid
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY
import org.hibernate.envers.Audited
import org.hibernate.validator.constraints.Length
import org.hibernate.validator.constraints.Range
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table
import javax.persistence.UniqueConstraint
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Entity
@Audited
@JsonInclude(NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@Table(uniqueConstraints = [UniqueConstraint(name = "unique_name", columnNames = ["name"])])
data class Watering(

    @get:NotBlank
    @get:Length(max = 255)
    @Column(nullable = false, updatable = false)
    var name: String? = null,

    @get:NotBlank
    @get:Length(max = 255)
    @Column(nullable = false, updatable = false)
    var location: String? = null,

    @get:PortValid
    @get:NotBlank
    @get:Length(max = 255)
    @Column(nullable = false, updatable = false)
    var wateringPortName: String? = null,

    @get:NotBlank
    @get:CronValid
    @Column(nullable = false)
    var wateringCron: String? = null,

    @get:NotNull
    @Range(min = 1, max = 60)
    @Column(nullable = false)
    var wateringDuration: Int? = null

) : BaseEntity() {

    companion object {

        private const val serialVersionUID = 79835162396908742L
    }
}
