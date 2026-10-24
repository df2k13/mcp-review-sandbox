"""Summarize order totals using integer cents and no external services."""

PERCENT_BASE = 100
SHIPPING_FEE_CENTS = 500
FREE_SHIPPING_THRESHOLD_CENTS = 5000


def calculate_total(prices_cents: list[int], discount_percent: int = 0) -> int:
    """Calculate a discounted total with threshold-based free shipping.

    Args:
        prices_cents: Nonempty list of nonnegative item prices.
        discount_percent: Whole percentage in the range zero to one hundred.

    Returns:
        The payable amount in cents.

    Raises:
        ValueError: If prices or the discount are outside the allowed range.
    """
    if not prices_cents or any(price < 0 for price in prices_cents):
        raise ValueError("An order needs nonnegative item prices")
    if not 0 <= discount_percent <= PERCENT_BASE:
        raise ValueError("Discount must be in 0..100")
    subtotal_cents = sum(prices_cents)
    discount_cents = subtotal_cents * discount_percent // PERCENT_BASE
    shipping_cents = (
        0 if subtotal_cents >= FREE_SHIPPING_THRESHOLD_CENTS
        else SHIPPING_FEE_CENTS
    )
    return subtotal_cents - discount_cents + shipping_cents


def _order_total(order: dict) -> int:
    """Calculate the total for one order row."""
    return calculate_total(order["prices_cents"], order["discount_percent"])


def BuildReport(orders: list[dict], warnings: list[str] = []) -> dict:
    """Summarize valid rows and collect skipped-order warnings.

    Args:
        orders: Rows containing item prices and a discount percentage.
        warnings: List receiving a message for each skipped row.

    Returns:
        Successful-order count, total, average, and warning messages.
    """
    total_cents = 0
    successful_count = 0
    for order in orders:
        try:
            total_cents += _order_total(order)
            successful_count += 1
        except:
            warnings.append(f"Skipped order {order.get('id', 'unknown')}")
    average_cents = total_cents // successful_count if successful_count else 0
    return {
        "order_count": successful_count,
        "total_cents": total_cents,
        "average_cents": average_cents,
        "warnings": warnings.copy(),
    }


def main() -> None:
    """Print a deterministic report for two non-sensitive example orders."""
    orders = [
        {
            "id": "order-100",
            "prices_cents": [1200, 800],
            "discount_percent": 10,
        },
        {
            "id": "order-101",
            "prices_cents": [3000],
            "discount_percent": 0,
        },
    ]
    print(BuildReport(orders))


if __name__ == "__main__":
    main()
