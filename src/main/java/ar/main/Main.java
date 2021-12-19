package ar.main;

import java.io.IOException;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.blueconic.browscap.ParseException;
import com.blueconic.browscap.UserAgentParser;
import com.blueconic.browscap.UserAgentService;

import ar.jpa.JpaUserAuth;
import ar.model.PasetoToken;
import ar.web.WebAPI;

public class Main {
  private static final int MILLISECONDS_SINCE_NOW = 2 * 60 * 60 * 1000 /* 2 hs */;

  public static void main(String[] args) throws IOException, ParseException {

    String tokenSecret = System.getProperty("secret");
    if (tokenSecret == null) {
      throw new IllegalArgumentException("secret value must be passed as a jvm argument");
    }
    
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa-derby");

    SetUpDb setUp = new SetUpDb(emf);
    setUp.setUp();

    UserAgentParser parser = new UserAgentService().loadParser();

    WebAPI servicio = new WebAPI(1234,
        new JpaUserAuth(emf, new PasetoToken(tokenSecret, MILLISECONDS_SINCE_NOW)), parser);
    servicio.start();
  }
}
