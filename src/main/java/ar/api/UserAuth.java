package ar.api;

import java.util.Map;
import java.util.Optional;

public interface UserAuth {
 
 Optional<Map<String, Object>> authenticate(String user, String password);

}
