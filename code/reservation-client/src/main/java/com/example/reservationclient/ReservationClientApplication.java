package com.example.reservationclient;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resources;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.messaging.MessageChannel;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

@EnableResourceServer
@IntegrationComponentScan
@EnableCircuitBreaker
@EnableFeignClients
@EnableBinding(Source.class)
@EnableZuulProxy
@EnableDiscoveryClient
@SpringBootApplication
public class ReservationClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReservationClientApplication.class, args);
    }
}


@MessagingGateway
interface ReservationWriter {

    @Gateway(requestChannel = Source.OUTPUT)
    void write(String rn);
}

@RestController
@RequestMapping("/reservations")
class ReservationApiAdapterRestController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());


    private final ReservationReader reservationReader;
    private final ReservationWriter reservationWriter;


    //    @Autowired
    ReservationApiAdapterRestController(
            ReservationWriter reservationWriter,
            ReservationReader reservationReader) {
        this.reservationReader = reservationReader;
        this.reservationWriter = reservationWriter;

    }

    public Collection<String> fallback(Pageable pageable) {
        return new ArrayList<>();
    }

    @PostMapping
    public void write(@RequestBody Reservation r) {
        log.info("sending {}", r.getReservationName());
        this.reservationWriter.write(r.getReservationName());
    }

    @HystrixCommand(fallbackMethod = "fallback")
    @GetMapping("/names")
    public Collection<String> names(Pageable pageable) {
        return reservationReader.read(pageable.getPageNumber(), pageable.getPageSize())
                .getContent()
                .stream()
                .map(Reservation::getReservationName)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}


@FeignClient("reservation-service")
interface ReservationReader {

    @GetMapping("/reservations")
    PagedResources<Reservation> read(@RequestParam("page") int page, @RequestParam("size") int size);

}


class Reservation {

    private String reservationName;


    public String getReservationName() {
        return reservationName;
    }

}