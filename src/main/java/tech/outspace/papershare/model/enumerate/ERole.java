package tech.outspace.papershare.model.enumerate;

public enum ERole {
    USER("USER"),
    ADMIN("ADMIN");

    private String role;

    ERole(String role) {
        this.role = role;
    }

    public String getRoleStr() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
