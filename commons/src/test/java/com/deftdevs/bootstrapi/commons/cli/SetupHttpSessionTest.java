package com.deftdevs.bootstrapi.commons.cli;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SetupHttpSessionTest {

    @Test
    void testParseFormInput() {
        final String html = "<form><input type=\"hidden\" name=\"atl_token\" value=\"TOKEN123\" id=\"atl_token\"></form>";
        assertEquals("TOKEN123", SetupHttpSession.parseFormInput(html, "atl_token"));
    }

    @Test
    void testParseFormInputWithAttributeBetweenNameAndValue() {
        final String html = "<input name=\"atl_token\" type=\"hidden\" value=\"TOKEN456\">";
        assertEquals("TOKEN456", SetupHttpSession.parseFormInput(html, "atl_token"));
    }

    @Test
    void testParseFormInputAcrossMultipleLines() {
        final String html = "<input\n    type=\"hidden\"\n    name=\"sid\"\n    value=\"BTKU-QFB7-2EPG-9S8L\"\n>";
        assertEquals("BTKU-QFB7-2EPG-9S8L", SetupHttpSession.parseFormInput(html, "sid"));
    }

    @Test
    void testParseFormInputWithValueBeforeName() {
        final String html = "<input value=\"TOKEN789\" name=\"atl_token\" type=\"hidden\">";
        assertEquals("TOKEN789", SetupHttpSession.parseFormInput(html, "atl_token"));
    }

    @Test
    void testParseFormInputMissingField() {
        assertThrows(SetupException.class, () -> SetupHttpSession.parseFormInput("<html></html>", "atl_token"));
    }
}
