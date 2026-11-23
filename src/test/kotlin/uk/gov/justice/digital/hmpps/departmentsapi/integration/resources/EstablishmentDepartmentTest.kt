package uk.gov.justice.digital.hmpps.departmentsapi.integration.resources

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import uk.gov.justice.digital.hmpps.departmentsapi.integration.IntegrationTestBase
import uk.gov.justice.digital.hmpps.departmentsapi.model.EstablishmentDepartment
import uk.gov.justice.digital.hmpps.departmentsapi.repository.EstablishmentDepartmentRepository
import java.util.UUID

class EstablishmentDepartmentTest : IntegrationTestBase() {

  @Autowired
  private lateinit var repository: EstablishmentDepartmentRepository

  @BeforeEach
  fun setUp() {
    repository.deleteAll()
  }

  @Test
  fun `returns only active departments for the requested establishment`() {
    val departmentId = UUID.randomUUID()
    val activeRecord = repository.save(EstablishmentDepartment(UUID.randomUUID(), departmentId, "MDI", true))
    repository.save(EstablishmentDepartment(UUID.randomUUID(), UUID.randomUUID(), "MDI", false))
    repository.save(EstablishmentDepartment(UUID.randomUUID(), UUID.randomUUID(), "LEI", true))

    webTestClient.get().uri("/establishments/MDI/departments")
      .headers(setAuthorisation(scopes = listOf(), roles = listOf("ROLE_DEPARTMENTS__DEPARTMENTS_RW")))
      .exchange()
      .expectStatus().isOk
      .expectBody()
      .jsonPath("$.length()").isEqualTo(1)
      .jsonPath("$[0].id").isEqualTo(activeRecord.id.toString())
      .jsonPath("$[0].establishmentId").isEqualTo("MDI")
      .jsonPath("$[0].departmentId").isEqualTo(departmentId.toString())
      .jsonPath("$[0].active").isEqualTo(true)
  }

  @Test
  fun `returns an empty list when the establishment has no active departments`() {
    repository.save(EstablishmentDepartment(UUID.randomUUID(), UUID.randomUUID(), "MDI", false))

    webTestClient.get().uri("/establishments/MDI/departments")
      .headers(setAuthorisation(scopes = listOf(), roles = listOf("ROLE_DEPARTMENTS__DEPARTMENTS_RO")))
      .exchange()
      .expectStatus().isOk
      .expectBody()
      .jsonPath("$.length()").isEqualTo(0)
  }

  @Test
  fun `returns forbidden without either departments role, even with the read scope`() {
    webTestClient.get().uri("/establishments/MDI/departments")
      .headers(setAuthorisation(roles = listOf("ROLE_OTHER")))
      .exchange()
      .expectStatus().isForbidden
  }
}
