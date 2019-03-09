/*
 * Yan Zhang
 * 300052103*/
package csi5175.note.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import csi5175.note.R;
import csi5175.note.fragment.NotePageFragment;
import csi5175.note.utility.Constants;

public class NoteEditActivity extends AppCompatActivity implements NotePageFragment.OnFragmentInteractionListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(getClass().getSimpleName(),"onCreate");
        setContentView(R.layout.activity_note_edit);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (savedInstanceState == null) {
            Bundle arg = getIntent().getExtras();
            if (arg != null && arg.containsKey(Constants.COLUMN_ID)) {
                long id = arg.getLong(Constants.COLUMN_ID);
                NotePageFragment notePageFragment = NotePageFragment.newInstance(id);
                openFragment(notePageFragment, "Editor");
                return;
            }
            openFragment(new NotePageFragment(), "Editor");
        }
    }

//    open fragment's of note editor
    private void openFragment(Fragment fragment, String title) {
        getSupportFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.container, fragment)
                .addToBackStack(null)
                .commit();
        getSupportActionBar().setTitle(title);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        //empty first
        Log.d(this.getClass().getSimpleName(), "internaction occured");
    }
}
