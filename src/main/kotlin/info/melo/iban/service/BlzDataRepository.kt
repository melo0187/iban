package info.melo.iban.service

fun interface BlzDataRepository {
    fun findBankNameByBlz(blz: String): String?
}