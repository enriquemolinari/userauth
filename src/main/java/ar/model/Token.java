package ar.model;

import java.util.Map;

public interface Token {

  String token(Map<String, Object> payload);
  
}
