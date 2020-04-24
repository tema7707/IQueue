package structure;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Branch {
    private String company;
    private String address;
    private Date waitingTime = new Date();

    public Date getWaitingTime() { return waitingTime; }

    public String getCompany() { return company; }

    public String getAddress() { return address; }

    public Branch(String company, String address){
        this.company = company;
        this.address = address;
    }
}
