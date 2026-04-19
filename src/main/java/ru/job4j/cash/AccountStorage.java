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
        return accounts.putIfAbsent(account.id(), account) == null;
    }

    public synchronized boolean update(Account account) {
        return accounts.replace(account.id(), account) != null;
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

