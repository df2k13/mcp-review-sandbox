package sandbox;

import java.util.Locale;

/** Formats nonnegative integer cents without floating-point conversion. */
public final class Money {
    private static final int CENTS_PER_DOLLAR = 100;

    private Money() {
    }

    /**
     * Formats an amount in dollars.
     *
     * @param cents a nonnegative amount in cents
     * @return an amount such as USD 23.00
     * @throws IllegalArgumentException if the amount is negative
     */
    public static String format(int cents) {
        if (cents < 0) {
            throw new IllegalArgumentException("Amount cannot be negative");
        }
        return String.format(Locale.ROOT, "USD %d.%02d",
                cents / CENTS_PER_DOLLAR, cents % CENTS_PER_DOLLAR);
    }
}
