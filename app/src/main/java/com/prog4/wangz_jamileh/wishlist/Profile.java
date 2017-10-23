package com.prog4.wangz_jamileh.wishlist;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.MediaStore;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.internal.LinkedTreeMap;
import com.prog4.wangz_jamileh.wishlist.Model.User;
import com.prog4.wangz_jamileh.wishlist.R;
import com.prog4.wangz_jamileh.wishlist.databinding.FragmentProfileBinding;
import com.prog4.wangz_jamileh.wishlist.magic.Ajax;
import com.prog4.wangz_jamileh.wishlist.utility_manager.ImageManager;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

//TODO image file name need to be done, organize the code!!!!!

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Profile.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Profile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Profile extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String IMAGE_CAPTURE_FOLDER = "wishlist_android/Upload";
    private static final int RESULT_LOAD_IMAGE = 1;
    // TODO: Rename and change types of parameters
    private Button gallaryBut;
    private ImageView imageView;
    private LinearLayout personalInfo;
    private View profileView;
    public String mParam1, mParam2;
    private User user;

    private OnFragmentInteractionListener mListener;

    public Profile() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Profile.
     */
    // TODO: Rename and change types and number of parameters
    public static Profile newInstance(String param1, String param2) {
        Profile fragment = new Profile();
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
        if(profileView!=null) return profileView;
//        profileView = inflater.inflate(R.layout.fragment_profile, container, false);
        user = User.getInstance();
        FragmentProfileBinding bind =  DataBindingUtil.inflate(
                   inflater, R.layout.fragment_profile, container, false);
        profileView = bind.getRoot();
        imageView = (ImageView) profileView.findViewById(R.id.profile_selectedImage);
        personalInfo = (LinearLayout) profileView.findViewById(R.id.profile_personalInfo);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();
            }
        });
        personalInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), ChangeProfileActivity.class);
                startActivity(i);
            }
        });
        bind.setUser(user);
//        Bitmap image = downloadImage();
//        if(image != null){
//            imageView.setImageBitmap(new ImageManager().compressImage(image));
//        }

        return profileView;
    }

    private void pickImage() {
        Intent pickIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");
        startActivityForResult(pickIntent, RESULT_LOAD_IMAGE);
    }

    public void uploadImage(InputStream fileInputStream) {
        Ajax a = new Ajax();
        TreeMap<String, String> params = new TreeMap<>();
        params.put("id", "123");
        String fileField = "image",
                mimeType = "image/jpeg",
                fileName = "avatar";
        a.post("/file", params, fileInputStream, fileField, mimeType, fileName);
        Map<String, Object> res = a.response();
        System.out.println(res.get("status"));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == getActivity().RESULT_OK && data != null) {
            Uri selectedImage = data.getData();

            ImageManager im = new ImageManager();
            InputStream image = im.uriToFile(selectedImage, getActivity());
            Bitmap compressedImg = im.compressImage(image);
            imageView.setImageDrawable(null);
            imageView.setImageBitmap(compressedImg);
            if(image != null){
//                uploadImage(image);
            }
        }
    }

    @SuppressWarnings("unchecked")
    public Bitmap downloadImage(){
        Ajax a = new Ajax();
        TreeMap<String, String> params = new TreeMap<>();
        a.post("/getImage", params);
        Map<String, Object> res = a.response();
        if(res != null && res.containsKey("status") && res.get("status").equals("ok")){
            Map<String, Object> data = (LinkedTreeMap<String, Object>) res.get("data");
            Map<String, Object> image = (LinkedTreeMap<String, Object>) res.get("image");
            return new ImageManager().listToBitmap((ArrayList<Double>) image.get("data"));
        }
       return null;
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

//    private void captureImage() {
//        Intent pickIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//        imageUri = Uri.fromFile(getFile());
//        pickIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//        startActivityForResult(pickIntent, 1111);
//    }
//
//    private File getFile() {
//        String filepath = Environment.getExternalStorageDirectory().getPath();
//        File file = new File(filepath, IMAGE_CAPTURE_FOLDER);
//        if (!file.exists()) {
//            file.mkdirs();
//        }
//
//        return new File(file + File.separator + imageFileName + ".jpg");
//    }
}
