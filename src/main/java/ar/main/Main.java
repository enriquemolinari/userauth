package ar.main;

import ar.web.WebAPI;

public class Main {
 public static void main(String[] args) {
  WebAPI servicio = new WebAPI(1234);
  servicio.start();
 }
}
