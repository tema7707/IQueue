package apshirokov.cs.hse.iqueue;

import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.Date;

public class BranchListElement {
    @SerializedName("address")
    private String address;
    @SerializedName("latitude")
    private double latitude;
    @SerializedName("longitude")
    private double longitude;
    @SerializedName("average")
    private double average;

    public BranchListElement(String address, double latitude, double longitude, double average){
        this.address = address;
        this.average = average;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public String getAverage() {
        return average == 0 ? "0 min" : "~" + average + " min";
    }
}
