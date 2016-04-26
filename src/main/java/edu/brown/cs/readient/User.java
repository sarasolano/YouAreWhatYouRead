package edu.brown.cs.readient;

import java.util.Arrays;

import edu.brown.cs.db.QueryManager;

public class User {
  private String username;
  private String firstName;
  private String lastName;
  private byte[] passwordHash;
  private byte[] salt;

  public User(String uName, String fName, String lName, String password,
      String slt) {
    this.username = uName;
    this.firstName = fName;
    this.lastName = lName;
    this.passwordHash = QueryManager.stringToByte(password);
    this.salt = QueryManager.stringToByte(slt);
  }

  public String getUsername() {
    return username;
  }

  public String getName() {
    return firstName + " " + lastName;
  }

  public byte[] getPasswordHash() {
    return passwordHash;
  }

  public byte[] getSalt() {
    return salt;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + Arrays.hashCode(passwordHash);
    result = prime * result + Arrays.hashCode(salt);
    result = prime * result + ((username == null) ? 0 : username.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    User other = (User) obj;
    if (!Arrays.equals(passwordHash, other.passwordHash))
      return false;
    if (!Arrays.equals(salt, other.salt))
      return false;
    if (username == null) {
      if (other.username != null)
        return false;
    } else if (!username.equals(other.username))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "User [username=" + username + ", firstName=" + firstName
        + ", lastName=" + lastName + "]";
  }
}
