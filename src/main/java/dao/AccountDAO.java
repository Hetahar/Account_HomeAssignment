package dao;

import entity.Account;
import jakarta.persistence.*;

public class AccountDAO {

    private EntityManagerFactory emf;

    public AccountDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public void create(Account account) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        tx.begin();
        em.persist(account);
        tx.commit();
        em.close();
    }

    public void delete(Account account) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        tx.begin();
        Account managedAccount = em.find(Account.class, account.getNumber());
        if (managedAccount != null) {
            em.remove(managedAccount);
        }
        tx.commit();
        em.close();
    }

    public Account search(Long number) {
        EntityManager em = emf.createEntityManager();
        try {
            Account account = em.find(Account.class, number);
            if (account != null) {
                System.out.println("Account found: Number = " + account.getNumber() + ", Balance = " + account.getBalance());
            } else {
                System.out.println("Account with number " + number + " not found.");
            }
            return account;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            em.close();
        }
    }

    public void transfer(int sourceAccountNumber, int destinationAccountNumber, double amount) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            Account sourceAccount = em.find(Account.class, (long) sourceAccountNumber, LockModeType.OPTIMISTIC);
            Account destinationAccount = em.find(Account.class, (long) destinationAccountNumber, LockModeType.OPTIMISTIC);

            if (sourceAccount == null || destinationAccount == null) {
                throw new IllegalArgumentException("One or both accounts not found.");
            }

            if (sourceAccount.getBalance() < amount) {
                throw new IllegalArgumentException("Insufficient funds in source account.");
            }

            sourceAccount.setBalance(sourceAccount.getBalance() - amount);
            destinationAccount.setBalance(destinationAccount.getBalance() + amount);

            em.merge(sourceAccount);
            em.merge(destinationAccount);

            tx.commit();
            System.out.println("Transfer of " + amount + " from account " + sourceAccountNumber + " to account " + destinationAccountNumber + " completed.");
        } catch (OptimisticLockException ole) {
            System.out.println("Transfer failed due to concurrent modification. Please retry.");
            if (tx.isActive()) {
                tx.rollback();
            }
        } finally {
            em.close();
        }
    }
}
