package ar.main;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import ar.jpa.JpaUserAuth;
import ar.web.WebAPI;

public class Main {
 public static void main(String[] args) {
   
  EntityManagerFactory emf = Persistence
       .createEntityManagerFactory("jpa-derby");
   
  SetUpDb setUp = new SetUpDb(emf);
  setUp.setUp();
   
  WebAPI servicio = new WebAPI(1234, new JpaUserAuth(emf));
  servicio.start();
 }
}
