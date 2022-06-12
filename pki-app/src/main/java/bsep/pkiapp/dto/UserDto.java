package bsep.pkiapp.dto;


import bsep.pkiapp.model.User;
import lombok.ToString;

@ToString
public class UserDto {

    private String name;
    private String surname;
    private String email;
    private String password;

    public UserDto() {}

    public UserDto(String name, String surname, String email, String password) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
    }

    public UserDto(String name, String surname, String email) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = "";
    }

    public UserDto(User user) {
        this.name = user.getName();
        this.surname = user.getSurname();
        this.email = user.getEmail();
        this.password = "";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
