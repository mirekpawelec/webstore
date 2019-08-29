package pl.pawelec.webshop.controller.attributes;

public enum CrudAttribute {
    CREATE,
    UPDATE,
    DELETE,
    ERROR
    ;

    public String getNameAsLowerCase() {
        return name().toLowerCase();
    }
}
