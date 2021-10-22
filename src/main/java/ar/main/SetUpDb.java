package ar.main;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

import ar.model.Role;
import ar.model.ClientUser;

public class SetUpDb {

  private EntityManagerFactory emf;
  
  public SetUpDb(EntityManagerFactory emf) {
    this.emf = emf;
  }
  
  public void setUp() {
    
    EntityManager em = emf.createEntityManager();
    EntityTransaction tx = em.getTransaction();
    try {
     tx.begin();

     ClientUser u1 = new ClientUser(1L, "juser", "juser123", new Role[] {new Role("SIMPLE")});
     em.persist(u1);
      
     ClientUser u2 = new ClientUser(2L, "guser", "guser123", new Role[] {new Role("SIMPLE")});
     em.persist(u2);

     ClientUser u3 = new ClientUser(3L, "admin", "admin123", new Role[] {new Role("ADMIN")});
     em.persist(u3);   
     
     tx.commit();
    } catch (Exception e) {
     tx.rollback();
     throw new RuntimeException(e);
    } finally {
     if (em != null && em.isOpen())
      em.close();
   }  
  }
}
