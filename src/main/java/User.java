import java.math.BigDecimal;

public class User
{
    private final Long id;
    private BigDecimal funds;
    private int numberOfApples;
    private boolean    loggedIn;

    public User(Long id, BigDecimal credit, int numberOfApples) {
        this.id = id;
        this.funds = credit;
        this.numberOfApples = numberOfApples;
    }

    public Long getId()
    {
        return id;
    }

    public boolean hasFunds(BigDecimal funds) {
        return this.funds.compareTo(funds) >= 0;
    }

    public boolean hasApples(int numberOfApples) {
        return this.numberOfApples >= numberOfApples;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void logIn() {
        loggedIn = true;
    }

    public void logOut() {
        loggedIn = false;
    }

    public boolean canExecute(AppleOrder order) {
        if (!loggedIn) {
            return false;
        }

        if (order.getBuyOrSell() == AppleOrder.BuyOrSell.BUY) {
            if (funds.compareTo(order.getTotalPrice()) < 0) {
                return false;
            }
        } else {
            if (numberOfApples < order.getNumberOfApples()) {
                return false;
            }
        }

        return true;
    }

    public void executeOrder(AppleOrder order) {
        if (order.getBuyOrSell() == AppleOrder.BuyOrSell.BUY) {
            funds = funds.subtract(order.getTotalPrice());
            numberOfApples = numberOfApples + order.getNumberOfApples();
        } else {
            numberOfApples = numberOfApples - order.getNumberOfApples();
            funds = funds.add(order.getTotalPrice());
        }
    }

    @Override
    public String toString() {
        return String.format("ID:%s, funds:%s, apples:%s, logged in:%s", id, funds, numberOfApples, loggedIn);
    }
}
