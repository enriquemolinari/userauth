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
    app.post("/test", test());
//  app.before(ctx -> {
//   System.out.println(ctx.headerMap());
//  });

//  app.after(ctx -> {
//   ctx.res.addHeader("Set-Cookie", "token=1234; HttpOnly; SameSite=strict; Secure");
//   //response.setHeader("Set-Cookie", "key=value; HttpOnly; SameSite=strict")
//  });

    app.exception(RuntimeException.class, (e, ctx) -> {
      ctx.json(Map.of("result", "error", "message", e.getMessage()));
      // log error in a stream...
    });

    app.exception(Exception.class, (e, ctx) -> {
      ctx.json(Map.of("result", "error", "message", "Ups... algo se rompió.: " + e.getMessage()));
      // log error in a stream...
    });
  }

  private Handler test() {
    return ctx -> {
      // read token cookie...
      // var value = ctx.cookie("token");
      LoginForm form = ctx.bodyAsClass(LoginForm.class);

      String token = userAuth.authenticate(form.getUser(), form.getPass())
          .orElseThrow(() -> new RuntimeException("Invalid Username or password"));

      ctx.json(Map.of("result", "success", "token", token));
    };
  }
}
