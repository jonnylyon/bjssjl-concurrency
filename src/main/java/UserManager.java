import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UserManager
{
    private Map<Long, User> users = new HashMap<>();

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

    public void loginUser(Long userId) {
        users.get(userId).logIn();
    }

    public void logoutUser(Long userId) {
        users.get(userId).logOut();
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
