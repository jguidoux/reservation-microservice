
package com.example.reservationservice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.stream.Stream;


//@EnableBinding(ConsumerChannels.class)
@EnableDiscoveryClient
@SpringBootApplication
public class ReservationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReservationServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner init(ReservationRepository rr) {
        return args -> {
            Stream.of("Josh", "Heidi", "Cameron", "Saritha",
                    "Balaji", "Soumya", "Steve", "Kelsey")
                    .forEach(name -> rr.save(new Reservation(name)));

            rr.findAll().forEach(System.out::println);
        };
    }

//    @Bean
//    IntegrationFlow inboundReservationFlow(ConsumerChannels channels,
//                                           ReservationRepository rr) {
//        return IntegrationFlows
//                .from(channels.input())
//                .handle((GenericHandler<String>) (reservationName, headers) -> {
//                    rr.save(new Reservation(reservationName));
//                    return null;
//                })
//                .get();
//    }
}
/*
@Component
class StreamListenerComponent {

    @StreamListener ("input")
    public void on( String reservationName) {

    }
}*/

//interface ConsumerChannels {
//    @Input
//    SubscribableChannel input();
//}

//@RestController
//class ReservationRestController {
//
//
//    private final ReservationRepository reservationRepository;
//
//    ReservationRestController(ReservationRepository reservationRepository) {
//        this.reservationRepository = reservationRepository;
//    }
//
//    @GetMapping("/reservations")
//    Collection<Reservation> reservations() {
//        return this.reservationRepository.findAll();
//    }
//
//}


@RestController
@RefreshScope
class MessageRestController {

    private final String value;

    MessageRestController(@Value("${message}") String value) {
        this.value = value;
    }

    @GetMapping("/message")
    String message() {
        return this.value;
    }
}

//@Component
//class CustomHealthIndicator implements HealthIndicator {
//
//    @Override
//    public Health health() {
//        return Health.status("I <3 Target!!!").build();
//    }
//}

@RepositoryRestResource
interface ReservationRepository extends JpaRepository<Reservation, Long> {

//    Collection<Reservation> findByReservationName(String rn);
}

//@NoArgsConstructor
//@AllArgsConstructor
//@Data
@Entity
class Reservation {

    public Reservation() {

    }

    public Reservation(String reservationName) {
        this.reservationName = reservationName;
    }

    @Id
    @GeneratedValue
    private Long id;

    private String reservationName; // reservation_name

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReservationName() {
        return reservationName;
    }

    public void setReservationName(String reservationName) {
        this.reservationName = reservationName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Reservation that = (Reservation) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        return reservationName != null ? reservationName.equals(that.reservationName) : that.reservationName == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (reservationName != null ? reservationName.hashCode() : 0);
        return result;
    }


    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", reservationName='" + reservationName + '\'' +
                '}';
    }
}