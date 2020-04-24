package structure;

import java.util.Date;

public class AdminNote {

    public String recordingTime;

    public String userName;

    private Date time;

    public Date getTime() {
        return time;
    }

    public AdminNote(String userName, Date recordingTime) {
        this.userName = userName;
        this.time = recordingTime;
        this.recordingTime = recordingTime.toString();
    }
}
