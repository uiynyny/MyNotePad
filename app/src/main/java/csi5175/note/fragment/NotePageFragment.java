/*
 * Yan Zhang
 * 300052103*/
package csi5175.note.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;

import csi5175.note.R;
import csi5175.note.activity.MainActivity;
import csi5175.note.data.NoteManager;
import csi5175.note.model.Note;
import csi5175.note.utility.Constants;
import csi5175.note.utility.PicassoImageGetter;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NotePageFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NotePageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotePageFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private EditText mTitle;
    private EditText mContent;
    private Note mNote;

    public NotePageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param id
     * @return A new instance of fragment NotePageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NotePageFragment newInstance(long id) {
        NotePageFragment fragment = new NotePageFragment();
        if (id > 0) {
            Bundle args = new Bundle();
            args.putLong(Constants.COLUMN_ID, id);
            fragment.setArguments(args);
        }
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("FRAGMENT_NOTEPAGE", "onCreate");
        setHasOptionsMenu(true);
        getCurrentNote();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("FRAGMENT_NOTEPAGE", "onResume");

    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(getClass().getSimpleName(), "onStop");
    }


    public void onSaveInstanceState(Bundle outState) {
        Log.d(getClass().getSimpleName(), "saveInstanceState");
        if (!TextUtils.isEmpty(mTitle.getText().toString())) {
            outState.putString("title", mTitle.getText().toString());
        }
        if (!TextUtils.isEmpty(mContent.getText().toString())) {
            outState.putString("content", Html.toHtml(mContent.getText()));
        }
    }

    private void getCurrentNote() {
        Bundle args = getArguments();
        if (args != null) {
            long id = args.getLong(Constants.COLUMN_ID);
            mNote = NoteManager.newInstance(getContext()).getNote(id);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(getClass().getSimpleName(), "onCreateView");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_note_page, container, false);
        mTitle = view.findViewById(R.id.note_title);
        mContent = view.findViewById(R.id.note_content);

        if (savedInstanceState == null) {
            if (mNote != null) {
                mTitle.setText(mNote.getTitle());
                Picasso picasso = new Picasso.Builder(getContext()).build();
                Spanned span = Html.fromHtml(mNote.getContent(), new PicassoImageGetter(picasso, mContent), null);
                mContent.setText(span);//Html.fromHtml(mNote.getContent())
                mContent.refreshDrawableState();
            }
        } else {
            String title = savedInstanceState.getString("title");
            mTitle.setText(title);
            String content = savedInstanceState.getString("content");
            mContent.setText(Html.fromHtml(content));
        }
        registerForContextMenu(mContent);
        return view;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getActivity().getMenuInflater().inflate(R.menu.menu_context_edit, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.option_italic:
                makeStyle(Typeface.ITALIC);
                return true;
            case R.id.option_underline:
                makeStyle(4);
                return true;
            case R.id.option_bold:
                makeStyle(Typeface.BOLD);
                return true;
        }
        return super.onContextItemSelected(item);
    }

    private void makeStyle(int type) {
        int start = mContent.getSelectionStart();
        int end = mContent.getSelectionEnd();
        SpannableString ss = new SpannableString(mContent.getText());
        if (type == 4) {
            ss.setSpan(new UnderlineSpan(), start, end, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else {
            ss.setSpan(new StyleSpan(type), start, end, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        mContent.setText(ss);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_note_edit, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                if (saveNote()) {
                    makeToast(mNote != null ? "Note Updated" : "Note Saved");
                    break;
                }
            case R.id.action_delete:
                if (mNote != null) {
                    deleteNote();
                } else {
                    makeToast("Can not delete the note that does not saved");
                }
                break;
            case R.id.action_image:
                Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickPhoto.setType("image/*");
                startActivityForResult(pickPhoto, 3);
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //result of image
        if (requestCode == 3 && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            try {
                addImage(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

    }

    private void addImage(Uri selectedImage) throws FileNotFoundException {
//        Bitmap bitmap = resizeImage(selectedImage);
//        add image into edit text
        ImageSpan im = new ImageSpan(getContext(), selectedImage);
        SpannableString ss = new SpannableString(" ");
        ss.setSpan(im, 0, ss.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        Editable editable = mContent.getEditableText();
        int curpos = mContent.getSelectionStart();
        editable.insert(curpos, ss);
    }

    private Bitmap resizeImage(Uri selectedImage) throws FileNotFoundException {
        Bitmap bitmap = BitmapFactory.decodeStream(getContext().getContentResolver().openInputStream(selectedImage));
        int imgWidth = bitmap.getWidth();
        int imgHeight = bitmap.getHeight();
        double ratio = imgWidth * 1.0 / imgHeight;
        double sqrtLength = Math.sqrt(Math.pow(ratio, 2) + 1);
        double newImgW = 480 * (ratio / sqrtLength);
        double newImgH = 480 * (1 / sqrtLength);
        float scaleW = (float) (newImgW / imgWidth);
        float scaleH = (float) (newImgH / imgHeight);
        Matrix m = new Matrix();
        m.postScale(scaleW, scaleH);
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, imgWidth, imgHeight, m, true);
        return bitmap;
    }


    private boolean saveNote() {
        Log.d("NOTEPAGEFARGMENT", "SAVE_NOTE");
        String title = mTitle.getText().toString();
        if (TextUtils.isEmpty(title)) {
            mTitle.setError("title missing");
            return false;
        }
        String content = mContent.getText().toString();
        if (TextUtils.isEmpty(content)) {
            mContent.setError("content missing");
            return false;
        }
        if (mNote != null) {
            mNote.setTitle(title);
            String c = Html.toHtml(mContent.getText());
            mNote.setContent(c);
            long id = NoteManager.newInstance(getContext()).update(mNote);
            Log.d("save state", String.valueOf(id));

        } else {
            Note note = new Note();
            note.setTitle(title);
            note.setContent(content);
            NoteManager.newInstance(getContext()).create(note);
        }
        return true;
    }

    private void deleteNote() {
        final String title = mNote.getTitle();
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity())
                .setTitle("Delete " + title)
                .setMessage("Are you sure want to delete note " + title + "?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        NoteManager.newInstance(getContext()).delete(mNote);
                        makeToast(title + " deleted");
                        startActivity(new Intent(getActivity(), MainActivity.class));
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    private void makeToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
