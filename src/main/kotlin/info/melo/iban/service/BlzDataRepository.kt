package info.melo.iban.service

interface BlzDataRepository {
    fun findBankNameByBlz(blz: String): String?
}