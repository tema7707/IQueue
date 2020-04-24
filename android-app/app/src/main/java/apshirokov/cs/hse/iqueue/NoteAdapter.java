package apshirokov.cs.hse.iqueue;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.*;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

public class NoteAdapter extends ArrayAdapter<Note> {

    private LayoutInflater inflater;
    private int layout;
    private List<Note> notes;

    private static final Interpolator INTERPOLATOR = new DecelerateInterpolator();

    public NoteAdapter(Context context, int resource, List<Note> states) {
        super(context, resource, states);
        this.notes = states;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View view = inflater.inflate(this.layout, parent, false);

        // TODO::add picasso + internet
        ImageView logoView = view.findViewById(R.id.noteLogo);
        TextView nameView = view.findViewById(R.id.noteCompanyName);
        TextView addressView = view.findViewById(R.id.noteAddress);
        TextView noteDay = view.findViewById(R.id.noteDay);
        TextView noteTime = view.findViewById(R.id.noteTime);
        Button deleteButton = view.findViewById(R.id.button_delete);

        Note note = notes.get(position);

        Picasso.get()
                .load(note.getLogoURL())
                .error(R.drawable.logo)
                .into(logoView);

        nameView.setText(note.getCompanyName());
        addressView.setText(note.getAddress());
        noteDay.setText(note.getDay());
        noteTime.setText(note.getTime());

        RelativeLayout formLayout = view.findViewById(R.id.noteForm);

        deleteButton.setOnClickListener(v -> {
            new Deleter().execute(""+note.getId());
            this.remove(note);
        });

        View.OnClickListener clickListener = v -> {
            // создаем анимацию
            LinearLayout buttonsLayout = v.findViewById(R.id.noteButtonsLayout);
            RelativeLayout formLayout1 = v.findViewById(R.id.noteForm);
            // Анимация кнопок
            Animation animationB = AnimationUtils.loadAnimation(v.getContext(), R.anim.anim_open_notes_buttons);
            animationB.setInterpolator(INTERPOLATOR);
            animationB.setAnimationListener(new AnimListener(buttonsLayout, false));
            buttonsLayout.startAnimation(animationB);

            AbsListView.LayoutParams lp = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT);
            formLayout1.setLayoutParams(lp);
        };
        formLayout.setOnClickListener(clickListener);

        return view;
    }

    private class AnimListener implements Animation.AnimationListener {

        private boolean isRevers;
        private LinearLayout animObject;
        public AnimListener(LinearLayout animObject, boolean isRevers){
            this.isRevers = isRevers;
            this.animObject = animObject;
        }

        @Override
        public void onAnimationEnd(Animation animation) {
        }

        @Override
        public void onAnimationStart(Animation animation) {
            animObject.setVisibility(View.VISIBLE);
        }

        @Override
        public void onAnimationRepeat(Animation animation) { }
    }

    class Deleter extends AsyncTask<String, Integer, String> {
        private void deleteNote(String id) {
            String request = String.format("http://192.168.43.137:8080/deletenote?id=%s", id);
            new HttpClient().request(request);
        }

        @Override
        protected String doInBackground(String... strings) {
            deleteNote(strings[0]);
            return null;
        }
    }
}


