package dev.dowell.water

import com.jayway.jsonpath.Configuration
import com.jayway.jsonpath.JsonPath
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.cloud.stream.binder.test.InputDestination
import org.springframework.cloud.stream.binder.test.OutputDestination
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration
import org.springframework.context.annotation.Import
import org.springframework.messaging.support.MessageBuilder
import reactor.core.publisher.Flux
import java.time.ZonedDateTime

@Suppress("SpringJavaInjectionPointsAutowiringInspection")
@SpringBootTest
@Import(TestChannelBinderConfiguration::class)
class WaterApplicationTests {

    @MockBean
    lateinit var billPaymentService: BillPaymentService

    @Autowired
    lateinit var inputDestination: InputDestination

    @Autowired
    lateinit var outputDestination: OutputDestination

    @Test
    fun contextLoads() {
    }

    @Test
    fun `it should emit a bill paid event when the water bill is successfully paid`() {
        `when`(billPaymentService.payIfNecessary()).thenReturn(WaterBillPaidEvent("10.00", ZonedDateTime.now()))

        inputDestination.send(MessageBuilder.withPayload("").build(), "water-in-try-to-pay-bill")
        val message = outputDestination.receive(1000L, "water-out-bill-paid")
        val payload = Configuration.defaultConfiguration().jsonProvider().parse(String(message.payload))

        assertThat(JsonPath.read<String>(payload, "$.amountPaid")).isNotNull
        assertThat(JsonPath.read<String>(payload, "$.timestamp")).isNotNull
    }
}
