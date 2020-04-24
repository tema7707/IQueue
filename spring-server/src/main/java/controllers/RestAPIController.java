package controllers;

import javafx.scene.input.DataFormat;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import structure.*;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@RestController
public class RestAPIController {

    private final String OK = "Success";
    private final String ER = "Error";
    private final DateFormat format = new SimpleDateFormat("dd-MM-yyyy_HH:mm:ss");
    private SqlController SqlCntl = new SqlController();
    private SqlControllerStats SqlCntlStats = new SqlControllerStats();

    @RequestMapping("/login")
    public boolean login(@RequestParam(value="login") String login,
                         @RequestParam(value="password") String password) {
        return SqlCntl.Login(login, password);
    }

    @RequestMapping("/registration")
    public boolean registration(@RequestParam(value="login") String login,
                                @RequestParam(value="password") String password,
                                @RequestParam(value="name") String name,
                                @RequestParam(value="sex") Boolean sex,
                                @RequestParam(value="city") String city,
                                @RequestParam(value="age") Integer age) {
        return SqlCntl.Registration(login, password, name, sex, city, age);
    }

    @RequestMapping("/getnotes")
    public List<Note> getnotes(@RequestParam(value="login") String login) {
          return SqlCntl.GetNotes(login);
    }

    @RequestMapping("/addnote")
    public boolean addnote(@RequestParam(value="login") String login,
                           @RequestParam(value="company") String company,
                           @RequestParam(value="logo") String logo,
                          @RequestParam(value="address") String address,
                          @RequestParam(value="time") String time) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd+HH:mm:ss");
            Date date = formatter.parse(time.replace(' ', '+'));
            return SqlCntl.AddNotes(login, company, logo, address, new java.sql.Date(date.getTime()));
        } catch (ParseException e) { return false; }
    }

    @RequestMapping("/test")
    public float[] test() {
        return new float[] {100, 200, 300, 400, 500, 600, 500, 400 ,300 ,200,  100, 0, -100};
    }

    @RequestMapping("/stats/company-name")
    public String getCompanyName(@RequestParam(value="login")String login) {
        return SqlCntlStats.getCompanyName(login);
    }

    @RequestMapping("/stats/sex-piechart")
    public float[] sexPiechart(@RequestParam(value="company") String company) {
        return SqlCntlStats.getSexPiechart(company);
    }

    @RequestMapping("/stats/all_orders")
    public int all_orders_count(@RequestParam(value="company") String company) {
        return SqlCntlStats.getAllOrdersCount(company);
    }
    @RequestMapping("/stats/today_orders")
    public int today_orders_count(@RequestParam(value="company") String company) {
        return SqlCntlStats.getTodayOrdersCount(company);
    }
    @RequestMapping("/stats/last_week_orders")
    public int last_week_orders_count(@RequestParam(value="company") String company) {
        return SqlCntlStats.getLastWeekOrdersCount(company);
    }
    @RequestMapping("/stats/last_month_orders")
    public int last_month_orders_count(@RequestParam(value="company") String company) {
        return SqlCntlStats.getLastMonthOrdersCount(company);
    }

    @RequestMapping("/stats/all_orders_w")
    public int[][] all_orders_w(@RequestParam(value="company") String company) {
        return SqlCntlStats.getOrdersW(company);
    }
    @RequestMapping("/stats/all_orders_m")
    public int[][] all_orders_m(@RequestParam(value="company") String company) {
        return SqlCntlStats.getOrdersM(company);
    }
    @RequestMapping("/stats/all_orders_y")
    public int[][] all_orders_y(@RequestParam(value="company") String company) {
        return SqlCntlStats.getOrdersY(company);
    }

    @RequestMapping("/stats/unique_orders_m")
    public int[] unique_orders_m(@RequestParam(value="company") String company) {
        return SqlCntlStats.getUniqueOrdersM(company);
    }

    @RequestMapping("admin/getnotes")
    public List<AdminNote> getNotesAdmin(@RequestParam(value="company") String company,
                                    @RequestParam(value="branch") String branch)
    {
        return SqlCntlStats.GetNotesAdmin(company, branch);
    }

    @RequestMapping("admin/donenote")
    public boolean doneNoteAdmin(@RequestParam(value="company") String company,
                                 @RequestParam(value="branch") String branch,
                                 @RequestParam(value="owner") String owner,
                                 @RequestParam(value="time") String time)
    {
        return SqlCntlStats.doneNoteAdmin(company, branch, owner, time);
    }

    @RequestMapping("stats/age")
    public float usersAge(@RequestParam(value="company") String company) {
        return SqlCntlStats.getAverageAge(company);
    }


    @RequestMapping("admin/registration")
    public boolean admin_registration(@RequestParam(value="login") String login,
                                @RequestParam(value="password") String password,
                                @RequestParam(value="name") String name,
                                @RequestParam(value="logo") String logo) {
        return SqlCntl.AdminRegistration(login, password, name, logo);
    }

    @RequestMapping("admin/login")
    public String admin_login(@RequestParam(value="login") String login,
                         @RequestParam(value="password") String password) {
        return SqlCntl.AdminLogin(login, password);
    }

    @RequestMapping("/companies")
    public Collection<Company> companies() {
        return SqlCntl.GetCompanies();
    }

    @RequestMapping("/branches")
    public Collection<Branch> branches(@RequestParam(value="company") String company) {
        return SqlCntl.GetBranches(company);
    }

    @RequestMapping("/add-branch")
    public boolean addBranch(@RequestParam(value="company") String company,
                             @RequestParam(value="name") String name,
                             @RequestParam(value="longitude") double longitude,
                             @RequestParam(value="latitude") double latitude) {
        return SqlCntl.AddBranch(company, name, longitude, latitude);
    }

    @RequestMapping("/deletenote")
    public String branches(@RequestParam(value="id") long id) {
        SqlCntl.deleteNote(id);
        return "Ok";
    }

    @RequestMapping("/profile")
    public String branches(@RequestParam(value="user") String user,
                                       @RequestParam(value="password") String password) {
        return SqlCntl.Profile(user, password).toString();
    }

    @RequestMapping("/generate")
    public String generate(@RequestParam(value="company") String company,
                           @RequestParam(value="address") String address) {
        SqlCntlStats.Generate(company, address);
        return "Ok";
    }
}
