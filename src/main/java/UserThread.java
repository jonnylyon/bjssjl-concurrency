import java.math.BigDecimal;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class UserThread implements Runnable
{
    private final OrderBookManager orderBookManager;
    private final UserManager userManager;
    private final BigDecimal basePrice;

    private Long userId;

    public UserThread(OrderBookManager orderBookManager, UserManager userManager, BigDecimal basePrice) {
        this.orderBookManager = orderBookManager;
        this.userManager = userManager;
        this.basePrice = basePrice;
    }

    @Override public void run()
    {
        this.userId = userManager.registerNewUser(new BigDecimal("10"), 10);

        for (int i = 0; i < 5; i++)
        {
            userManager.loginUser(this.userId);
            System.out.println("User logged in: " + this.userId);

            final Timer timer = new Timer("User order timer");
            final TimerTask timerTask = new TimerTask() {
                @Override
                public void run()
                {
                    UserThread.this.placeOrder();
                }
            };
            timer.scheduleAtFixedRate(timerTask, 0L, 5L);

            try
            {
                int loginTime = new Random().nextInt(20000);
                Thread.sleep(loginTime);

                timer.cancel();
                userManager.logoutUser(userId);
                System.out.println("User logged out: " + this.userId);

                Thread.sleep(40000);
            }
            catch (InterruptedException e)
            {
                throw new RuntimeException(e);
            }
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
