package data;

import java.util.Objects;

public final class UDPRequest {

    private final int port;
    private final String code;
    private final String type;

    public UDPRequest(int port, String code, String type) {

        this.port = port;
        this.code = code;
        this.type = type;
    }


    public int port() {
        return port;
    }

    public String code() {
        return code;
    }

    public String type() {
        return type;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (obj == null || obj.getClass() != this.getClass())
            return false;
        var that = (UDPRequest) obj;
        return this.port == that.port &&
                Objects.equals(this.code, that.code) &&
                Objects.equals(this.type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(port, code, type);
    }

    @Override
    public String toString() {
        return "UDPRequest[" +
                "port=" + port + ", " +
                "code=" + code + ", " +
                "type=" + type + ']';
    }

}
