package info.melo.iban.controller

import arrow.core.left
import arrow.core.right
import info.melo.iban.model.BankName
import info.melo.iban.service.IbanValidationService
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post

@WebMvcTest(IbanValidationController::class)
class IbanValidationControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var ibanValidationService: IbanValidationService

    @Test
    fun `given valid iban when calling iban validation api then success result with matching bank name is returned`() {
        val ibanInput = "DE92611500200000125161"
        `when`(ibanValidationService(ibanInput)).thenReturn(
            IbanValidationService.IbanValidationSuccess(
                "DE92 6115 0020 0000 1251 61",
                BankName("Kreissparkasse Esslingen-Nürtingen")
            ).right()
        )

        mockMvc.post("/api/validateIban") {
            contentType = MediaType.APPLICATION_JSON
            content = """
                {
                   "ibanToValidate": "$ibanInput"
                }
                """.trimIndent()
        }.andExpect {
            status { isOk() }
            content { contentType("application/json") }
            content {
                json(
                    """
                {
                  "status":"OK",
                  "validIban": "DE92 6115 0020 0000 1251 61",
                  "bankName":"Kreissparkasse Esslingen-Nürtingen"
                }
                """.trimIndent()
                )
            }
        }
    }

    @Test
    fun `given valid iban of unknown bank when calling iban validation api then success result without bank name is returned`() {
        val ibanInput = "GB94BARC10201530093459"
        `when`(ibanValidationService(ibanInput)).thenReturn(IbanValidationService.IbanValidationSuccess("GB94 BARC 1020 1530 0934 59").right())

        mockMvc.post("/api/validateIban") {
            contentType = MediaType.APPLICATION_JSON
            content = """
                {
                   "ibanToValidate": "$ibanInput"
                }
                """.trimIndent()
        }.andExpect {
            status { isOk() }
            content { contentType("application/json") }
            content {
                json(
                    """
                {
                  "status":"OK",
                   "validIban": "GB94 BARC 1020 1530 0934 59"
                }""".trimIndent()
                )
            }
        }
    }

    @Test
    fun `given invalid iban when calling iban validation api then failure result is returned`() {
        val ibanInput = "GB94BARC20201530093459"
        `when`(ibanValidationService(ibanInput)).thenReturn(IbanValidationService.IbanValidationError.InvalidCheckDigit.left())

        mockMvc.post("/api/validateIban") {
            contentType = MediaType.APPLICATION_JSON
            content = """
                {
                   "ibanToValidate": "$ibanInput"
                }
                """.trimIndent()
        }.andExpect {
            status { isBadRequest() }
            content { contentType("application/json") }
            content {
                json(
                    """
                {
                  "status":"BAD_REQUEST",
                   "invalidIban":"GB94BARC20201530093459"
                }""".trimIndent()
                )
            }
        }
    }
}