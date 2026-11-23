package uk.gov.justice.digital.hmpps.departmentsapi.repository

import org.springframework.data.jpa.repository.JpaRepository
import uk.gov.justice.digital.hmpps.departmentsapi.model.EstablishmentDepartment
import java.util.UUID

interface EstablishmentDepartmentRepository : JpaRepository<EstablishmentDepartment, UUID> {
  fun findAllByEstablishmentIdAndActiveTrue(establishmentId: String): List<EstablishmentDepartment>
}
