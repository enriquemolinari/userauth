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
  private static final int OFFLINE_MILLISECONDS_SINCE_NOW = 7 * 60 * 60 * 1000 * 24 /* 7 dias */;

  public static void main(String[] args) throws IOException, ParseException {

    String tokenSecret = System.getProperty("secret");

    if (tokenSecret == null) {
      throw new IllegalArgumentException("secret value must be passed as a jvm argument");
    }

    Boolean localTunnel = Boolean.valueOf(System.getProperty("test-with-lt"));

    if (localTunnel == null) {
      localTunnel = false;
    }

    Boolean offlineFirst = Boolean.valueOf(System.getProperty("offline"));

    if (offlineFirst == null) {
      offlineFirst = false;
    }

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa-derby");

    SetUpDb setUp = new SetUpDb(emf);
    setUp.setUp();

    UserAgentParser parser = new UserAgentService().loadParser();

    int expirationMilliseconds = (offlineFirst) ? OFFLINE_MILLISECONDS_SINCE_NOW
        : MILLISECONDS_SINCE_NOW;

    WebAPI servicio = new WebAPI(1234, localTunnel,
        new JpaUserAuth(emf, new PasetoToken(tokenSecret, expirationMilliseconds)), parser);
    servicio.start();
  }
}
