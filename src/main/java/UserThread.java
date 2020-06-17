import java.math.BigDecimal;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class UserThread implements Runnable
{
    private final OrderBookManager orderBookManager;
    private final UserManager userManager;
    private final BigDecimal basePrice;

    private final Timer timer;

    private Long userId;

    public UserThread(OrderBookManager orderBookManager, UserManager userManager, BigDecimal basePrice) {
        this.orderBookManager = orderBookManager;
        this.userManager = userManager;
        this.basePrice = basePrice;

        this.timer = new Timer("User order timer");
    }

    @Override public void run()
    {
        this.userId = userManager.registerNewUser(new BigDecimal("10"), 10);
        userManager.loginUser(this.userId);

        printUser();

        final TimerTask timerTask = new TimerTask() {
            @Override
            public void run()
            {
                UserThread.this.placeOrder();
            }
        };
        this.timer.scheduleAtFixedRate(timerTask, 0L, 5L);

        try
        {
            Thread.sleep(20000);

            this.timer.cancel();
            userManager.logoutUser(userId);

            Thread.sleep(5000);
        }
        catch (InterruptedException e)
        {
            throw new RuntimeException(e);
        }

        printUser();
    }

    private void placeOrder() {
        int numberOfApples = randomNumberOfApples();
        BigDecimal totalPrice = randomItemPrice().multiply(new BigDecimal(numberOfApples));
        AppleOrder.BuyOrSell buyOrSell = randomBuyOrSell();

        orderBookManager.submitOrder(new AppleOrder(this.userId, buyOrSell, numberOfApples, totalPrice));
    }

    private int randomNumberOfApples() {
        return new Random().nextInt(15);
    }

    private AppleOrder.BuyOrSell randomBuyOrSell() {
        boolean randomBool = new Random().nextBoolean();
        return randomBool ? AppleOrder.BuyOrSell.BUY : AppleOrder.BuyOrSell.SELL;
    }

    private BigDecimal randomItemPrice() {
        BigDecimal extraPennies = new BigDecimal(new Random().nextInt(40)).divide(BigDecimal.valueOf(100L));

        return basePrice.add(extraPennies);
    }

    private void printUser() {
        System.out.println(userManager.getById(userId).toString());
    }
}
