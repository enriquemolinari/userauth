package ar.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

public class JwtToken implements Token {

  private String secret;
  private static final long defaultSecondsSinceNow = 60 * 60 * 1000; //1 hs
  private Long secondsSinceNow;

  public JwtToken(String secret, long secondsSinceNow) {
    this.secret = secret;
    this.secondsSinceNow = secondsSinceNow;
  }

   public JwtToken(String secret) {
    this(secret, defaultSecondsSinceNow);
  }

  private Long expiration() {
    return (new Date().getTime() + this.secondsSinceNow) / 1000;
  }
   
  @Override
  public String token(Map<String, Object> payload) {
    Algorithm algorithmHS = Algorithm.HMAC256(this.secret);
    Map<String, Object> p = new HashMap<String, Object>();
    p.putAll(payload);
    p.put("exp", this.expiration());
    return JWT.create().withPayload(p).sign(algorithmHS);
  }

}
