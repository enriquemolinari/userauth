package ar.main;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import ar.jpa.JpaUserAuth;
import ar.model.JwtToken;
import ar.web.WebAPI;

public class Main {
  private static final int SECONDS_SINCE_NOW = 6 * 60 * 60 * 1000 /* 6 hs */;
  private static final String JWT_SECRET = "secret";

  public static void main(String[] args) {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa-derby");

    SetUpDb setUp = new SetUpDb(emf);
    setUp.setUp();

    WebAPI servicio = new WebAPI(1234,
        new JpaUserAuth(emf, 
            new JwtToken(JWT_SECRET, SECONDS_SINCE_NOW)));
    servicio.start();
  }
}
