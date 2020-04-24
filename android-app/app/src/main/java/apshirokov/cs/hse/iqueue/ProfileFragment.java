package apshirokov.cs.hse.iqueue;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Debug;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class ProfileFragment extends Fragment {

    View currentView;
    JSONObject profileDate;

    public ProfileFragment() { }

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_profile, container, false);
        currentView = view;
        // начальная инициализация списка
        new Initializer().execute(LoginActivity.getLogin(), LoginActivity.getPassword());

        Button signOut = view.findViewById(R.id.button_sign_out);
        signOut.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), LoginActivity.class);
            startActivity(intent);
        });

        return view;
    }

    class Initializer extends AsyncTask<String, Integer, String> {
        private void setInitialData(String login, String password) {
            String request = String.format("http://192.168.43.137:8080/profile?user=%s&password=%s",
                    login, password);
            try {
                profileDate = new JSONObject(new HttpClient().request(request));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            setInitialData(strings[0], strings[1]);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                TextView fullNameView = currentView.findViewById(R.id.full_name);
                TextView cityView = currentView.findViewById(R.id.city);
                TextView ageView = currentView.findViewById(R.id.age);
                TextView sexView = currentView.findViewById(R.id.sex);
                TextView emailView = currentView.findViewById(R.id.email);
                fullNameView.setText(profileDate.getString("name"));
                cityView.setText(profileDate.getString("city"));
                ageView.setText(profileDate.getString("age") + " years");
                sexView.setText(profileDate.getString("sex"));
                emailView.setText(profileDate.getString("login"));
            } catch (JSONException e) { e.printStackTrace(); }
        }
    }
}
