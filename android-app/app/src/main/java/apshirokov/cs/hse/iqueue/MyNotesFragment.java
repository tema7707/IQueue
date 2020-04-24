package apshirokov.cs.hse.iqueue;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;


import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MyNotesFragment extends Fragment {

    private List<Note> notes = new ArrayList();
    ListView notesList;
    private View currentView;
    public MyNotesFragment() { }

    public static MyNotesFragment newInstance() {
        return new MyNotesFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_my_notes, container, false);
        currentView = view;
        // начальная инициализация списка
        new Initializer().execute(LoginActivity.getLogin());
        return view;
    }

    class Initializer extends AsyncTask<String, Integer, String> {
        private void setInitialData(String login) {
            try {
                String request = String.format("http://192.168.43.137:8080/getnotes?login=%s", login);
                String answer = new HttpClient().request(request);
                JSONArray notesJSON = new JSONArray(answer);
                for (int i = 0; i < notesJSON.length(); i++)
                    notes.add(new Gson().fromJson(notesJSON.getJSONObject(i).toString(), Note.class));
            } catch (JSONException e) {
                Log.e("IQueue", e.getMessage());
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            setInitialData(strings[0]);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            // получаем элемент ListView
            notesList = currentView.findViewById(R.id.notesList);
            // создаем адаптер
            NoteAdapter noteAdapter = new NoteAdapter(currentView.getContext(), R.layout.form_note, notes);
            // устанавливаем адаптер
            notesList.setAdapter(noteAdapter);
        }
    }
}