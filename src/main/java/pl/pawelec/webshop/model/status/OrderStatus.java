/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.webshop.model.status;

/**
 *
 * @author mirek
 */
public enum OrderStatus {
    ED("Edit"),
    WT("Wait"),
    AC("Active"),
    RE("Realization"),
    CA("Canceled"),
    FI("Completed");
    
    private String description;

    private OrderStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
