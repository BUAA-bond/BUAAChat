package com.BUAAChat.Server.Config;

/**
 * Service Exceptions.
 * @author WnRock
 * @version 0.1
 */
public interface ServiceException {

    /**
     * Thrown when Client-Server Link fails.
     */
    public class LinkInitFailException extends RuntimeException {
        public LinkInitFailException(){
            super("Link initiation Failed");
        }
    }

    /**
     * Thrown when Client send a wrong System Command.
     */
    public class SysCmdNotFoundException extends RuntimeException {
        public SysCmdNotFoundException(){
            super("System command not found.");
        }
    }
}
