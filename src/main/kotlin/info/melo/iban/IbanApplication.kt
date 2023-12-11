package info.melo.iban

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class IbanApplication

fun main(args: Array<String>) {
	runApplication<IbanApplication>(*args)
}
