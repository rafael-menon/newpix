package newpixclient.json;

public class TransactionViewModel {
    private String date;
    private String type;
    private double value;

    public TransactionViewModel(String date, String type, double value) {
        this.date = date;
        this.type = type;
        this.value = value;
    }

    public String getDate() { return date; }
    public String getType() { return type; }
    public double getValue() { return value; }
}
