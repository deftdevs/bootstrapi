package de.aservo.confapi.confluence.model.type;

import de.aservo.confapi.confluence.model.DefaultAuthenticationScenario;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class DefaultAuthenticationScenarioTest {

    @Test
    void testDefaultReturnValues() {
        DefaultAuthenticationScenario scenario = new DefaultAuthenticationScenario();
        assertTrue(scenario.isCommonUserBase());
        assertTrue(scenario.isTrusted());
    }

}
