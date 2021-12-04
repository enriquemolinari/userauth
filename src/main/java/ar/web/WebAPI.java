package ar.web;

import java.util.Map;

import com.blueconic.browscap.Capabilities;
import com.blueconic.browscap.UserAgentParser;

import ar.api.UserAuth;
import io.javalin.Javalin;
import io.javalin.http.Handler;

public class WebAPI {

  private static final int CHROME_MINIMUM_VERSION_SUPPORTED = 51;
  private static final String BROWSER_SUPPORTED = "chrome";
  private int webPort;
  private UserAuth userAuth;
  private UserAgentParser userAgent;

  public WebAPI(int webPort, UserAuth userAuth, UserAgentParser userAgent) {
    this.webPort = webPort;
    this.userAuth = userAuth;
    this.userAgent = userAgent;
  }

  public void start() {
    Javalin app = Javalin.create().start(this.webPort);
    app.post("/login", login());
    app.post("/logout", logout());

    app.exception(RuntimeException.class, (e, ctx) -> {
      ctx.status(401);
      ctx.json(Map.of("result", "error", "message", e.getMessage()));
      // log error in a stream...
    });

    app.exception(OldBrowserException.class, (e, ctx) -> {
      ctx.status(500);
      ctx.json(Map.of("result", "error", "message",
          "Your browser is not supported. Only this and that from version X and above."));
      // log error in a stream...
    });

    app.exception(Exception.class, (e, ctx) -> {
      ctx.status(500);
      ctx.json(Map.of("result", "error", "message", "Ups... something when wrong."));
      // log error in a stream...
    });

    app.before(ctx -> {
      String requestUserAgent = ctx.req.getHeader("User-Agent");
      final Capabilities capabilities = this.userAgent.parse(requestUserAgent);
      // according to this:
      // https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Set-Cookie/SameSite
      // only allow browsers that support same-site strict cookies
      // As example, only Chrome greater/equal than version 51
      final String browser = capabilities.getBrowser();
      final String browserMajorVersion = capabilities.getBrowserMajorVersion();
     
      if (!BROWSER_SUPPORTED.contains(browser.toLowerCase())) {
        throw new OldBrowserException();
      }
      if (CHROME_MINIMUM_VERSION_SUPPORTED > Integer.valueOf(browserMajorVersion)) {
        throw new OldBrowserException();
      }
    });
  }

  private Handler logout() {
    return ctx -> {
      // TODO: if you want register login/logout time

      // just remove the token cookie
      ctx.removeCookie("token");
      ctx.json(Map.of("result", "success"));
    };
  }

  private Handler login() {
    return ctx -> {
      LoginForm form = ctx.bodyAsClass(LoginForm.class);

      Map<String, Object> auth = userAuth.authenticate(form.getUser(), form.getPass())
          .orElseThrow(() -> new RuntimeException("Invalid username or password"));

      // TODO: add secure for PROD
      ctx.res.setHeader("Set-Cookie",
          "token=" + auth.get("token") + ";path=/; HttpOnly; SameSite=strict");

      ctx.json(Map.of("result", "success", "user", auth.get("user")));
    };
  }
}
