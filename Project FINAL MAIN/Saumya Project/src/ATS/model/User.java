package ATS.model;

public class User {

    private int userId;
    private String fullName;
    private String email;
    private String password;
    private String role;
    private String createdAt;
    private String accountStatus;

    
     // Default constructor.
     // is used to set all default value = null
    // means in starting we don't know that what is the details .
    public User() {
    }

    
     // Parameterized constructor.
     // to fill the details in this columns .
    // made this for using this all things at same time .
    // like if in another class i call user us = new user();
    // then all values come together like group .
    public User(int userId, String fullName, String email, String password,
                String role, String createdAt, String accountStatus) {
        this.userId = userId;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.role = role;
        this.createdAt = createdAt;
        this.accountStatus = accountStatus;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }


    //  Returns a string representation of the user object.

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", accountStatus='" + accountStatus + '\'' +
                '}';
    }
}
