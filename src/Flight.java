public class Flight {
    private String flightNumber;
    private double price;

    public Flight(String flightNumber, double price) {
        this.flightNumber = flightNumber;
        this.price = price;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public double getPrice() {
        return price;
    }
}