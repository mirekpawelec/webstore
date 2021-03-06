/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.webshop.model.role;
/**
 *
 * @author mirek
 */
public enum UserRole {
    ROLE_CLIENT("Client"),
    ROLE_USER("User"),
    ROLE_ADMIN("Manager"),
    ROLE_DBA("Admin");
    
    private String description;

    private UserRole(String description) {
        this.description = description;
    }

    public String getName() {
        return this.name();
    }
        
    public String getDescription() {
        return description;
    }    
}
