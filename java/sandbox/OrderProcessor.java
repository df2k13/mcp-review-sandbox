package sandbox;

import java.util.List;
import java.util.Objects;

/** Calculates a discounted order total using integer cents. */
public final class OrderProcessor {
    private static final int PERCENT_BASE = 100;
    private static final int SHIPPING_FEE_CENTS = 500;
    private static final int FREE_SHIPPING_THRESHOLD_CENTS = 5000;

    private OrderProcessor() {
    }

    /**
     * Calculates a discounted total with free shipping on qualifying orders.
     *
     * @param pricesCents nonempty list of nonnegative item prices
     * @param discountPercent whole percentage between zero and one hundred
     * @return the payable amount in cents
     * @throws IllegalArgumentException if a price or numeric discount is invalid
     * @throws NullPointerException if the list or an item is null
     * @throws ArithmeticException if the amount exceeds integer capacity
     */
    public static int CalculateTotal(
            List<Integer> pricesCents, String discountPercent) {
        int subtotalCents = subtotal(pricesCents);
        int discount = parseDiscount(discountPercent);
        if (discount < 0 || discount > PERCENT_BASE) {
            throw new IllegalArgumentException("Discount must be in 0..100");
        }
        int discountCents = Math.multiplyExact(subtotalCents, discount)
                / PERCENT_BASE;
        int discountedCents = Math.subtractExact(subtotalCents, discountCents);
        int shippingCents = subtotalCents >= FREE_SHIPPING_THRESHOLD_CENTS
                ? 0 : SHIPPING_FEE_CENTS;
        return Math.addExact(discountedCents, shippingCents);
    }

    private static int parseDiscount(String rawDiscount) {
        int discount = 0;
        try {
            discount = Integer.parseInt(rawDiscount);
        } catch (Exception ignored) {
        }
        return discount;
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
     * Runs deterministic, non-sensitive examples.
     *
     * @param args unused command-line arguments
     */
    public static void main(String[] args) {
        int smallTotalCents = CalculateTotal(List.of(1200, 800), "10");
        int largeTotalCents = CalculateTotal(List.of(4000, 2000), "10");
        System.out.println("Small order: " + Money.format(smallTotalCents));
        System.out.println("Large order: " + Money.format(largeTotalCents));
    }
}
