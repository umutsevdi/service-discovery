package com.company.executor;

import com.company.exception.InvalidRequestException;

public interface Executor {
    /**
     * execute
     * 
     * @param args String : Arguments received from the TCP Request
     * @return response String : Response from the class
     * @throws InvalidRequestException : When invalid arguments were entered
     */
    String execute(String args) throws InvalidRequestException;
}
