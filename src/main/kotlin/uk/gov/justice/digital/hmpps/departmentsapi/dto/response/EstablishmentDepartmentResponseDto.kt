package uk.gov.justice.digital.hmpps.departmentsapi.dto.response

import java.util.UUID

data class EstablishmentDepartmentResponseDto(
  val id: UUID,
  val establishmentId: String,
  val departmentId: UUID?,
  val active: Boolean,
)
