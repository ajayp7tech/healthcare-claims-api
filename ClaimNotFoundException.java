package com.ajayp.claims.exception;

public class ClaimNotFoundException extends RuntimeException {
    public ClaimNotFoundException(String id) {
        super("Claim not found: " + id);
    }
}
