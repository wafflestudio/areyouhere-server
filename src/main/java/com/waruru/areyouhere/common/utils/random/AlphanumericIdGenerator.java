package com.waruru.areyouhere.common.utils.random;

import java.util.Random;

public class AlphanumericIdGenerator implements RandomIdentifierGenerator {
    private static final String ALPHANUMERIC_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private final Random random = new Random();

    @Override
    public String generateRandomIdentifier(int length) {
        StringBuilder builder = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(ALPHANUMERIC_CHARACTERS.length());
            builder.append(ALPHANUMERIC_CHARACTERS.charAt(index));
        }
        return builder.toString();
    }
}
