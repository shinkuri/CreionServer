import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class Database {

    private static Database INSTANCE;

    private final SessionFactory sessionFactory;

    private Database() {
        sessionFactory = new Configuration().configure().buildSessionFactory();
    }

    static Database getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Database();
        }
        return INSTANCE;
    }

    /**
     * Persist object using a transaction.
     *
     * @param o Object to persist
     */
    void save(Object o) {
        try (final Session session = sessionFactory.openSession()) {
            final Transaction transaction = session.beginTransaction();
            session.save(o);
            transaction.commit();
        } catch (HibernateException hibernateException) {
            sessionFactory.getCurrentSession().getTransaction().rollback();
            LogManager.getLogger().log(Level.ERROR, "Failed to persist data");
        }
    }
}
