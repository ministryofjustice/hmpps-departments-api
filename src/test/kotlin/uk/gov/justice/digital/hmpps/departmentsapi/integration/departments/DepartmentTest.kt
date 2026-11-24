package uk.gov.justice.digital.hmpps.departmentsapi.integration.departments

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import uk.gov.justice.digital.hmpps.departmentsapi.departments.DepartmentModel
import uk.gov.justice.digital.hmpps.departmentsapi.departments.DepartmentRepository
import uk.gov.justice.digital.hmpps.departmentsapi.departments.EstablishmentDepartmentModel
import uk.gov.justice.digital.hmpps.departmentsapi.departments.EstablishmentDepartmentRepository
import uk.gov.justice.digital.hmpps.departmentsapi.integration.IntegrationTestBase
import java.util.UUID

class DepartmentTest : IntegrationTestBase() {

  @Autowired
  private lateinit var departmentRepository: DepartmentRepository

  @Autowired
  private lateinit var establishmentDepartmentRepository: EstablishmentDepartmentRepository

  @BeforeEach
  fun setUp() {
    establishmentDepartmentRepository.deleteAll()
    departmentRepository.deleteAll()
  }

  @Test
  fun `returns only departments actively linked to the requested establishment`() {
    val activeDepartment = departmentRepository.save(DepartmentModel(UUID.randomUUID(), "Safer Custody", "safer-custody"))
    val inactiveDepartment = departmentRepository.save(DepartmentModel(UUID.randomUUID(), "Reception", "reception"))
    val otherEstablishmentDepartment = departmentRepository.save(DepartmentModel(UUID.randomUUID(), "Healthcare", "healthcare"))
    establishmentDepartmentRepository.save(EstablishmentDepartmentModel(UUID.randomUUID(), activeDepartment.id, "MDI", true))
    establishmentDepartmentRepository.save(EstablishmentDepartmentModel(UUID.randomUUID(), inactiveDepartment.id, "MDI", false))
    establishmentDepartmentRepository.save(EstablishmentDepartmentModel(UUID.randomUUID(), otherEstablishmentDepartment.id, "LEI", true))

    webTestClient.get().uri("/departments/activeAt/MDI")
      .headers(setAuthorisation(scopes = listOf(), roles = listOf("ROLE_DEPARTMENTS__DEPARTMENTS_RW")))
      .exchange()
      .expectStatus().isOk
      .expectBody()
      .jsonPath("$.length()").isEqualTo(1)
      .jsonPath("$[0].id").isEqualTo(activeDepartment.id.toString())
      .jsonPath("$[0].name").isEqualTo("Safer Custody")
      .jsonPath("$[0].slug").isEqualTo("safer-custody")
  }

  @Test
  fun `returns an empty list when the establishment has no active departments`() {
    val department = departmentRepository.save(DepartmentModel(UUID.randomUUID(), "Reception", "reception"))
    establishmentDepartmentRepository.save(EstablishmentDepartmentModel(UUID.randomUUID(), department.id, "MDI", false))

    webTestClient.get().uri("/departments/activeAt/MDI")
      .headers(setAuthorisation(scopes = listOf(), roles = listOf("ROLE_DEPARTMENTS__DEPARTMENTS_RO")))
      .exchange()
      .expectStatus().isOk
      .expectBody()
      .jsonPath("$.length()").isEqualTo(0)
  }

  @Test
  fun `returns forbidden without either departments role, even with the read scope`() {
    webTestClient.get().uri("/departments/activeAt/MDI")
      .headers(setAuthorisation(roles = listOf("ROLE_OTHER")))
      .exchange()
      .expectStatus().isForbidden
  }
}
