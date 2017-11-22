package com.prog4.wangz_jamileh.wishlist;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.Toast;

import com.prog4.wangz_jamileh.wishlist.Model.User;
import com.prog4.wangz_jamileh.wishlist.error.InputCheck;
import com.prog4.wangz_jamileh.wishlist.magic.Ajax;
import com.prog4.wangz_jamileh.wishlist.utility_manager.ImageManager;

import java.io.InputStream;
import java.util.Map;
import java.util.TreeMap;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Post.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Post#newInstance} factory method to
 * create an instance of this fragment. okay.
 */
public class Post extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int RESULT_LOAD_IMAGE = 2;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View postView;
    private Button postBut;
    private InputStream image;
    private ImageButton imageBut;
    private ImageButton remove;
    private Bitmap compressedImage;
    EditText giftNameView,
            giftDescView;
    RatingBar desierView,
            costView;

    private OnFragmentInteractionListener mListener;

    public Post() {
        // Required empty public constructor
    }

//    public void setFragmentInteractionListener(OnFragmentInteractionListener mListener){
//        this.mListener = mListener;
//    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Post.
     */
    // TODO: Rename and change types and number of parameters
    public static Post newInstance(String param1, String param2) {
        Post fragment = new Post();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (postView != null) return postView;
        postView = inflater.inflate(R.layout.fragment_post, container, false);

        postBut = (Button) postView.findViewById(R.id.post_button);
        postBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newPost();
            }
        });

        imageBut = (ImageButton) postView.findViewById(R.id.post_image);
        imageBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();
            }
        });
        remove = (ImageButton) postView.findViewById(R.id.post_remove_image);
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remove();
            }
        });
        return postView;
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

    private void newPost() {
        InputCheck check = new InputCheck();
        giftNameView = (EditText) postView.findViewById(R.id.post_giftname);
        giftDescView = (EditText) postView.findViewById(R.id.post_desc);
        desierView = (RatingBar) postView.findViewById(R.id.post_desire);
        costView = (RatingBar) postView.findViewById(R.id.post_cost);
        String giftName = giftNameView.getText().toString(),
                desc = giftDescView.getText().toString();
        float desire = desierView.getRating(),
                cost = costView.getRating();
        String id = User.getInstance().session;
        boolean cancel = false;
        if (check.empty(desc)) {
            check.error(giftDescView, "this field is required");
            cancel = true;
        }

        if (check.empty(giftName)) {
            check.error(giftNameView, "this field is required");
            cancel = true;
        }

        if (cancel) {
            check.showErr().requestFocus();
        } else {
            Ajax a = new Ajax();
            TreeMap<String, String> params = new TreeMap<>();
            params.put("id", id);
            params.put("desc", desc);
            params.put("desire_level", String.valueOf(desire));
            params.put("cost_level", String.valueOf(cost));
            params.put("title", giftName);
            String fileField = "image",
                    mimeType = "image/jpeg",
                    fileName = "post";

                a.post("/newDesireGift", params, image, fileField, mimeType, fileName);
                Map<String, Object> res = a.response();
            String msg = "something went wrong";
            //TODO use res
            if(res != null && res.containsKey("status") && res.get("status").equals("ok")){
                msg = "post successfully";
                reset();
            }
            Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
        }
    }

    private void pickImage() {
        Intent pickIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");
        startActivityForResult(pickIntent, RESULT_LOAD_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == getActivity().RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            ImageManager im = new ImageManager(getActivity());
            compressedImage = im.compressImage(selectedImage, 640, 480, 90);
            imageBut.setImageBitmap(compressedImage);
            remove.setVisibility(View.VISIBLE);
            image = im.Bitmap2InputStream(compressedImage);
        }
    }

    private void remove() {
        if (compressedImage != null) {
            image = null;
            compressedImage = null;
            remove.setVisibility(View.INVISIBLE);
            imageBut.setImageBitmap(null);
            imageBut.setImageResource(R.drawable.ic_photo_library_black_24dp);
        }
    }

    private void reset() {
        giftNameView.setText("");
        giftDescView.setText("");
        desierView.setRating((float)1.0);
        costView.setRating((float)1.0);
        remove();
    }
}
