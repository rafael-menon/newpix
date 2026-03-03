package newpixclient.messages;

public class TcpRequest {
    private final String payload;
    private final boolean expectsResponse;

    public TcpRequest(String payload, boolean expectsResponse) {
        this.payload = payload;
        this.expectsResponse = expectsResponse;
    }

    public String getPayload() { return payload; }
    public boolean expectsResponse() { return expectsResponse; }
}
