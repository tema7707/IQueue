package apshirokov.cs.hse.iqueue;

import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ChooseCompanyFragment extends Fragment {

    private List<ChooseCompany> companies = new ArrayList();
    ListView companiesList;
    View currentView;

    public ChooseCompanyFragment() { }

    public static ChooseCompanyFragment newInstance() {
        return new ChooseCompanyFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_choose_company, container, false);
        currentView = view;
        // начальная инициализация списка
        new Initializer().execute();
        // получаем элемент ListView
        companiesList =  view.findViewById(R.id.chooseCompanyList);
        // создаем адаптер
        ChooseCompanyAdapter stateAdapter = new ChooseCompanyAdapter(view.getContext(), R.layout.form_company, companies);
        // устанавливаем адаптер
        companiesList.setAdapter(stateAdapter);

        return view;
    }

    class Initializer extends AsyncTask<String, Integer, String> {
        private void setInitialData() {
            try {
                String request = String.format("http://192.168.43.137:8080/companies");
                String answer = new HttpClient().request(request);
                JSONArray companiesJSON = new JSONArray(answer);
                Company first = null, second = null;
                for (int i = 0; i < companiesJSON.length(); i++) {
                    if (i % 2 == 0) first = new Gson().fromJson(companiesJSON.getJSONObject(i).toString(), (Type) Company.class);
                    else {
                        second = new Gson().fromJson(companiesJSON.getJSONObject(i).toString(), (Type) Company.class);
                        companies.add(new ChooseCompany(first, second));
                    }
                }
                if (companiesJSON.length() % 2 == 1)
                    companies.add(new ChooseCompany(first));

            } catch (JSONException e) {
                Log.e("IQueue", e.getMessage());
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            setInitialData();
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            // получаем элемент ListView
            companiesList = currentView.findViewById(R.id.chooseCompanyList);
            // создаем адаптер
            ChooseCompanyAdapter companyAdapter = new ChooseCompanyAdapter(currentView.getContext(), R.layout.form_company, companies);
            // устанавливаем адаптер
            companiesList.setAdapter(companyAdapter);
        }
    }
}
