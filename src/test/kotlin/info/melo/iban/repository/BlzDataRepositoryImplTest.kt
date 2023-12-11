package info.melo.iban.repository

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class BlzDataRepositoryImplTest {
    private val testSubject: BlzDataRepositoryImpl = BlzDataRepositoryImpl()

    @Test
    fun `given known BLZ return bank's name`() {
        val bankName = testSubject.findBankNameByBlz("61150020")
        assertThat(bankName).isEqualTo("Kreissparkasse Esslingen-Nürtingen")
    }

    @Test
    fun `given unknown BLZ return null`() {
        val bankName = testSubject.findBankNameByBlz("0815")
        assertThat(bankName).isNull()
    }
}