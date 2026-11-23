package uk.gov.justice.digital.hmpps.departmentsapi.service

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.departmentsapi.dto.response.EstablishmentDepartmentResponseDto
import uk.gov.justice.digital.hmpps.departmentsapi.model.EstablishmentDepartment
import uk.gov.justice.digital.hmpps.departmentsapi.repository.EstablishmentDepartmentRepository

@Service
class EstablishmentDepartmentService(private val repository: EstablishmentDepartmentRepository) {

  fun getActiveDepartments(establishmentId: String): List<EstablishmentDepartmentResponseDto> = repository
    .findAllByEstablishmentIdAndActiveTrue(establishmentId)
    .map { it.toDto() }
}

private fun EstablishmentDepartment.toDto() = EstablishmentDepartmentResponseDto(
  id = id,
  establishmentId = establishmentId,
  departmentId = departmentId,
  active = active,
)
