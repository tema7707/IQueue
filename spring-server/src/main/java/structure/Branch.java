package structure;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Branch {
    private String company;
    private String address;
    private double latitude;
    private double longitude;
    private double average;

    public String getCompany() {
        return company;
    }

    public String getAddress() {
        return address;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getAverage() {
        return average;
    }

    public Branch(String company, String address, double latitude, double longitude, double average){
        this.company = company;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.average = average;
    }
}
