package org.example.dto;

public class UserUpdateRequestDto {

    private String userName;
    private String email;


    public UserUpdateRequestDto() {
    }

    public UserUpdateRequestDto(String userName, String email) {
        this.userName = userName;
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
