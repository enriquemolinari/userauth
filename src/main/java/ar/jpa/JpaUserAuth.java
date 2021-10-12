package ar.jpa;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import ar.api.UserAuth;
import ar.model.ClientUser;

public class JpaUserAuth implements UserAuth {

  private EntityManagerFactory emf;

  public JpaUserAuth(EntityManagerFactory emf) {
    this.emf = emf;
  }

  @Override
  public Optional<String> authenticate(String user, String password) {
    EntityManager em = emf.createEntityManager();
    try {
      TypedQuery<ClientUser> q = em.createQuery(
          "select u from ClientUser u where u.username = :username and u.password = :password",
          ClientUser.class);

      q.setParameter("username", user);
      q.setParameter("password", password);

      try {
        ClientUser u = q.getSingleResult();

        Algorithm algorithmHS = Algorithm.HMAC256("secret");
        Map<String, Object> payloadClaims = new HashMap<>();
        payloadClaims.put("roles", u.roles());
        return Optional.of(JWT.create().withPayload(payloadClaims).sign(algorithmHS));
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
