package com.deftdevs.bootstrapi.commons.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Redacts license keys for inclusion in HTTP responses.
 * <p>
 * License keys are sensitive and should not be echoed back in full to clients,
 * since responses may be captured by intermediaries (proxies, logs, etc.) that
 * do not capture the original request body. Redacted keys allow a caller to
 * correlate a response entry with the key they sent without leaking the full
 * value.
 * <p>
 * Format: {@code <first4>...<last4>#<hash4>}, where {@code hash4} is the first
 * four hex characters of the SHA-1 digest of the full key. The hash suffix
 * disambiguates keys that happen to share the same prefix and suffix (e.g.
 * Atlassian license keys, which all start with the same product header).
 * <p>
 * Keys shorter than nine characters are hashed entirely and emitted as
 * {@code ***#<hash4>} to avoid leaking the full key when redaction would be
 * effectively a no-op.
 */
public final class LicenseKeyRedactor {

    private static final int VISIBLE_PREFIX_LENGTH = 4;
    private static final int VISIBLE_SUFFIX_LENGTH = 4;
    private static final int HASH_LENGTH = 4;
    private static final int MIN_REDACTABLE_LENGTH = VISIBLE_PREFIX_LENGTH + VISIBLE_SUFFIX_LENGTH + 1;

    private LicenseKeyRedactor() {
    }

    public static String redact(final String key) {
        if (key == null) {
            return null;
        }
        final String hash = shortSha1(key);
        if (key.length() < MIN_REDACTABLE_LENGTH) {
            return "***#" + hash;
        }
        return key.substring(0, VISIBLE_PREFIX_LENGTH)
                + "..."
                + key.substring(key.length() - VISIBLE_SUFFIX_LENGTH)
                + "#"
                + hash;
    }

    private static String shortSha1(final String input) {
        try {
            final MessageDigest md = MessageDigest.getInstance("SHA-1"); // NOSONAR: Hash is just used for license collisions
            final byte[] digest = md.digest(input.getBytes(StandardCharsets.UTF_8));
            final StringBuilder hex = new StringBuilder(HASH_LENGTH);
            for (int i = 0; i < (HASH_LENGTH + 1) / 2; i++) {
                hex.append(String.format("%02x", digest[i] & 0xff));
            }
            return hex.substring(0, HASH_LENGTH);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-1 not available", e);
        }
    }

}
