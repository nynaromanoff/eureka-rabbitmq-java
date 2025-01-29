package com.messageriamicroservice.msavaliacredito.exception;

import lombok.Getter;

public class CommunicationErrorMicroservices extends Exception {
    @Getter
    private Integer status;
    public CommunicationErrorMicroservices(String message, Integer status) {
        super(message);
        this.status = status;
    }
}
