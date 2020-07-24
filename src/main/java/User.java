import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

public class User
{
    private final Long          id;
    private       BigDecimal    funds;
    private       int           numberOfApples;
    private       AtomicBoolean loggedIn = new AtomicBoolean(false);

    private ReentrantLock userLock = new ReentrantLock();

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
        return loggedIn.get();
    }

    public void logIn() {
        userLock.lock();
        try
        {
            loggedIn.set(true);
        } finally {
            userLock.unlock();
        }
    }

    public void logOut() {
        userLock.lock();
        try
        {
            loggedIn.set(false);
        } finally {
            userLock.unlock();
        }
    }

    public boolean canExecute(AppleOrder order) {
        if (order.isFulfilled()) {
            return false;
        }

        if (order.getBuyOrSell() == AppleOrder.BuyOrSell.BUY)
        {
            if (funds.compareTo(order.getTotalPrice()) < 0)
            {
                return false;
            }
        }
        else
        {
            if (numberOfApples < order.getNumberOfApples())
            {
                return false;
            }
        }

        return true;
    }

    public boolean startTransaction() {
        try
        {
            if (!this.isLoggedIn()) {
                return false;
            }

            if (!this.userLock.tryLock(1L, TimeUnit.SECONDS)) {
                System.out.println("Lock not acquired after 1 second, user " + this.id);
                return false;
            }

            boolean loggedIn = this.isLoggedIn();
            if (!loggedIn)
            {
                System.out.println("Transaction declined after lock acquired; user has been logged out, user " + this.id);
            }
            return loggedIn;
        }
        catch (InterruptedException e)
        {
            throw new RuntimeException("Interrupted while waiting for lock", e);
        }
    }

    public void transactionFinished() {
        if (this.userLock.isHeldByCurrentThread())
        {
            this.userLock.unlock();
        }
    }

    public void executeOrder(AppleOrder order) {
        if (order.getBuyOrSell() == AppleOrder.BuyOrSell.BUY) {
            funds = funds.subtract(order.getTotalPrice());
            numberOfApples = numberOfApples + order.getNumberOfApples();
        } else {
            numberOfApples = numberOfApples - order.getNumberOfApples();
            funds = funds.add(order.getTotalPrice());
        }

        order.markAsFulfilled();
    }

    @Override
    public String toString() {
        return String.format("ID:%s, funds:%s, apples:%s, logged in:%s", id, funds, numberOfApples, loggedIn);
    }
}
