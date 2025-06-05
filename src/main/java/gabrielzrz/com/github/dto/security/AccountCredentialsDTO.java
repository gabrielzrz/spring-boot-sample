package gabrielzrz.com.github.dto.security;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 * @author Zorzi
 */
public class AccountCredentialsDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -7594649086592956412L;

    private String username;
    private String password;
    private String fullname;

    //Methods
    public AccountCredentialsDTO() {
    }

    public AccountCredentialsDTO(String username, String password, String fullname) {
        this.username = username;
        this.password = password;
        this.fullname = fullname;
    }

    //Getters && Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    //Equals && HashCode
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        AccountCredentialsDTO that = (AccountCredentialsDTO) o;
        return Objects.equals(username, that.username) && Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password);
    }
}
