import entity.Account;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.*;
import dao.AccountDAO;

public class Main {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("accounting_system");
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            Account a1 = new Account(1000);
            Account a2 = new Account(2000);
            em.persist(a1);
            em.persist(a2);

            em.getTransaction().commit();

            System.out.println("Created accounts:");
            System.out.println("Account 1 - Number: " + a1.getNumber() + ", Balance: " + a1.getBalance());
            System.out.println("Account 2 - Number: " + a2.getNumber() + ", Balance: " + a2.getBalance());

            AccountDAO accountDAO = new AccountDAO(emf);
            accountDAO.search(1L);

            accountDAO.transfer(a1.getNumber().intValue(), a2.getNumber().intValue(), 420.69);
        } finally {
            em.close();
            emf.close();
        }
    }
}
