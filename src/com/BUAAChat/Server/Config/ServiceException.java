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
     * Thrown when database operation invalid.
     */
    public class DatabaseOperationException extends RuntimeException {
        public DatabaseOperationException(){
            super("[ERROR!] Database operation ERROR.");
        }
    }

    /**
     * Thrown when client close the socket.
     */
    public class ConnResetException extends RuntimeException {
        public ConnResetException(){
            super("Connection reset by client.");
        }
    }

    /**
     * Thrown when resender exception occurs.
     */
    public class ResenderException extends RuntimeException {
        public ResenderException(){
            super("[ERROR!] Resender ERROR.");
        }
    }

    /**
     * Thrown when input wrong param.
     */
    public class IllegalParamException extends RuntimeException {
        public IllegalParamException(){
            super("[ERROR!] Param ERROR.");
        }
    }
}
