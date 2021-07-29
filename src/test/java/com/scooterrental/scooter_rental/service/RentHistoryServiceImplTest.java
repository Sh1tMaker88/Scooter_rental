package com.scooterrental.scooter_rental.service;

import com.scooterrental.scooter_rental.exception.ServiceException;
import com.scooterrental.scooter_rental.model.*;
import com.scooterrental.scooter_rental.repository.RentHistoryRepository;
import com.scooterrental.scooter_rental.repository.RentalPointRepository;
import com.scooterrental.scooter_rental.repository.ScooterRepository;
import com.scooterrental.scooter_rental.security.model.Role;
import com.scooterrental.scooter_rental.security.model.Status;
import com.scooterrental.scooter_rental.security.model.User;
import com.scooterrental.scooter_rental.security.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Date;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("RentHistoryService_Test")
class RentHistoryServiceImplTest {

    @Mock
    private ScooterRepository scooterRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private RentalPointRepository rentalPointRepository;
    @Mock
    private RentHistoryRepository rentHistoryRepository;
    @InjectMocks
    private RentHistoryServiceImpl rentHistoryService;

    @Nested
    @DisplayName("rentScooter_tests")
    class RentScooter {

        private Catalog city;
        private RentalPoint rentalPoint;
        private RentalPrice rentalPrice;
        private Scooter scooter;
        private Role role;
        private User user;

        @BeforeEach
        void setUp() {
            city = new Catalog(1L, "City", null, null);
            rentalPoint = new RentalPoint(11L, "TestPoint", "TestOwner", null,
                    new GeometryFactory().createPoint(new Coordinate(55.234, 22.234)),
                    city, 120.0, "Test street", 55, "029123456123",
                    "testScooter@gmail.com", null);
            rentalPrice = new RentalPrice(33L, "Price Set", null, 1.1, 2.2,
                    3.3, 5.5, 9.9, 12.2, 20.2);
            scooter = new Scooter(22L, "Xiaomi", "Mi Test Scooter", "asd332w",
                    ScooterStatus.AVAILABLE, rentalPoint, rentalPrice, 300, 20,
                    20.0, 12.0, 35, 14.5, null);
            role = new Role(4L, new Date(), null, Status.ACTIVE, "ROLE_ADMIN", null);
            user = new User(44L, "Testname", "Dima", "Ezer", "dima@mail.ru", "pass", new Date(), null,
                    Status.ACTIVE, Collections.singletonList(role), null);
        }

        @Test
        void should_SetScooterStatusToOccupied_when_RentScooter() {
            //when
            when(scooterRepository.getScooterById(22L)).thenReturn(Optional.of(scooter));
            when(userRepository.findByUsername("Testname")).thenReturn(Optional.of(user));
            when(rentalPointRepository.getRentalPointById(11L)).thenReturn(Optional.of(rentalPoint));
            rentHistoryService.rentScooter(11L, 22L, "one_day", "Testname");

            //then
            assertThat(scooter.getStatus(), is(equalTo(ScooterStatus.OCCUPIED)));
        }

        @Test
        @DisplayName("scooterRepository_exception")
        void should_ThrowServiceException_when_RentScooter_scooterRepository() {
            //when
            when(scooterRepository.getScooterById(22L)).thenReturn(Optional.empty());
//            when(rentalPointRepository.getRentalPointById(11L)).thenReturn(Optional.ofNullable(rentalPoint));
//            Executable executable3 = () -> rentalPointRepository.getRentalPointById(11L);

            //then
            assertThrows(ServiceException.class, () -> rentHistoryService.rentScooter(11L, 22L,
                    "one_day", "Testname"));
        }

        @Test
        @DisplayName("userRepository_exception")
        void should_ThrowServiceException_when_RentScooter_userRepository() {
            //when
            when(scooterRepository.getScooterById(22L)).thenReturn(Optional.of(scooter));
            when(userRepository.findByUsername("Testname")).thenReturn(Optional.empty());

            //then
            assertThrows(ServiceException.class, () -> rentHistoryService.rentScooter(11L, 22L,
                    "one_day", "Testname"));
        }

        @Test
        @DisplayName("rentalPointRepository_exception")
        void should_ThrowServiceException_when_RentScooter_rentalPointRepository() {
            //when
            when(scooterRepository.getScooterById(22L)).thenReturn(Optional.of(scooter));
            when(userRepository.findByUsername("Testname")).thenReturn(Optional.of(user));
            when(userRepository.findByUsername("Testname")).thenReturn(Optional.empty());

            //then
            assertThrows(ServiceException.class, () -> rentHistoryService.rentScooter(11L, 22L,
                    "one_day", "Testname"));
        }
    }

    @ParameterizedTest
    @ValueSource(doubles = {25.4, 333.5, 0.0})
    @DisplayName("payTheRent")
    void should_SetCorrectBalance_when_payTheRent(Double price) {
        //given
        RentalPoint rentalPoint = new RentalPoint(11L, "TestPoint", "TestOwner", null,
                new GeometryFactory().createPoint(new Coordinate(55.234, 22.234)),
                null, 120.0, "Test street", 55, "029123456123",
                "testScooter@gmail.com", null);
        Double currentBalance = rentalPoint.getBalance();

        //when
        rentHistoryService.payTheRent(rentalPoint, price);

        //then
        assertThat(rentalPoint.getBalance(), is(equalTo(currentBalance + price)));
    }
}