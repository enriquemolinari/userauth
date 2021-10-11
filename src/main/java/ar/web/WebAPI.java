package ar.web;

import java.util.Map;

import io.javalin.Javalin;
import io.javalin.http.Handler;

public class WebAPI {

 private int webPort;

 public WebAPI(int webPort) {
  this.webPort = webPort;
 }

 public void start() {
  Javalin app = Javalin.create().start(this.webPort);
  app.get("/test", test());
//  app.before(ctx -> {
//   System.out.println(ctx.headerMap());
//  });
  
//  app.after(ctx -> {
//   ctx.res.addHeader("Set-Cookie", "token=1234; HttpOnly; SameSite=strict; Secure");
//   //response.setHeader("Set-Cookie", "key=value; HttpOnly; SameSite=strict")
//  });
  
//  app.exception(PersonaException.class, (e, ctx) -> {
//   ctx.json(Map.of("result", "error", "errors", e.toMap()));
//   // log error in a stream...
//  });  
  
//  app.exception(Exception.class, (e, ctx) -> {
//   ctx.json(Map.of("result", "error", "message", "Ups... algo se rompió.: " + e.getMessage()));
//   // log error in a stream...
//  });
 }

 private Handler test() {
  return ctx -> {
   //read token cookie...
   //var value = ctx.cookie("token");

   ctx.json(Map.of("result", "success", "test", "tesssssstt"));
  };
 }

}
