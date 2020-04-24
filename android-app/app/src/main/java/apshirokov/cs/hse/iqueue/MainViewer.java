package apshirokov.cs.hse.iqueue;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationMenu;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;



public class MainViewer extends AppCompatActivity {

    TextView title;
    private static int idFirstBottomItem;

    private static MainViewer singleMainViewer;
    private static String companyName;
    private static String companyURL;

    public static MainViewer singleMainViewr(){
        return singleMainViewer;
    }

    public static String getCompanyName() { return companyName; }

    public static void setCompanyName(String name) { companyName = name; }

    public static String getCompanyURL() { return companyURL; }

    public static void setCompanyURL(String url) { companyURL = url; }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.my_notes:
                    loadFragment(MyNotesFragment.newInstance());
                    title.setText("My Notes");
                    return true;
                case R.id.add_new:
                    loadFragment(ChooseCompanyFragment.newInstance());
                    title.setText("Add New Note");
                    return true;
                case R.id.profile:
                    loadFragment(ProfileFragment.newInstance());
                    title.setText("My profile");
                    return true;
            }
            return false;
        }
    };

    public void loadFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fl_content, fragment);
        ft.commit();
    }

    public void goToFirstMenuItem() {
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setSelectedItemId(idFirstBottomItem);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_viewer);

        singleMainViewer = singleMainViewer == null ? this : singleMainViewer;
        title = findViewById(R.id.title);
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        idFirstBottomItem = navigation.getSelectedItemId();

        loadFragment(MyNotesFragment.newInstance());
    }

}
