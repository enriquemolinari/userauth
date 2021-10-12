package ar.api;

import java.util.Optional;

public interface UserAuth {
 
 Optional<String> authenticate(String user, String password);

}
