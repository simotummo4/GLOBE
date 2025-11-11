package it.uniroma2.ispw.globe.bean;

import it.uniroma2.ispw.globe.exception.IncorrectDataException;
import javafx.util.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

class ItineraryBeanTest {

    // Teo Miozzi

    @Test
    void testSetDurationCorrect() throws IncorrectDataException {
        ItineraryBean itineraryBean = new ItineraryBean();
        itineraryBean.setDuration(3);
        Assertions.assertEquals(3, itineraryBean.getDuration());
    }

    @ParameterizedTest
    @ValueSource(ints = {
            -1, // negative number
            100 // too many days
    })
    void testSetDurationIncorrect(int duration) {
        String errorMess = "";
        ItineraryBean itineraryBean = new ItineraryBean();
        try {
            itineraryBean.setDuration(duration);
        } catch (IncorrectDataException e) {
            errorMess = e.getMessage();
        }
        Assertions.assertEquals("Itinerary duration not valid", errorMess);
    }

    @Test
    void testSetOutboundFlightDepartureTimeCorrect() throws IncorrectDataException {
        ItineraryBean itineraryBean = new ItineraryBean();
        itineraryBean.setOutboundFlightDepartureTime(13.4);
        Assertions.assertEquals(13.4, itineraryBean.getOutboundFlightDepartureTime());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "-12.00",     // Negative hour
            "24.00",     // Exceeds maximum hour (23)
            "12.60",     // Minutes exceed 59
            "99.99",     // Completely out of range
            "12.599",    // Invalid minute precision
            "23.75"      // 75 minutes is not valid
    })
    void testSetOutboundFlightDepartureTimeIncorrect(String time) {
        String errorMess = "";
        ItineraryBean itineraryBean = new ItineraryBean();
        double doublueTime = Double.parseDouble(time);
        try {
            itineraryBean.setOutboundFlightDepartureTime(doublueTime);
        } catch (IncorrectDataException e) {
            errorMess = e.getMessage();
        }
        Assertions.assertEquals("Itinerary outbound departure time not valid", errorMess);
    }

    @Test
    void testSetAccommodationsCorrect() throws IncorrectDataException {
        ItineraryBean itineraryBean = new ItineraryBean();
        List<Pair<String, String>> accommodations = new ArrayList<>();
        accommodations.add(new Pair<>("hotel test1", "50th street"));
        accommodations.add(new Pair<>("hotel test2", "via roma 12"));
        itineraryBean.setAccommodations(accommodations);

        Assertions.assertEquals(accommodations, itineraryBean.getAccommodations());
    }

    @ParameterizedTest
    @MethodSource("provideAccommodations")
    void testSetAccommodationsIncorrect(List<Pair<String, String>> accommodations) {
        String errorMess = "";
        ItineraryBean itineraryBean = new ItineraryBean();
        try {
            itineraryBean.setAccommodations(accommodations);
        } catch (IncorrectDataException e) {
            errorMess = e.getMessage();
        }
        Assertions.assertEquals("Accommodation not valid", errorMess);
    }

    static Stream<List<Pair<String, String>>> provideAccommodations() {
        return Stream.of(

                // empty hotel name
                List.of(
                        new Pair<>("", "Via Roma 10"),
                        new Pair<>("Grand Palace", "Boulevard Saint-Michel, 5")
                ),

                // invalid address
                List.of(
                        new Pair<>("Hotel Roma", "Address@Invalid"),
                        new Pair<>("Grand Palace", "Main street, 5")
                ),

                // empty hotel address
                List.of(new Pair<>("Hotel Roma", "Via Roma 10"),
                        new Pair<>("Grand Palace", ""))
        );
    }
}