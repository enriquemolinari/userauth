package ar.web;

import java.util.Map;

import ar.api.UserAuth;
import io.javalin.Javalin;
import io.javalin.http.Handler;

public class WebAPI {

  private int webPort;
  private UserAuth userAuth;

  public WebAPI(int webPort, UserAuth userAuth) {
    this.webPort = webPort;
    this.userAuth = userAuth;
  }

  public void start() {
    Javalin app = Javalin.create().start(this.webPort);
    app.post("/login", login());

    app.exception(RuntimeException.class, (e, ctx) -> {
      ctx.status(401);
      ctx.json(Map.of("result", "error", "message", e.getMessage()));
      // log error in a stream...
    });

    app.exception(Exception.class, (e, ctx) -> {
      ctx.status(500);
      ctx.json(Map.of("result", "error", "message", "Ups... algo se rompió.: " + e.getMessage()));
      // log error in a stream...
    });
  }

  private Handler login() {
    return ctx -> {
      LoginForm form = ctx.bodyAsClass(LoginForm.class);

      Map<String, Object> auth = userAuth.authenticate(form.getUser(), form.getPass())
          .orElseThrow(() -> new RuntimeException("Invalid username or password"));

      // TODO: add secure for PROD
      ctx.res.setHeader("Set-Cookie", "token=" + auth.get("token") + ";" + "HttpOnly; SameSite=strict");

      ctx.json(Map.of("result", "success", "roles", auth.get("roles"), "name", auth.get("name")));
    };
  }
}
