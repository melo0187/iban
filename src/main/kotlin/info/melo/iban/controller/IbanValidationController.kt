package info.melo.iban.controller

import info.melo.iban.service.IbanValidationService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class IbanValidationController(
    private val ibanValidationService: IbanValidationService,
) {

    @PostMapping("/api/validateIban")
    fun validateIban(@RequestBody ibanValidationRequest: IbanValidationRequest): ResponseEntity<IbanValidationResponse> =
        ibanValidationService(ibanValidationRequest.ibanToValidate).fold(
            ifLeft = {
                val errorMessage = when (it) {
                    is IbanValidationService.IbanValidationError.InvalidCheckDigit -> "Invalid check digit"
                    is IbanValidationService.IbanValidationError.InvalidFormat -> "Invalid format"
                    is IbanValidationService.IbanValidationError.UnsupportedCountry -> "Unsupported country"
                }
                IbanValidationResponse.IbanValidationFailure(ibanValidationRequest.ibanToValidate, errorMessage)
            },
            ifRight = {
                IbanValidationResponse.IbanValidationSuccess(it.validIban, it.bankName)
            }
        ).let { response ->
            ResponseEntity(response, response.status)
        }

}

data class IbanValidationRequest(val ibanToValidate: String)

sealed class IbanValidationResponse(val status: HttpStatus) {
    data class IbanValidationSuccess(val validIban: String, val bankName: String?) : IbanValidationResponse(HttpStatus.OK)
    data class IbanValidationFailure(val invalidIban: String, val errorMessage: String) : IbanValidationResponse(HttpStatus.BAD_REQUEST)
}