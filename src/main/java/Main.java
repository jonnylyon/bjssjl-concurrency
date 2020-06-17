import java.math.BigDecimal;

public class Main
{

    public static void main(String[] args) {
        UserManager um = new UserManager();
        OrderBookManager obm = new OrderBookManager(um);

        UserThread ut = new UserThread(obm, um, new BigDecimal("0.80"));
        UserThread ut1 = new UserThread(obm, um, new BigDecimal("0.85"));
        UserThread ut2 = new UserThread(obm, um, new BigDecimal("0.90"));
        UserThread ut3 = new UserThread(obm, um, new BigDecimal("0.75"));
        UserThread ut4 = new UserThread(obm, um, new BigDecimal("0.70"));
        UserThread ut5 = new UserThread(obm, um, new BigDecimal("0.65"));
        UserThread ut6 = new UserThread(obm, um, new BigDecimal("1.00"));
        UserThread ut7 = new UserThread(obm, um, new BigDecimal("0.95"));

        new Thread(ut).start();
        new Thread(ut1).start();
        new Thread(ut2).start();
        new Thread(ut3).start();
        new Thread(ut4).start();
        new Thread(ut5).start();
        new Thread(ut6).start();
        new Thread(ut7).start();

    }
}
