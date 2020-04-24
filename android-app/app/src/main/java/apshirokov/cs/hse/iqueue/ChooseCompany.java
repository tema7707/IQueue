package apshirokov.cs.hse.iqueue;

import java.net.URL;

public class ChooseCompany {

    private String companyName1, companyName2;
    private String logoURL1, logoURL2;

    public ChooseCompany(Company first, Company second){
        companyName1 = first.getCompanyName();
        companyName2 = second.getCompanyName();
        logoURL1 =first.getLogoUrl();
        logoURL2 = second.getLogoUrl();
    }

    public ChooseCompany(Company first){
        companyName1 = first.getCompanyName();
        logoURL1 =first.getLogoUrl();
        logoURL2 = "";
        companyName2 = "";
    }

    public ChooseCompany(String companyName, String logoURL){
        this.companyName1 = companyName;
        this.logoURL1 = logoURL;
        // empty
        this.companyName2 = "";
        this.logoURL2 = null;
    }

    public ChooseCompany(String companyName, String logoURL, String companyName2, String logoURL2){
        this.companyName1 = companyName;
        this.logoURL1 = logoURL;
        this.companyName2 = companyName2;
        this.logoURL2 = logoURL2;
    }

    public String getCompanyName1() {
        return companyName1;
    }

    public String getLogoURL1() {
        return logoURL1;
    }

    public String getCompanyName2() {
        return companyName2;
    }

    public String getLogoURL2() {
        return logoURL2;
    }
}
