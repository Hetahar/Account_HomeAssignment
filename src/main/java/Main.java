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
            AccountDAO accountDAO = new AccountDAO(emf);

            Account a1 = new Account(1000);
            Account a2 = new Account(2000);
            accountDAO.create(a1);
            accountDAO.create(a2);

            em.getTransaction().commit();

            System.out.println("Created accounts:");
            System.out.println("Account 1 - Number: " + a1.getNumber() + ", Balance: " + a1.getBalance());
            System.out.println("Account 2 - Number: " + a2.getNumber() + ", Balance: " + a2.getBalance());

            accountDAO.search(1L);

            accountDAO.transfer(1,2, 420.69);
            accountDAO.search(1L);
            accountDAO.search(2L);

            accountDAO.transfer(2,1, 69.42);
            accountDAO.search(1L);
            accountDAO.search(2L);

        } finally {
            em.close();
            emf.close();
        }
    }
}
