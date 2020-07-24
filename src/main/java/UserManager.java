import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;

public class UserManager
{
    private Semaphore semaphore = new Semaphore(4);

    private Map<Long, User> users = new ConcurrentHashMap<>();

    private Long idCounter = 0L;

    public Long registerNewUser(BigDecimal initialFunds, int initialNumberOfApples) {
        Long id = nextAvailableId();
        User user = new User(id, initialFunds, initialNumberOfApples);
        users.put(id, user);

        return id;
    }

    public boolean hasFunds(Long userId, BigDecimal funds) {
        return users.get(userId).hasFunds(funds);
    }

    public boolean hasApples(Long userId, int numberOfApples) {
        return users.get(userId).hasApples(numberOfApples);
    }

    public boolean isLoggedIn(Long userId) {
        return users.get(userId).isLoggedIn();
    }

    public void loginUser(Long userId)
    {
        try
        {
            semaphore.acquire();
        } catch (InterruptedException ex) {
            throw new RuntimeException("Interrupted", ex);
        }

        users.get(userId).logIn();
    }

    public void logoutUser(Long userId) {
        users.get(userId).logOut();
        semaphore.release();
    }

    public User getById(Long userId) {
        return users.get(userId);
    }

    private Long nextAvailableId() {
        Long result = idCounter;
        idCounter++;
        return result;
    }
}
