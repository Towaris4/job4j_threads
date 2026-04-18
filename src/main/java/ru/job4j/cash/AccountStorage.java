package ru.job4j.cash;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import java.util.HashMap;
import java.util.Optional;

@ThreadSafe
public class AccountStorage {

    @GuardedBy("this")
    private final HashMap<Integer, Account> accounts = new HashMap<>();

    public synchronized boolean add(Account account) {
        if (accounts.containsKey(account.id())) {
            return false;
        }
        accounts.put(account.id(), account);
        return true;
    }

    public synchronized boolean update(Account account) {
        if (!accounts.containsKey(account.id())) {
            return false;
        }
        accounts.replace(account.id(), account);
        return true;
    }

    public synchronized void delete(int id) {
        accounts.remove(id);
    }

    public synchronized Optional<Account> getById(int id) {
        return Optional.ofNullable(accounts.get(id));
    }

    public synchronized boolean transfer(int fromId, int toId, int amount) {
        Optional<Account> accFrom = getById(fromId);
        Optional<Account> accTo = getById(toId);
        if (accFrom.isEmpty() || accTo.isEmpty()) {
            return false;
        }
        Account accountFrom = accFrom.get();
        Account accountTo = accTo.get();
        if (accountFrom.amount() < amount) {
            return false;
        }
        update(new Account(fromId, accountFrom.amount() - amount));
        update(new Account(toId, accountTo.amount() + amount));
        return true;
    }
}

