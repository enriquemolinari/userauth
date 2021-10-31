package ar.jpa;

import java.util.Map;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import ar.api.UserAuth;
import ar.model.ClientUser;
import ar.model.Token;

public class JpaUserAuth implements UserAuth {

  private EntityManagerFactory emf;
  private Token token;

  public JpaUserAuth(EntityManagerFactory emf, Token token) {
    this.emf = emf;
    this.token = token;
  }

  @Override
  public Optional<Map<String, Object>> authenticate(String user, String password) {
    EntityManager em = emf.createEntityManager();
    try {
      TypedQuery<ClientUser> q = em.createQuery(
          "select u from ClientUser u where u.username = :username and u.password = :password",
          ClientUser.class);

      q.setParameter("username", user);
      q.setParameter("password", password);

      try {

        ClientUser u = q.getSingleResult();

        return Optional.of(Map.of("token", token.token(u.toMap()), "user", u.toMap()));

      } catch (NoResultException e) {
        return Optional.empty();
      }

    } catch (Exception e) {
      throw new RuntimeException(e);
    } finally {
      if (em != null && em.isOpen())
        em.close();
    }
  }
}
