package Model;

public class Courier {
    private String courierId;
    private String name;
    private String email;
    private String phoneNumber;
    private String vehicleNumber;
    private String simNumber;

    public Courier(String courierId, String name, String email, String phoneNumber, String vehicleNumber, String simNumber) {
        this.courierId = courierId;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.vehicleNumber = vehicleNumber;
        this.simNumber = simNumber;
    }

    // Getters and setters
    public String getCourierId() {
        return courierId;
    }

    public void setCourierId(String courierId) {
        this.courierId = courierId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public String getSimNumber() {
        return simNumber;
    }

    public void setSimNumber(String simNumber) {
        this.simNumber = simNumber;
    }
}
