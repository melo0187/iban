package info.melo.iban.repository

import info.melo.iban.service.BankDataRepository
import org.jetbrains.kotlinx.dataframe.DataFrame
import org.jetbrains.kotlinx.dataframe.api.column
import org.jetbrains.kotlinx.dataframe.api.firstOrNull
import org.jetbrains.kotlinx.dataframe.io.read
import org.springframework.stereotype.Repository

@Repository
class BankDataRepositoryImpl : BankDataRepository {
    private val df = DataFrame.read("src/main/resources/blz-data.csv")
    private val Bankleitzahl by column<Int>()
    private val Bezeichnung by column<String>()

    override fun findBankNameByBankCode(blzString: String): String? =
        blzString.toIntOrNull()
            ?.let { blz ->
                df.firstOrNull { Bankleitzahl() == blz }?.get(Bezeichnung)
            }

}