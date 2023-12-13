package info.melo.iban.repository

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class BankDataRepositoryImplTest {
    private val testSubject: BankDataRepositoryImpl = BankDataRepositoryImpl()

    @Test
    fun `given known BLZ return bank's name`() {
        val bankName = testSubject.findBankNameByBankCode("61150020")
        assertThat(bankName).isEqualTo("Kreissparkasse Esslingen-Nürtingen")
    }

    @Test
    fun `given unknown BLZ return null`() {
        val bankName = testSubject.findBankNameByBankCode("0815")
        assertThat(bankName).isNull()
    }
}