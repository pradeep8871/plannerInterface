package com.opta.demo.todo;

public class OptaNotFoundException extends RuntimeException {

    public OptaNotFoundException(String id) {
        super(String.format("No todo entry found with id: <%s>", id));
    }
}
