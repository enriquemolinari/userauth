package ar.model;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

@Entity
public class ClientUser {

 @Id
 @GeneratedValue(strategy = GenerationType.AUTO)
 private Long id;
 private String username;
 private String password;
 
 @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
 @JoinColumn(name = "id_user")
 private List<Role> roles;
 
 public ClientUser(String username, String password) {
  this(username, password, new Role[0]);
 }

 public ClientUser(String username, String password, Role... roles) {
  this.username = username;
  //TODO: This must be encrypted!
  this.password = password;
  this.roles = Arrays.<Role>asList(roles);
 }

 public Map<String, Object> toMap() {
   return Map.of("roles", this.roles(), "name", this.username, "id", this.id);
 }
 
 public List<String> roles() {
   return this.roles.stream().map((r) -> {
     return r.toString();
   }).collect(Collectors.toList());
 }
 
 public String name() {
   return this.username;
 }
 
 public Long userId() {
  return this.id;
 }
 
 //Below private methods are just for Hiberante

 protected ClientUser() {
  
 }
 
 private Long getId() {
  return id;
 }

 private void setId(Long id) {
  this.id = id;
 }

 private String getUsername() {
  return username;
 }

 private void setUsername(String username) {
  this.username = username;
 }

 private String getPassword() {
  return password;
 }

 private void setPassword(String password) {
  this.password = password;
 }
}
