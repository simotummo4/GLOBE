package it.uniroma2.ispw.globe.bean;

import it.uniroma2.ispw.globe.exception.IncorrectDataException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class RequestBeanTest {

    // test by Simone Tummolo 0309116

    @Test
    void testSetAcceptedCorrect() throws IncorrectDataException {
        RequestBean requestBean = new RequestBean();
        requestBean.setAccepted("accepted");
        assertEquals("accepted", requestBean.getAccepted());
    }


    @ParameterizedTest
    @ValueSource(strings = {
            "", // empty string
            "string" // not accepted string
    })

    void testSetAcceptedIncorrect(String accepted){
        String errorMess = "";
        RequestBean requestBean = new RequestBean();
        try {
            requestBean.setAccepted(accepted);
        }catch (IncorrectDataException e){
            errorMess = e.getMessage();
        }
        Assertions.assertEquals("Request accepted not valid", errorMess);
    }
    @Test
    void testSetDayNumCorrect() throws IncorrectDataException {
        RequestBean requestBean = new RequestBean();
        requestBean.setDayNum(5);
        Assertions.assertEquals(5, requestBean.getDayNum());
    }

    @ParameterizedTest
    @ValueSource(ints = {
            -1, // negative number
            100 // too many days
    })
    void testSetDayNumIncorrect(int dayNum) {
        String errorMess = "";
        RequestBean requestBean = new RequestBean();
        try {
            requestBean.setDayNum(dayNum);
        }catch (IncorrectDataException e){
            errorMess = e.getMessage();
        }
        Assertions.assertEquals("Request dayNum not valid", errorMess);

    }
    @Test
    void testSetOtherRequestsCorrect() throws IncorrectDataException {
        RequestBean requestBean = new RequestBean();
        requestBean.setOtherRequests("otherRequests");
        Assertions.assertEquals("otherRequests", requestBean.getOtherRequests());
    }

    @ParameterizedTest
    @MethodSource("provideOtherRequests")
    void testSetOtherRequestsIncorrect(String otherRequests) {
        String errorMess = "";
        RequestBean requestBean = new RequestBean();
        try {
            requestBean.setOtherRequests(otherRequests);
        } catch (IncorrectDataException e){
            errorMess = e.getMessage();
        }
        Assertions.assertEquals("Proposal other request not valid", errorMess);
    }

    static Stream<String> provideOtherRequests() {
        return Stream.of(
                "a".repeat(1000),
                ""
        );
    }
}
