package uk.gov.justice.digital.hmpps.departmentsapi.departments

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.UUID

interface DepartmentRepository : JpaRepository<DepartmentModel, UUID> {

  @Query(
    """
    select d from DepartmentModel d
    where exists (
      select 1 from EstablishmentDepartmentModel ed
      where ed.departmentId = d.id
        and ed.establishmentId = :establishmentId
        and ed.active = true
    )
    """,
  )
  fun findActiveByEstablishmentId(@Param("establishmentId") establishmentId: String): List<DepartmentModel>
}
