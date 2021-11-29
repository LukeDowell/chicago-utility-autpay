package dev.dowell.water

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import java.time.ZonedDateTime
import java.util.function.Function

data class WaterBillPaidEvent(val amountPaid: String, val timestamp: ZonedDateTime)

@SpringBootApplication
class WaterApplication

@Component
class TryToPayBill(val billPaymentService: BillPaymentService) : Function<String, WaterBillPaidEvent?> {
    override fun apply(p0: String): WaterBillPaidEvent? {
        billPaymentService.payIfNecessary().let {
            println("Paid at ${it?.timestamp}")
            return it
        }
    }
}

fun main(args: Array<String>) {
    runApplication<WaterApplication>(*args)
}
