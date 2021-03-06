import java.math.BigDecimal;

public class AppleOrder
{
    private final Long userId;
    private final BuyOrSell buyOrSell;
    private final int numberOfApples;
    private final BigDecimal totalPrice;

    public AppleOrder(Long userId, BuyOrSell buyOrSell, int numberOfApples, BigDecimal totalPrice) {
        this.userId = userId;
        this.buyOrSell = buyOrSell;
        this.numberOfApples = numberOfApples;
        this.totalPrice = totalPrice;
    }

    public Long getUserId()
    {
        return userId;
    }

    public BuyOrSell getBuyOrSell()
    {
        return buyOrSell;
    }

    public int getNumberOfApples()
    {
        return numberOfApples;
    }

    public BigDecimal getTotalPrice()
    {
        return totalPrice;
    }

    public boolean matches(AppleOrder otherOrder) {
        if (this.numberOfApples != otherOrder.numberOfApples) {
            return false;
        }
        if (this.totalPrice.compareTo(otherOrder.totalPrice) != 0) {
            return false;
        }
        if (this.buyOrSell == BuyOrSell.BUY && otherOrder.buyOrSell != BuyOrSell.SELL) {
            return false;
        }
        if (this.buyOrSell == BuyOrSell.SELL && otherOrder.buyOrSell != BuyOrSell.BUY) {
            return false;
        }

        return true;
    }

    public enum BuyOrSell { BUY, SELL }
}
