package apshirokov.cs.hse.iqueue;

import com.google.gson.annotations.SerializedName;

public class Company {
    @SerializedName("name")
    private String companyName;
    @SerializedName("logoUrl")
    private String logoUrl;

    public String getCompanyName() {
        return companyName;
    }

    public String getLogoUrl() {
        return logoUrl;
    }
}
