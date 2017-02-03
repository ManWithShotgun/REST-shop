package ru.ilia.rest.exception;

/**
 * Костомный вид ошибки, что бы не выбрасывать просто Exception
 */
public class ExceptionDAO extends Exception {
    public ExceptionDAO(String message) {
        super(message);
    }
}
