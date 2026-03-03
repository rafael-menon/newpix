package newpixserver.messages.client_state;

public record StateResult(Object result, ClientState nextState) {
    public static StateResult unchanged(Object result) {
        return new StateResult(result, null);
    }

    public static StateResult transition(ClientState nextState) {
        return new StateResult(null, nextState);
    }
}
