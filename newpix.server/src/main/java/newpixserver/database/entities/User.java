package newpixserver.database.entities;

public class User {
	private int id;
	private String name;
	private String cpf;
	private String password;
	private double balance;
	private String token;
    private int isActive;
	
	public User(int id, String name, String cpf, String password, double balance, String token) {
		this.id = id;
		this.name = name;
		this.cpf = cpf;
		this.password = password;
		this.balance = 0.00;
		this.token = null;
        this.isActive = 1;
	}
	
	public User(String name, String cpf, String password) {
		this.name = name;
		this.cpf = cpf;
		this.password = password;
		this.balance = 0.00;
		this.token = null;
        this.isActive = 1;
	}

	public User() {}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}
	
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

    public int isActive() {
        return isActive;
    }

    public void setActive(int active) {
        isActive = active;
    }

    @Override
	public String toString() {
	    return "User{" +
	           "id=" + id +
	           ", name='" + name + '\'' +
	           ", cpf='" + cpf + '\'' +
	           ", password=" + password +  
	           ", balance=" + balance +
	           ", token=" + token +   
	           '}';
	}
}
