package Zomato.DTO;
public class OrderRequest
{
    String lat;
    String log;

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    String userid;

    public OrderRequest(String lat, String log, String userid) {
        this.lat = lat;
        this.log = log;
        this.userid = userid;
    }
    public OrderRequest() {
        this.lat = lat;
        this.log = log;
        this.userid = userid;
    }


}
