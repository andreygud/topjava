package ru.javawebinar.topjava.util.exception;

public class ResultSetSQLException extends RuntimeException {
    public ResultSetSQLException(Throwable throwable){
        super(throwable);
    }
}
