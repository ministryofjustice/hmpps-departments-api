package uk.gov.justice.digital.hmpps.departmentsapi.departments

import java.util.UUID

data class DepartmentResponse(
  val id: UUID,
  val name: String,
  val slug: String,
)
