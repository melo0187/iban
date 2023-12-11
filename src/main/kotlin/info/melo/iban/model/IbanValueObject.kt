package info.melo.iban.model

import org.iban4j.Iban
import org.iban4j.IbanFormatException
import org.iban4j.InvalidCheckDigitException
import org.iban4j.UnsupportedCountryException
import arrow.core.Either
import arrow.core.left
import arrow.core.right
import java.util.Locale

class IbanValueObject private constructor(private val value: Iban) {
    companion object {
        fun create(userInput: String): Either<CreateIbanErrors, IbanValueObject> {
            return try {
                val sanitizedInput = userInput
                    .replace(Regex("[^a-zA-Z0-9]"), "")
                    .uppercase(Locale.getDefault())
                val value = Iban.valueOf(sanitizedInput)
                IbanValueObject(value).right()
            } catch (e: Exception) {
                when (e) {
                    is InvalidCheckDigitException -> CreateIbanErrors.InvalidCheckDigit.left()
                    is IbanFormatException -> CreateIbanErrors.InvalidFormat.left()
                    is UnsupportedCountryException -> CreateIbanErrors.UnsupportedCountry.left()
                    else -> error("Unexpected exception: $e")
                }
            }
        }
    }

    sealed interface CreateIbanErrors {
        data object InvalidCheckDigit : CreateIbanErrors
        data object InvalidFormat : CreateIbanErrors
        data object UnsupportedCountry : CreateIbanErrors
    }
    fun getRawValue(): String = value.toString()
    fun getFormattedValue(): String = value.toFormattedString()
    fun getBankCode(): String = value.bankCode

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as IbanValueObject

        return getRawValue() == other.getRawValue()
    }

    override fun hashCode(): Int {
        return getRawValue().hashCode()
    }
}
