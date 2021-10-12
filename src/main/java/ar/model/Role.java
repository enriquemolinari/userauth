package ar.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Role {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  private String roleName;

  public Role(String roleName) {
    this.roleName = roleName;
  }

  @Override
  public String toString() {
    return roleName;
  }

//Below private methods are just for Hiberante
  protected Role() {

  }

  private String getRoleName() {
    return roleName;
  }

  private void setRoleName(String roleName) {
    this.roleName = roleName;
  }

  private Long getId() {
    return id;
  }

  private void setId(Long id) {
    this.id = id;
  }

}
