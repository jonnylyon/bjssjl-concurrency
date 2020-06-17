import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class OrderBookManager
{
    private List<AppleOrder> openOrders = new ArrayList<>();

    private final UserManager userManager;

    public OrderBookManager(UserManager userManager) {
        this.userManager = userManager;
    }

    public void submitOrder(AppleOrder newOrder) {
        List<AppleOrder> matchCandidates = new ArrayList<>();
        for (AppleOrder ao : openOrders) {
            if (ao.matches(newOrder)) {
                matchCandidates.add(ao);
            }
        }

        for (AppleOrder matchCandidate : matchCandidates) {
            if (tryMatch(newOrder, matchCandidate)) {
                openOrders.remove(matchCandidate);
                return;
            }
        }

        // no match succeeded, add new order to open orders list
        openOrders.add(newOrder);
    }

    private boolean tryMatch(AppleOrder newOrder, AppleOrder matchCandidate) {
        User u1 = userManager.getById(newOrder.getUserId());
        User u2 = userManager.getById(matchCandidate.getUserId());

        if (u1.canExecute(newOrder) && u2.canExecute(matchCandidate)) {
            u1.executeOrder(newOrder);
            u2.executeOrder(matchCandidate);
            return true;
        }
        return false;
    }
}
