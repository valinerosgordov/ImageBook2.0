package ru.imagebook.client.app.service;

import java.io.Serializable;

/**
 * @author Sergey Boykov
 */
public class PostamateUnavailableException extends RuntimeException implements Serializable {

    public PostamateUnavailableException() {
    }

    public PostamateUnavailableException(String message) {
        super(message);
    }

    public PostamateUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }
}
