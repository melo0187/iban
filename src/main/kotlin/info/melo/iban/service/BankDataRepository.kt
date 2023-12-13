package info.melo.iban.service

fun interface BankDataRepository {
    fun findBankNameByBankCode(blzString: String): String?
}