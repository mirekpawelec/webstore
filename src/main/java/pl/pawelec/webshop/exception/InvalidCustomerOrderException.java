/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.webshop.exception;

/**
 * @author mirek
 */
public class InvalidCustomerOrderException extends RuntimeException {
    private String message;

    public InvalidCustomerOrderException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
