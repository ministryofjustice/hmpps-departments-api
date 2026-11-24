package uk.gov.justice.digital.hmpps.departmentsapi.departments

import org.springframework.stereotype.Service

@Service
class DepartmentService(private val repository: DepartmentRepository) {

  fun getActiveDepartments(establishmentId: String): List<DepartmentResponse> = repository
    .findActiveByEstablishmentId(establishmentId)
    .map { it.toDto() }
}

private fun DepartmentModel.toDto() = DepartmentResponse(
  id = id,
  name = name,
  slug = slug,
)
