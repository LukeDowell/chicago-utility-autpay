package dev.dowell.water

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.scheduling.annotation.Scheduled
import reactor.core.publisher.Flux
import reactor.core.publisher.toFlux
import java.time.ZonedDateTime

data class WaterBillPaidEvent(val amountPaid: String, val timestamp: ZonedDateTime)

@SpringBootApplication
class WaterApplication(val billPaymentService: BillPaymentService) {

    @Bean
    @Scheduled(cron = "0 1/12 * * *", zone = "America/Chicago")
    fun tryToPayBill(): Flux<WaterBillPaidEvent> = billPaymentService.payIfNecessary()
}

fun main(args: Array<String>) {
    runApplication<WaterApplication>(*args)
}
