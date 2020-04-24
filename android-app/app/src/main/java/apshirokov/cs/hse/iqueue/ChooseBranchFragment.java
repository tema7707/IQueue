package apshirokov.cs.hse.iqueue;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.common.OnEngineInitListener;
import com.here.android.mpa.common.PositioningManager;
import com.here.android.mpa.mapping.Map;
import com.here.android.mpa.mapping.MapMarker;
import com.here.android.mpa.mapping.SupportMapFragment;

import org.json.JSONArray;
import org.json.JSONException;

public class ChooseBranchFragment extends Fragment {

    private List<BranchListElement> elements = new ArrayList();
    ListView elementsList;
    View currentView;

    private MapFragmentView mapFrafmentView;
    // map embedded in the map fragment
    private Map map = null;
    // map fragment embedded in this activity
    private SupportMapFragment mapFragment = null;

    public ChooseBranchFragment() { }

    public static ChooseBranchFragment newInstance() {
        return new ChooseBranchFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        final View view = inflater.inflate(R.layout.fragment_choose_branch, container, false);
        currentView = view;
        // начальная инициализация списка
        new Initializer().execute(MainViewer.getCompanyName());

        // Карта
        // Search for the map fragment to finish setup by calling init().
        mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.mapfragment);
        mapFrafmentView = new MapFragmentView(mapFragment, MainViewer.singleMainViewr(), 7);

        return view;
    }

    class Initializer extends AsyncTask<String, Integer, String> {
        private void setInitialData(String company) {
            try {
                String request = String.format("http://192.168.43.137:8080/branches?company=%s", company);
                String answer = new HttpClient().request(request);
                JSONArray notesJSON = new JSONArray(answer);
                for (int i = 0; i < notesJSON.length(); i++) {
                    elements.add(new Gson().fromJson(notesJSON.getJSONObject(i).toString(), BranchListElement.class));
                    mapFrafmentView.createMapMarker(elements.get(i).getLongitude(), elements.get(i).getLatitude());
                }
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
            elementsList =  currentView.findViewById(R.id.branchElementsList);
            // создаем адаптер
            BranchListElementAdapter branchAdapter = new BranchListElementAdapter(currentView.getContext(),
                    R.layout.form_branch_list_element, elements);
            // устанавливаем адаптер
            elementsList.setAdapter(branchAdapter);
        }
    }
}
