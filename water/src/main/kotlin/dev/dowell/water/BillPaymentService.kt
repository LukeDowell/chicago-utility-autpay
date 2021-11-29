package dev.dowell.water

import org.springframework.stereotype.Component

@Component
class BillPaymentService {

    fun payIfNecessary(): WaterBillPaidEvent? {
        return null
    }
}
