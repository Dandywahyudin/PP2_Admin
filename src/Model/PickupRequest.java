package Model;

public class PickupRequest {
    private String requestId;
    private String userId;
    private String courierId;
    private String status;
    private int points;

    public PickupRequest(String requestId, String userId, String courierId, String status, int points) {
        this.requestId = requestId;
        this.userId = userId;
        this.courierId = courierId;
        this.status = status;
        this.points = points;
    }

    // Getters and setters
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
}