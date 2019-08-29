package pl.pawelec.webshop.controller.attributes;

public enum AlertAttribute {
    SUCCESS,
    INFO,
    WARNING,
    DANGER
    ;

    public String getNameAsLowerCase() {
        return name().toLowerCase();
    }
}
