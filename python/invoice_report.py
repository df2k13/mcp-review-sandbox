"""Summarize order totals using integer cents and no external services."""

PERCENT_BASE = 100
SHIPPING_FEE_CENTS = 500


def calculate_total(prices_cents: list[int], discount_percent: int = 0) -> int:
    """Calculate discounted merchandise plus shipping.

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
    return subtotal_cents - discount_cents + SHIPPING_FEE_CENTS


def _order_total(order: dict) -> int:
    """Calculate the total for one order row."""
    return calculate_total(order["prices_cents"], order["discount_percent"])


def build_report(orders: list[dict]) -> dict:
    """Summarize all supplied order rows.

    Args:
        orders: Rows containing item prices and a discount percentage.

    Returns:
        The number of orders and their combined payable amount.

    Raises:
        ValueError: If a row contains invalid prices or a discount.
        KeyError: If a row lacks a required field.
    """
    total_cents = sum(_order_total(order) for order in orders)
    return {"order_count": len(orders), "total_cents": total_cents}


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
    print(build_report(orders))


if __name__ == "__main__":
    main()
