/*
 * Yan Zhang
 * 300052103*/
package csi5175.note.utility;

import csi5175.note.fragment.NoteFragment;
import csi5175.note.fragment.NotePageFragment;

//an interface for two fragments listener, easy for mainactivity to handle the events
public interface OnListFragmentInteractionListener<T> extends
        NotePageFragment.OnFragmentInteractionListener,
        NoteFragment.OnListFragmentInteractionListener {
}

