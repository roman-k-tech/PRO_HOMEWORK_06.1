import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.*;

import static org.junit.Assert.*;

public class UserTest {

    private static final String SETTINGS = "SETTINGS";

    private EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;
    private EntityTransaction transaction;

    @Before
    public void setUp() throws Exception {
        entityManagerFactory = Persistence.createEntityManagerFactory(SETTINGS);
        entityManager = entityManagerFactory.createEntityManager();
        transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.persist(new User("Roman", new Account(1000)));
        entityManager.persist(new User("Evgen", new Account(500)));

        transaction.commit();
    }

    @After
    public void tearDown() throws Exception {
        entityManager.close();
        entityManagerFactory.close();
    }

    @Test
    public void insertTest() throws Exception
    {
        transaction.begin();
        User user = new User("Irina", new Account(222));
        entityManager.persist(user);
        transaction.commit();

        int accountID = user.getAccount().getId();
        Query query = entityManager.createQuery("SELECT testAcc FROM Account testAcc WHERE testAcc.id=:id", Account.class);
        query.setParameter("id", accountID);
        Account testAcc = (Account) query.getSingleResult();

        assertTrue(222 == testAcc.getAmount());
    }

    @Test
    public void deleteTest() throws Exception
    {
        Query query = entityManager.createQuery("SELECT testUser FROM User testUser WHERE testUser.name=:name", User.class);
        query.setParameter("name", "Roman");
        User testUser = (User) query.getSingleResult();

        Account testAcc = testUser.getAccount();
        int accountID = testAcc.getId();
        testUser.setAccount(null);

        transaction.begin();
        entityManager.remove(testAcc);
        transaction.commit();

        query = entityManager.createQuery("SELECT testAcc FROM Account testAcc WHERE testAcc.id=:id", Account.class);
        query.setParameter("id", accountID);
        Account deletedAcc = null;
        try {
            deletedAcc = (Account) query.getSingleResult();
        }
        catch (NoResultException e) { }
        assertNull(deletedAcc);
    }

    @Test
    public void doTransaction() throws Exception
    {
        transaction.begin();
        Account account1 = new Account(320);
        Account account2 = new Account(250);
        User user1 = new User("Ivan", account1);
        User user2 = new User("Kokoko", account2);
        entityManager.persist(user1);
        entityManager.persist(user2);
        MoneyTransaction moneyTransaction = new MoneyTransaction(account1, account2);
        moneyTransaction.processTransation(-500);


        entityManager.persist(moneyTransaction);
        transaction.commit();
        assertFalse(moneyTransaction.isFinished());
        //assertTrue(account1.getAmount() == 420);
    }
}