package structure;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.HashMap;
import java.util.Map;

public class Company {
    @JsonIgnore
    public static Map<String, Company> companies = new HashMap<>();
    @JsonIgnore
    private Map<String, Branch> branches = new HashMap<>();

    private String name;
    private String logoUrl;

    public String getName() {
        return name;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public Map<String, Branch> getBranches() {
        return branches;
    }

    public void addBranch(String key, Branch branch){
        branches.put(key, branch);
    }

    public Company(String name, String logoUrl){
        this.name = name;
        this.logoUrl = logoUrl;
        companies.put(name, this);
    }
}
