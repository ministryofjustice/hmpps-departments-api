package uk.gov.justice.digital.hmpps.departmentsapi.departments

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "department")
data class DepartmentModel(
  @Id
  @Column(name = "id")
  val id: UUID,

  @Column(name = "name", nullable = false)
  val name: String,

  @Column(name = "slug", nullable = false)
  val slug: String,
)
