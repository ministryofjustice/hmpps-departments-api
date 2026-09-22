package uk.gov.justice.digital.hmpps.departmentsapi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class DepartmentsApi

fun main(args: Array<String>) {
  runApplication<DepartmentsApi>(*args)
}
