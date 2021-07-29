package com.scooterrental.scooter_rental.service;

import com.scooterrental.scooter_rental.model.Scooter;
import com.scooterrental.scooter_rental.model.ScooterStatus;
import com.scooterrental.scooter_rental.repository.ScooterRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("ScooterService_Test")
class ScooterServiceImplTest {

    @Mock
    private ScooterRepository scooterRepository;
    @InjectMocks
    private ScooterServiceImpl scooterService;

    private Scooter scooter;

    @BeforeEach
    void setUp() {
        scooter = new Scooter(22L, "Xiaomi", "Mi Test Scooter", "asd332w",
                ScooterStatus.OCCUPIED, null, null, 300, 20,
                20.0, 12.0, 35, 14.5, null);
    }

    @Test
    @DisplayName("setNotAvailableStatus")
    void should_ReturnNotAvailableStatus_when_setNotAvailableStatus() {
        //when
        when(scooterRepository.save(scooter)).thenReturn(scooter);
        when(scooterRepository.getScooterById(22L)).thenReturn(java.util.Optional.ofNullable(scooter));
        scooterService.setNotAvailableStatus(22L);

        //then
        assertThat(scooter.getStatus(), is(ScooterStatus.NOT_AVAILABLE));
    }

    @Test
    @DisplayName("setAvailableStatus")
    void should_ReturnAvailableStatus_when_setNotAvailableStatus() {
        //when
        when(scooterRepository.save(scooter)).thenReturn(scooter);
        when(scooterRepository.getScooterById(22L)).thenReturn(java.util.Optional.ofNullable(scooter));
        scooterService.setAvailableStatus(22L);

        //then
        assertThat(scooter.getStatus(), is(ScooterStatus.AVAILABLE));
    }
}