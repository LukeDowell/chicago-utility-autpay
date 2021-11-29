package dev.dowell.water

import org.springframework.stereotype.Component
import reactor.core.publisher.Flux

@Component
class BillPaymentService {

    fun payIfNecessary(): Flux<WaterBillPaidEvent> {
        return Flux.empty()
    }
}
