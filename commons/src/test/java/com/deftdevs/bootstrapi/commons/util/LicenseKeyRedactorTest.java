package com.deftdevs.bootstrapi.commons.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class LicenseKeyRedactorTest {

    @Test
    void redactsNullAsNull() {
        assertNull(LicenseKeyRedactor.redact(null));
    }

    @Test
    void redactsShortKeyAsObfuscatedWithHash() {
        final String redacted = LicenseKeyRedactor.redact("abc");
        assertEquals("***#a9993e36".substring(0, "***#".length() + 4), redacted);
    }

    @Test
    void redactsLongKeyShowingFirstAndLastFourPlusHash() {
        final String key = "AAAA1234567890ZZZZ";
        final String redacted = LicenseKeyRedactor.redact(key);
        assertEquals("AAAA...ZZZZ#", redacted.substring(0, "AAAA...ZZZZ#".length()));
        assertEquals(4, redacted.length() - "AAAA...ZZZZ#".length());
    }

    @Test
    void differentKeysWithSamePrefixAndSuffixGetDifferentHashes() {
        final String a = LicenseKeyRedactor.redact("AAAA0000ZZZZ");
        final String b = LicenseKeyRedactor.redact("AAAA1111ZZZZ");
        assertEquals(a.substring(0, "AAAA...ZZZZ#".length()), b.substring(0, "AAAA...ZZZZ#".length()));
        assertNotEquals(a, b);
    }

    @Test
    void redactionIsDeterministic() {
        final String key = "AAAA-some-very-long-license-key-payload-ZZZZ";
        assertEquals(LicenseKeyRedactor.redact(key), LicenseKeyRedactor.redact(key));
    }

}
