package info.melo.iban.service

import arrow.core.Either
import arrow.core.continuations.either
import info.melo.iban.model.IbanValueObject
import org.springframework.stereotype.Service

@Service
class IbanValidationService(
    private val blzDataRepository: BlzDataRepository,
) {
    operator fun invoke(ibanInput: String): Either<IbanValidationError, IbanValidationSuccess> =
        either.eager {
            val iban = IbanValueObject.create(ibanInput).mapLeft {
                when (it) {
                    is IbanValueObject.CreateIbanErrors.InvalidCheckDigit -> IbanValidationError.InvalidCheckDigit
                    is IbanValueObject.CreateIbanErrors.InvalidFormat -> IbanValidationError.InvalidFormat
                    is IbanValueObject.CreateIbanErrors.UnsupportedCountry -> IbanValidationError.UnsupportedCountry
                }
            }.bind()
            val bankName = blzDataRepository.findBankNameByBlz(iban.getBankCode())
            IbanValidationSuccess(iban.getFormattedValue(), bankName)
        }

    data class IbanValidationSuccess(val validIban: String, val bankName: String? = null)
    sealed interface IbanValidationError {
        data object InvalidCheckDigit : IbanValidationError
        data object InvalidFormat : IbanValidationError
        data object UnsupportedCountry : IbanValidationError
    }
}
