package info.melo.iban.model

import info.melo.iban.model.IbanValueObject.CreateIbanErrors
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.stream.Stream

class IbanValueObjectFactoryTest {

    data class TestCase(val description: String, val input: String, val result: TestResult) {
        sealed interface TestResult {
            data class Success(val iban: String) : TestResult
            data class Failure(val error: CreateIbanErrors) : TestResult
        }

        override fun toString(): String = description
    }

    class TestCaseArgumentProvider: ArgumentsProvider {
        /**
         * courtesy of https://de.iban.com/testibans
         * and with some additions from myself
         */
        private val testCases = listOf(
            TestCase(
                "Gültige IBAN, Länge, Prüfsumme, Bankleitzahl, Konto und Struktur",
                "GB33BUKB20201555555555",
                TestCase.TestResult.Success("GB33BUKB20201555555555")
            ),
            TestCase(
                "Gültige IBAN, Bankleitzahl nicht gefunden (Bank kann nicht identifiziert werden): Gültige Länge, Prüfsumme, Bankleitzahl, Konto und Struktur",
                "GB94BARC10201530093459",
                TestCase.TestResult.Success("GB94BARC10201530093459")
            ),
            TestCase(
                "Gültige IBAN mit Kleinbuchstaben",
                "de92611500200000125161",
                TestCase.TestResult.Success("DE92611500200000125161")
            ),
            TestCase(
                "Gültige IBAN mit Trennzeichen",
                "DE92 6115 0020 0000 1251 61",
                TestCase.TestResult.Success("DE92611500200000125161")
            ),
            TestCase(
                "Gültige IBAN mit Bindestrich als Trennzeichen",
                "DE92-6115-0020-0000-1251-61",
                TestCase.TestResult.Success("DE92611500200000125161")
            ),
            TestCase(
                "Ungültige IBAN-Prüfstellen MOD-97-10 nach ISO/IEC 7064:2003",
                "GB94BARC20201530093459",
                TestCase.TestResult.Failure(CreateIbanErrors.InvalidCheckDigit)
            ),
            TestCase(
                "Ungültige IBAN-Länge muss \"X\"-Zeichen lang sein!",
                "GB96BARC202015300934591",
                TestCase.TestResult.Failure(CreateIbanErrors.InvalidFormat)
            ),
            TestCase(
                "Ungültige Kontenstruktur",
                "GB12BARC20201530093A59",
                TestCase.TestResult.Failure(CreateIbanErrors.InvalidFormat)
            ),
            TestCase(
                "Bankleitzahl nicht gefunden und Ungültige Bankleitzahlstruktur",
                "GB78BARCO0201530093459",
                TestCase.TestResult.Failure(CreateIbanErrors.InvalidFormat)
            ),
            TestCase(
                "Ungültige IBAN-Prüfsummenstruktur",
                "GB2LABBY09012857201707",
                TestCase.TestResult.Failure(CreateIbanErrors.InvalidFormat)
            ),
            TestCase(
                "Land scheint IBAN nicht zu unterstützen!",
                "US64SVBKUS6S3300958879",
                TestCase.TestResult.Failure(CreateIbanErrors.UnsupportedCountry)
            )
        )

        override fun provideArguments(p0: ExtensionContext?): Stream<out Arguments> =
            testCases.map { Arguments.of(it) }.stream()

    }

    @ParameterizedTest
    @ArgumentsSource(TestCaseArgumentProvider::class)
    fun `test createIBAN`(testCase: TestCase) {
        val result = IbanValueObject.create(testCase.input)
        when (testCase.result) {
            is TestCase.TestResult.Success -> {
                assertThat(result.isRight()).isTrue()
                result.map {
                    assertThat(it.getRawValue()).isEqualTo(testCase.result.iban)
                }
            }
            is TestCase.TestResult.Failure -> {
                assertThat(result.isLeft()).isTrue()
                result.mapLeft {
                    assertThat(it).isEqualTo(testCase.result.error)
                }
            }
        }
    }
}