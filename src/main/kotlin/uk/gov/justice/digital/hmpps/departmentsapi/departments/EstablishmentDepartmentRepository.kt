package uk.gov.justice.digital.hmpps.departmentsapi.departments

import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface EstablishmentDepartmentRepository : JpaRepository<EstablishmentDepartmentModel, UUID>
