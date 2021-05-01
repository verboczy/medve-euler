package graph;

public class GraphValidationException extends Exception {

    GraphValidationException(final String message) {
        super(message);
    }

    GraphValidationException(final String message, Object... parameters) {
        super(String.format(message, parameters));
    }
}
