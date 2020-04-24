package apshirokov.cs.hse.iqueue;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class BranchListElementAdapter extends ArrayAdapter<BranchListElement>{

    private LayoutInflater inflater;
    private int layout;
    private List<BranchListElement> elements;

    public BranchListElementAdapter(Context context, int resource, List<BranchListElement> elements) {
        super(context, resource, elements);
        this.elements = elements;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View view = inflater.inflate(this.layout, parent, false);

        TextView addressText = view.findViewById(R.id.branchElementAddress);
        TextView timeText = view.findViewById(R.id.branchElementTime);

        BranchListElement element = elements.get(position);

        addressText.setText(element.getAddress());
        timeText.setText(element.getAverage());

        // Set Listeners
        View.OnClickListener clickListener = v -> {
            MainViewer.singleMainViewr().loadFragment(BranchFragment.newInstance(element.getAddress(),
                    element.getAverage(), element.getLongitude(), element.getLatitude()));
        };
        view.findViewById(R.id.formBranchListElement).setOnClickListener(clickListener);

        return view;
    }
}
