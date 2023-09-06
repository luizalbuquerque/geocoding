package apigeomapservices.exceptions;

public class GeocodingException extends RuntimeException {
    private int statusCode;

    public GeocodingException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
