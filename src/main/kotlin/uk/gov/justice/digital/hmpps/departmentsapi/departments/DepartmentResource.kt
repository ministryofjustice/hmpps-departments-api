package uk.gov.justice.digital.hmpps.departmentsapi.departments

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.MediaType
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import uk.gov.justice.hmpps.kotlin.common.ErrorResponse

@RestController
@RequestMapping("/departments", produces = [MediaType.APPLICATION_JSON_VALUE])
class DepartmentResource(private val service: DepartmentService) {

  @Tag(name = "Establishment departments")
  @Operation(
    summary = "Get active departments for an establishment",
    description = "Returns the active departments for the given establishment. Requires either the `ROLE_DEPARTMENTS__DEPARTMENTS_RW` or `ROLE_DEPARTMENTS__DEPARTMENTS_RO` role.",
    security = [SecurityRequirement(name = "bearerAuth")],
    responses = [
      ApiResponse(
        responseCode = "200",
        description = "Active departments returned successfully",
        content = [
          Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            array = ArraySchema(schema = Schema(implementation = DepartmentResponse::class)),
          ),
        ],
      ),
      ApiResponse(
        responseCode = "401",
        description = "Unauthorized to access this endpoint",
        content = [Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = Schema(implementation = ErrorResponse::class))],
      ),
      ApiResponse(
        responseCode = "403",
        description = "Forbidden to access this endpoint. The token does not have either the `ROLE_DEPARTMENTS__DEPARTMENTS_RW` or `ROLE_DEPARTMENTS__DEPARTMENTS_RO` role.",
        content = [Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = Schema(implementation = ErrorResponse::class))],
      ),
    ],
  )
  @GetMapping("/activeAt/{establishmentCode}")
  @PreAuthorize("hasAnyAuthority('ROLE_DEPARTMENTS__DEPARTMENTS_RW', 'ROLE_DEPARTMENTS__DEPARTMENTS_RO')")
  fun getActiveDepartments(
    @PathVariable("establishmentCode") establishmentCode: String,
  ): List<DepartmentResponse> = service.getActiveDepartments(establishmentCode)
}
