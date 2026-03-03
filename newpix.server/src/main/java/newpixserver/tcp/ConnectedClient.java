package newpixserver.tcp;

public class ConnectedClient {
    private String ip;
    private int port;
    private String hostname;
    private String name;

    public ConnectedClient(String ip, int port, String hostname, String name) {
        this.ip = ip;
        this.port = port;
        this.hostname = hostname;
        this.name = name;
    }

    public ConnectedClient() {}

	public String getIp() {
		return ip;
	}
	
	public void setIp(String ip) {
		this.ip = ip;
	}
	
	public int getPort() {
		return port;
	}
	
	public void setPort(int port) {
		this.port = port;
	}
	
	public String getHostname() {
		return hostname;
	}
	
	public void setHostname(String hostname) {
		this.hostname = hostname;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
}