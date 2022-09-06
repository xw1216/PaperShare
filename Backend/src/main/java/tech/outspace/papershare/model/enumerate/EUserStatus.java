package tech.outspace.papershare.model.enumerate;

public enum EUserStatus {

    NORMAL("NORMAL"),
    BAN("BAN");

    private String status;

    EUserStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isBaned() {
        return this == BAN;
    }
}
