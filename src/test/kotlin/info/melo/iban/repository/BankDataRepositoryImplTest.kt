package info.melo.iban.repository

import info.melo.iban.model.BankName
import info.melo.iban.service.BankDataRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(classes = [BankDataRepositoryImpl::class])
class BankDataRepositoryImplTest {
    @Autowired
    private lateinit var testSubject: BankDataRepository

    @Test
    fun `given known BLZ return bank's name`() {
        val bankName = testSubject.findBankNameByBankCode("61150020")
        assertThat(bankName).isEqualTo(BankName("Kreissparkasse Esslingen-Nürtingen"))
    }

    @Test
    fun `given unknown BLZ return null`() {
        val bankName = testSubject.findBankNameByBankCode("0815")
        assertThat(bankName).isNull()
    }
}