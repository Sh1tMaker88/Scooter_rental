package com.scooterrental.scooter_rental.service;

import com.scooterrental.scooter_rental.service.CatalogServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

@ExtendWith(MockitoExtension.class)
public class CatalogServiceImplTest {

    @InjectMocks
    private CatalogServiceImpl catalogService;

    @ParameterizedTest
    @ValueSource(strings = {"one", "Two", "two words", "two Words", "three words here"})
    void should_ReturnFirstLetterUpperCase_when_makeFirstLetterUppercase(String title) {
        //given
        String str = title;

        //when
        String returnedString = catalogService.makeFirstLetterUppercase(str);

        //then
        assertThat(returnedString.substring(0, 1), is(equalTo(title.substring(0, 1).toUpperCase())));
    }

    @Test
    void should_ReturnAllWordsUpperCase_when_makeEveryWordStartsUppercase() {
        //given
        String string = "three Words here";

        //when
        String returnedString = catalogService.makeEveryWordStartsUppercase(string);

        //then
        assertThat(returnedString, is(equalTo("Three Words Here")));
    }
}
