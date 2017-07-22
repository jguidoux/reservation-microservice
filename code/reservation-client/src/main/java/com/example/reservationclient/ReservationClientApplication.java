package com.example.reservationclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Resources;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

//@EnableResourceServer
//@IntegrationComponentScan
//@EnableCircuitBreaker
@EnableFeignClients
//@EnableBinding(ProducerChannels.class)
@EnableZuulProxy
@EnableDiscoveryClient
@SpringBootApplication
public class ReservationClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReservationClientApplication.class, args);
    }
}

//interface ProducerChannels {
//
//    String OUTPUT = "output";
//
//    @Output(OUTPUT)
//    MessageChannel output();
//}

//
//@MessagingGateway
//interface ReservationWriter {
//
//    @Gateway(requestChannel = ProducerChannels.OUTPUT)
//    void write(String rn);
//}

@RestController
@RequestMapping("/reservations")
class ReservationApiAdapterRestController {

    private final ReservationReader reservationReader;
//    private final ReservationWriter reservationWriter;
//
    ReservationApiAdapterRestController(
            //ReservationWriter reservationWriter,
                                        ReservationReader reservationReader) {
        this.reservationReader = reservationReader;
//        this.reservationWriter = reservationWriter;

    }

    public Collection<String> fallback() {
        return new ArrayList<>();
    }

//    @PostMapping
//    public void write(@RequestBody Reservation r) {
//        this.reservationWriter.write(r.getReservationName());
//    }
//
//    @HystrixCommand(fallbackMethod = "fallback")
    @GetMapping("/names")
    public Collection<String> names() {
        return reservationReader.read()
                .getContent()
                .stream()
                .map(Reservation::getReservationName)
                .collect(Collectors.toList());
    }
}


@FeignClient("reservation-service")
interface ReservationReader {

    @GetMapping("/reservations")
    Resources<Reservation> read();

}


class Reservation {

    private String reservationName;


    public String getReservationName() {
        return reservationName;
    }

}