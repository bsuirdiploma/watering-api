package by.trusevich.house.watering.controller

import by.trusevich.house.core.annotation.TokenHeaderImplicit
import by.trusevich.house.core.exception.EntityNoContentException.Companion.NO_CONTENT_REASON
import by.trusevich.house.core.exception.EntityNotFoundException.Companion.NOT_FOUND_REASON
import by.trusevich.house.core.exception.MalformedRequestDataException.Companion.MALFORMED_REASON
import by.trusevich.house.core.exception.UnauthorizedException.Companion.UNAUTHORIZED_REASON
import by.trusevich.house.core.exception.model.ErrorDetails
import by.trusevich.house.core.util.SC_UNPROCESSABLE_ENTITY
import by.trusevich.house.core.util.VALIDATION_REASON
import by.trusevich.house.watering.model.Watering
import by.trusevich.house.watering.service.WateringService
import by.trusevich.house.watering.validation.CronValid
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.hibernate.validator.constraints.Range
import org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod.DELETE
import org.springframework.web.bind.annotation.RequestMethod.GET
import org.springframework.web.bind.annotation.RequestMethod.PATCH
import org.springframework.web.bind.annotation.RequestMethod.POST
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST
import javax.servlet.http.HttpServletResponse.SC_NOT_FOUND
import javax.servlet.http.HttpServletResponse.SC_NO_CONTENT
import javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED
import javax.validation.Valid
import javax.validation.constraints.Min

@RestController
@RequestMapping("/watering")
@Api("Watering Management", description = "Endpoints for managing watering")
@CrossOrigin(origins = ["*"], allowedHeaders = ["*"], methods = [GET, POST, DELETE, PATCH])
class WateringController(private val wateringService: WateringService) {

    @TokenHeaderImplicit
    @ApiOperation("Create watering", notes = "Creates a new watering")
    @ApiResponses(
        ApiResponse(code = SC_UNAUTHORIZED, message = UNAUTHORIZED_REASON, response = ErrorDetails::class),
        ApiResponse(code = SC_UNPROCESSABLE_ENTITY, message = VALIDATION_REASON, response = ErrorDetails::class),
        ApiResponse(code = SC_BAD_REQUEST, message = MALFORMED_REASON, response = ErrorDetails::class)
    )
    @PostMapping(consumes = [APPLICATION_JSON_UTF8_VALUE], produces = [APPLICATION_JSON_UTF8_VALUE])
    fun createWatering(@Valid @ApiParam(hidden = true) @RequestBody watering: Watering) =
        wateringService.create(watering)

    @TokenHeaderImplicit
    @ApiOperation("Get watering by id", notes = "Gets watering by id")
    @ApiResponses(
        ApiResponse(code = SC_UNAUTHORIZED, message = UNAUTHORIZED_REASON, response = ErrorDetails::class),
        ApiResponse(code = SC_NO_CONTENT, message = NO_CONTENT_REASON, response = ErrorDetails::class)
    )
    @GetMapping("/{id}", produces = [APPLICATION_JSON_UTF8_VALUE])
    fun getWateringById(@ApiParam("10") @PathVariable id: Long) = wateringService.find(id)

    @TokenHeaderImplicit
    @ApiOperation("Delete watering by id", notes = "Deletes watering from database.")
    @ApiResponses(
        ApiResponse(code = SC_UNAUTHORIZED, message = UNAUTHORIZED_REASON, response = ErrorDetails::class),
        ApiResponse(code = SC_NOT_FOUND, message = NOT_FOUND_REASON, response = ErrorDetails::class)
    )
    @DeleteMapping("/{id}", produces = [APPLICATION_JSON_UTF8_VALUE])
    fun deleteWateringById(@ApiParam("10") @PathVariable id: Long) = wateringService.delete(id)

    @TokenHeaderImplicit
    @ApiOperation(
        "Find all watering",
        notes = "Retrieves a list of Watering (supports pagination and ordering)"
    )
    @ApiResponses(
        ApiResponse(code = SC_UNAUTHORIZED, message = UNAUTHORIZED_REASON, response = ErrorDetails::class)
    )
    @GetMapping(produces = [APPLICATION_JSON_UTF8_VALUE])
    fun findAllWaterings(
        @Valid @Min(0L, message = "Page number should not be negative")
        @ApiParam("0") @RequestParam(required = false, defaultValue = "0") page: Int,
        @Valid @Range(min = 0L, max = 30L, message = "Page limit should not be  from 0 to 30")
        @ApiParam("0") @RequestParam(required = false, defaultValue = "\${house.defaults.page.size}") limit: Int
    ) = wateringService.findAll(page, limit)

    @TokenHeaderImplicit
    @ApiOperation("Set cron and duration of watering", notes = "Sets cron and duration of watering")
    @ApiResponses(ApiResponse(code = SC_UNAUTHORIZED, message = UNAUTHORIZED_REASON, response = ErrorDetails::class))
    @PatchMapping("/{id}", produces = [APPLICATION_JSON_UTF8_VALUE])
    fun setWateringSchedule(
        @PathVariable id: Long,
        @Valid @CronValid @RequestParam cron: String,
        @Valid @Range(min = 1, max = 60) @RequestParam duration: Int
    ) = wateringService.setWateringSchedule(id, cron, duration)
}
