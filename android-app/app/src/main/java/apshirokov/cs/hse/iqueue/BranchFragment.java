package apshirokov.cs.hse.iqueue;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.here.android.mpa.mapping.Map;
import com.here.android.mpa.mapping.SupportMapFragment;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class BranchFragment extends Fragment {

    protected String address, time;
    protected double longitude, latitude;
    Calendar dateAndTime = Calendar.getInstance();
    Date recordingTime = new Date();
    private MapFragmentView mapFrafmentView;
    // map embedded in the map fragment
    private Map map = null;
    // map fragment embedded in this activity
    private SupportMapFragment mapFragment = null;
    int yearD;

    public BranchFragment() { }

    public static BranchFragment newInstance(String address, String time, double longitude, double latitude) {
        BranchFragment fragment = new BranchFragment();
        fragment.address = address;
        fragment.time = time;
        fragment.longitude = longitude;
        fragment.latitude = latitude;
        return fragment;
    }

    // отображаем диалоговое окно для выбора даты
    public void setDate() {
        new DatePickerDialog(getContext(), (view, year, month, dayOfMonth) -> {
            yearD = year;
            recordingTime.setYear(year);
            recordingTime.setMonth(month);
            recordingTime.setDate(dayOfMonth);
            setTime();
        },
        dateAndTime.get(Calendar.YEAR),
        dateAndTime.get(Calendar.MONTH),
        dateAndTime.get(Calendar.DAY_OF_MONTH))
        .show();
    }

    // отображаем диалоговое окно для выбора времени
    public void setTime() {
        new TimePickerDialog(getContext(), (view, hourOfDay, minute) -> {
            recordingTime.setHours(hourOfDay);
            recordingTime.setMinutes(minute);
            SimpleDateFormat formatter = new SimpleDateFormat("MM-dd+HH:mm:ss");
            new Adder().execute(LoginActivity.getLogin(), MainViewer.getCompanyName(), address,
                    yearD+"-"+formatter.format(recordingTime));
            MainViewer.singleMainViewr().goToFirstMenuItem();
        },
        dateAndTime.get(Calendar.HOUR_OF_DAY),
        dateAndTime.get(Calendar.MINUTE), true).show();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_branch, container, false);

        // начальная инициализация списка
        setInitialData();

        ((TextView) view.findViewById(R.id.branchFragmentAddress)).setText(address);
        ((TextView) view.findViewById(R.id.branchFragmentTime)).setText(time);

        // Кнопка
        Button getIn = view.findViewById(R.id.getIn);
        getIn.setOnClickListener(v -> {
            setDate();
        });

        // Карта
        // Search for the map fragment to finish setup by calling init().
        mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.branchMapFragment);
        mapFrafmentView = new MapFragmentView(mapFragment, MainViewer.singleMainViewr(), 11);
        mapFrafmentView.createMapMarker(longitude, latitude);

        ((TextView) view.findViewById(R.id.branchFragmentDist)).setText(
                "" + mapFrafmentView.getDist(latitude, longitude) + " meters"
        );

        return view;
    }

    class Adder extends AsyncTask<String, Integer, String> {
        private void addNote(String login, String company, String address, String time) {
            String request = null;
            try {
                request = String.format("http://192.168.43.137:8080/addnote?login=%s&company=%s&address=%s&time=%s&logo=%s",
                        login, company, address, time, URLEncoder.encode(MainViewer.getCompanyURL(), "UTF-8"));
            } catch (UnsupportedEncodingException e) { }
            String answer = new HttpClient().request(request);
        }

        @Override
        protected  String doInBackground(String... strings) {
            addNote(strings[0], strings[1], strings[2], strings[3]);
            return null;
        }
    }

    private void setInitialData(){
    }
}
