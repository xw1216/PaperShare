package tech.outspace.papershare.model.enumerate;

public enum EFocusType {
    WATCH("WATCH"),
    STAR("STAR"),
    FORK("FORK");

    private String type;

    EFocusType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
