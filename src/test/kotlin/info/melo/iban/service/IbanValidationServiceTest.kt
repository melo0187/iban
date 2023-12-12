package info.melo.iban.service

import arrow.core.left
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class IbanValidationServiceTest {

    private lateinit var ibanValidationService: IbanValidationService

    private val blzDataRepositoryFake = BlzDataRepository {
        when (it) {
            "10000000" -> "Bundesbank"
            "10010010" -> "Postbank Ndl der Deutsche Bank"
            "10010123" -> "OLINDA Zweigniederlassung Deutschland"
            "10010200" -> "Treezor, Berlin"
            "10010300" -> "Klarna Bank German Branch"
            "10010424" -> "Aareal Bank"
            "10010500" -> "Noelse Pay"
            "10011001" -> "N26 Bank"
            else -> null
        }
    }

    @BeforeEach
    fun setUp() {
        ibanValidationService = IbanValidationService(blzDataRepositoryFake)
    }

    @Test
    fun `given valid iban when calling iban validation service then success result with matching bank name is returned`() {
        val ibanInput = "DE91100000000123456789"

        val result = ibanValidationService(ibanInput)

        assertTrue(result.isRight())
        assertEquals("DE91 1000 0000 0123 4567 89", result.orNull()!!.validIban)
        assertEquals("Bundesbank", result.orNull()!!.bankName)
    }

    @Test
    fun `given valid iban of unknown bank when calling iban validation service then success result without bank name is returned`() {
        val ibanInput = "GB94BARC10201530093459"

        val result = ibanValidationService(ibanInput)

        assertTrue(result.isRight())
        assertEquals("GB94 BARC 1020 1530 0934 59", result.orNull()!!.validIban)
        assertNull(result.orNull()!!.bankName)
    }

    @Test
    fun `given iban with wrong check digit when calling iban validation service then error result is returned`() {
        val ibanInput = "GB94BARC20201530093459"

        val result = ibanValidationService(ibanInput)

        assertTrue(result.isLeft())
        assertEquals(IbanValidationService.IbanValidationError.InvalidCheckDigit.left(), result)
    }

    @Test
    fun `given iban with wrong format when calling iban validation service then error result is returned`() {
        val ibanInput = "GB96BARC202015300934591"

        val result = ibanValidationService(ibanInput)

        assertTrue(result.isLeft())
        assertEquals(IbanValidationService.IbanValidationError.InvalidFormat.left(), result)
    }

    @Test
    fun `given iban with unsupported country when calling iban validation service then error result is returned`() {
        val ibanInput = "US64SVBKUS6S3300958879"

        val result = ibanValidationService(ibanInput)

        assertTrue(result.isLeft())
        assertEquals(IbanValidationService.IbanValidationError.UnsupportedCountry.left(), result)
    }

}