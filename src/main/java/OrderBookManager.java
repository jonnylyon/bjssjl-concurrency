import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class OrderBookManager
{
    private List<AppleOrder> allOrders = new ArrayList<>();

    private final UserManager userManager;

    public OrderBookManager(UserManager userManager) {
        this.userManager = userManager;
    }

    public void submitOrder(AppleOrder newOrder) {
        List<AppleOrder> matchCandidates;

        synchronized (allOrders)
        {
            allOrders.add(newOrder);

            matchCandidates = allOrders.stream().filter(ao -> !ao.isFulfilled() && ao.matches(newOrder)).collect(Collectors.toList());
        }

        for (AppleOrder matchCandidate : matchCandidates) {
            if (tryMatch(newOrder, matchCandidate))
            {
                return;
            }
        }
    }

    private boolean tryMatch(AppleOrder newOrder, AppleOrder matchCandidate) {
        User u1 = userManager.getById(newOrder.getUserId());
        User u2 = userManager.getById(matchCandidate.getUserId());

        synchronized (allOrders)
        {
            try
            {
                boolean usersReady = true;
                usersReady &= u1.startTransaction();
                usersReady &= u2.startTransaction();

                if (usersReady && u1.canExecute(newOrder) && u2.canExecute(matchCandidate))
                {
                    u1.executeOrder(newOrder);
                    u2.executeOrder(matchCandidate);
                    return true;
                }
            } finally {
                u1.transactionFinished();
                u2.transactionFinished();
            }
        }
        return false;
    }
}
