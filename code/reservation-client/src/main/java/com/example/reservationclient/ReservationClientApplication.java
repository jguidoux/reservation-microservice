package com.example.reservationclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collection;

//@EnableResourceServer
//@IntegrationComponentScan
//@EnableCircuitBreaker
//@EnableFeignClients
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

//    private final ReservationReader reservationReader;
//    private final ReservationWriter reservationWriter;
//
//    ReservationApiAdapterRestController(ReservationWriter reservationWriter,
//                                        ReservationReader reservationReader) {
//        this.reservationReader = reservationReader;
//        this.reservationWriter = reservationWriter;
//
//    }

    public Collection<String> fallback() {
        return new ArrayList<>();
    }

//    @PostMapping
//    public void write(@RequestBody Reservation r) {
//        this.reservationWriter.write(r.getReservationName());
//    }
//
//    @HystrixCommand(fallbackMethod = "fallback")
//    @GetMapping("/names")
//    public Collection<String> names() {
//        return reservationReader
//                .read()
//                .stream()
//                .map(Reservation::getReservationName)
//                .collect(Collectors.toList());
//    }
}


//@FeignClient("reservation-service")
//interface ReservationReader {
//
//    @RequestMapping(method = RequestMethod.GET, value = "/reservations")
//    Collection<Reservation> read();
//
//}


class Reservation {

    private String reservationName;


    public String getReservationName() {
        return reservationName;
    }

}