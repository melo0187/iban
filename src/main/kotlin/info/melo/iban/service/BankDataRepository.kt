package info.melo.iban.service

import info.melo.iban.model.BankName

fun interface BankDataRepository {
    fun findBankNameByBankCode(blzString: String): BankName?
}