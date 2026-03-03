package newpixserver.type;

public class ServerPort {
    private int serverPort;

    public ServerPort(int serverPort) {
        validatePort(serverPort);
        this.serverPort = serverPort;
    }

    private void validatePort(int serverPort) {
        if (serverPort < 20000 || serverPort > 30000) {
            throw new IllegalArgumentException("Erro: Escolha uma porta entre 20000 e 30000.");
        }
    }

    public int getServerPort() {
        return serverPort;
    }
}
