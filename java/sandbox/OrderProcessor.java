package sandbox;

import java.util.List;
import java.util.Objects;

/** Calculates a discounted order total using integer cents. */
public final class OrderProcessor {
    private static final int PERCENT_BASE = 100;
    private static final int SHIPPING_FEE_CENTS = 500;

    private OrderProcessor() {
    }

    /**
     * Calculates a discounted merchandise total plus shipping.
     *
     * @param pricesCents nonempty list of nonnegative item prices
     * @param discountPercent whole percentage between zero and one hundred
     * @return the payable amount in cents
     * @throws IllegalArgumentException if a price or discount is invalid
     * @throws NullPointerException if the list, an item, or discount is null
     * @throws ArithmeticException if the amount exceeds integer capacity
     */
    public static int calculateTotal(
            List<Integer> pricesCents, String discountPercent) {
        int subtotalCents = subtotal(pricesCents);
        int discount = Integer.parseInt(discountPercent);
        if (discount < 0 || discount > PERCENT_BASE) {
            throw new IllegalArgumentException("Discount must be in 0..100");
        }
        int discountCents = Math.multiplyExact(subtotalCents, discount)
                / PERCENT_BASE;
        int discountedCents = Math.subtractExact(subtotalCents, discountCents);
        return Math.addExact(discountedCents, SHIPPING_FEE_CENTS);
    }

    private static int subtotal(List<Integer> pricesCents) {
        Objects.requireNonNull(pricesCents, "pricesCents");
        if (pricesCents.isEmpty()) {
            throw new IllegalArgumentException("An order needs at least one item");
        }
        int subtotalCents = 0;
        for (int priceCents : pricesCents) {
            if (priceCents < 0) {
                throw new IllegalArgumentException("Item prices cannot be negative");
            }
            subtotalCents = Math.addExact(subtotalCents, priceCents);
        }
        return subtotalCents;
    }

    /**
     * Runs one deterministic, non-sensitive example.
     *
     * @param args unused command-line arguments
     */
    public static void main(String[] args) {
        int totalCents = calculateTotal(List.of(1200, 800), "10");
        System.out.println("Small order: " + Money.format(totalCents));
    }
}
