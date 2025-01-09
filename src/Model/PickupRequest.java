package Model;

public class PickupRequest {
    private String requestId;
    private String userId;
    private String courierId;
    private String status;
    private int points;
    private String wasteType;

    public PickupRequest(String requestId, String userId, String courierId, String status, int points, String wasteType) {
        this.requestId = requestId;
        this.userId = userId;
        this.courierId = courierId;
        this.status = status;
        this.points = points;
        this.wasteType = wasteType;
    }

    // Getter dan Setter
    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCourierId() {
        return courierId;
    }

    public void setCourierId(String courierId) {
        this.courierId = courierId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getWasteType() {
        return wasteType;
    }

    public void setWasteType(String wasteType) {
        this.wasteType = wasteType;
    }
}
