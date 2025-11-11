package it.uniroma2.ispw.globe.bean;

import it.uniroma2.ispw.globe.exception.IncorrectDataException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

class ProposalBeanTest {

    // Teo Miozzi

    @Test
    void testSetPriceCorrect() throws IncorrectDataException {
        ProposalBean proposalBean  = new ProposalBean();
        proposalBean.setPrice(159.4);
        Assertions.assertEquals(159.4, proposalBean.getPrice());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "-12.00",     // Negative price
            "0"     // null price
    })
    void testSetPriceIncorrect(String price) {
        String errorMess = "";
        ProposalBean proposalBean = new ProposalBean();
        double doubluePrice = Double.parseDouble(price);
        try {
            proposalBean.setPrice(doubluePrice);
        } catch (IncorrectDataException e) {
            errorMess = e.getMessage();
        }
        Assertions.assertEquals("Proposal price not valid", errorMess);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "accepted",
            "rejected",
            "pending"
    })
    void testSetAcceptedCorrect(String accepted) throws IncorrectDataException {
        ProposalBean proposalBean = new ProposalBean();
        proposalBean.setAccepted(accepted);
        Assertions.assertEquals(accepted, proposalBean.getAccepted());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "",  // empty string
            "incorrect status"  // status not valid
    })
    void testSetAcceptedIncorrect(String accepted) {
        String errorMess = "";
        ProposalBean proposalBean = new ProposalBean();
        try {
            proposalBean.setAccepted(accepted);
        } catch (IncorrectDataException e) {
            errorMess = e.getMessage();
        }
        Assertions.assertEquals("Proposal status not valid", errorMess);
    }

    @Test
    void testSetDescriptionCorrect() throws IncorrectDataException {
        ProposalBean proposalBean = new ProposalBean();
        proposalBean.setDescription("test description for agency proposal");
        Assertions.assertEquals("test description for agency proposal", proposalBean.getDescription());
    }

    @ParameterizedTest
    @MethodSource("provideDescription")
    void testSetDescriptionIncorrect(String description) {
        String errorMess = "";
        ProposalBean proposalBean = new ProposalBean();
        try {
            proposalBean.setDescription(description);
        } catch (IncorrectDataException e){
            errorMess = e.getMessage();
        }
        Assertions.assertEquals("Proposal description not valid", errorMess);
    }

    static Stream<String> provideDescription() {
        return Stream.of(
                "x".repeat(2000),
                "",
                null
        );
    }

}