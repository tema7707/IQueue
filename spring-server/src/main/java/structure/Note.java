package structure;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Note {
    private String companyName;
    private String address;
    private Date recordingTime;
    private String logoURL;
    private long id;

    public String getCompanyName() {
        return companyName;
    }

    public String getAddress() {
        return address;
    }

    public Date getRecordingTime() {
        return recordingTime;
    }

    public String getLogoURL() {
        return logoURL;
    }

    public long getId() { return id; }

    public Note(String companyName, String address, String logoURL, Date time, long id){
        this.logoURL = logoURL;
        this.address = address;
        this.companyName = companyName;
        this.recordingTime = time;
        this.id = id;
    }
}
