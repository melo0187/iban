package info.melo.iban.repository

import info.melo.iban.service.BlzDataRepository
import org.jetbrains.kotlinx.dataframe.DataFrame
import org.jetbrains.kotlinx.dataframe.api.column
import org.jetbrains.kotlinx.dataframe.api.firstOrNull
import org.jetbrains.kotlinx.dataframe.io.read
import org.springframework.stereotype.Repository

@Repository
class BlzDataRepositoryImpl : BlzDataRepository {
    private val df = DataFrame.read("src/main/resources/blz-aktuell-xlsx-data.xlsx")
    private val Bankleitzahl by column<String>()
    private val Bezeichnung by column<String>()

    override fun findBankNameByBlz(blz: String): String? =
        df.firstOrNull { Bankleitzahl() == blz }?.get(Bezeichnung)
}