package info.melo.iban

import info.melo.iban.controller.IbanValidationRequest
import info.melo.iban.controller.IbanValidationResponse
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class IbanApplicationTests {

    @Autowired
    private lateinit var restTemplate: TestRestTemplate

    @Test
    fun `given valid iban when calling iban validation api then success result with matching bank name is returned`() {
        val ibanInput = "DE92611500200000125161"

        val response =
            restTemplate.postForEntity("/api/validateIban", IbanValidationRequest(ibanInput), IbanValidationResponse.IbanValidationSuccess::class.java)

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body).isEqualTo(
            IbanValidationResponse.IbanValidationSuccess(
                validIban = "DE92 6115 0020 0000 1251 61",
                bankName = "Kreissparkasse Esslingen-Nürtingen"
            )
        )
    }

}
