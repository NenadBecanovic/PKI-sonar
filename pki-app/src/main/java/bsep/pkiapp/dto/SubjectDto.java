package bsep.pkiapp.dto;

import bsep.pkiapp.model.User;

public class SubjectDto {
    public String displayName;
    public String email;

    public SubjectDto(String displayName, String email) {
        this.displayName = displayName;
        this.email = email;
    }

    public SubjectDto(User user) {
        this.email = user.getEmail();
        this.displayName = user.getName() + " " + user.getSurname();
    }
}
