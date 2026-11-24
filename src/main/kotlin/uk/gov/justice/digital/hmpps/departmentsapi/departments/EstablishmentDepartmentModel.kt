package uk.gov.justice.digital.hmpps.departmentsapi.departments

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "establishment_departments")
data class EstablishmentDepartmentModel(
  @Id
  @Column(name = "id")
  val id: UUID,

  @Column(name = "department_id")
  val departmentId: UUID?,

  @Column(name = "establishment_id", nullable = false)
  val establishmentId: String,

  @Column(name = "active", nullable = false)
  val active: Boolean,
)
