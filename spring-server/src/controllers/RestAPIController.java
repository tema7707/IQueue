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

    @RequestMapping("/login")
    public boolean login(@RequestParam(value="login") String login,
                         @RequestParam(value="password") String password) {
        return SqlCntl.Login(login, password);
    }

    @RequestMapping("/registration")
    public boolean registration(@RequestParam(value="login") String login,
                                @RequestParam(value="password") String password,
                                @RequestParam(value="name") String name,
                                @RequestParam(value="surname") String surname) {
        return SqlCntl.Registration(login, password, name, surname);
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
            Date date = formatter.parse("2019-25-17+12:25:22");
            return SqlCntl.AddNotes(login, company, logo, address, new java.sql.Date(date.getTime()));
        } catch (ParseException e) { return false; }
    }

    @RequestMapping("/companies")
    public Collection<Company> companies() {
        return SqlCntl.GetCompanies();
    }

    @RequestMapping("/branches")
    public Collection<Branch> branches(@RequestParam(value="company") String company) {
        return SqlCntl.GetBranches(company);
    }
}
