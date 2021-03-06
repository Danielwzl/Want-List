package com.prog4.wangz_jamileh.wishlist;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
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
import com.prog4.wangz_jamileh.wishlist.databinding.FragmentProfileBinding;
import com.prog4.wangz_jamileh.wishlist.magic.Ajax;
import com.prog4.wangz_jamileh.wishlist.utility_manager.ImageManager;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
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
    private static final int RESULT_LOAD_PROFILE = 3;
    public static boolean newFriend;
    // TODO: Rename and change types of parameters
    private Button gallaryBut;
    private ImageView imageView;
    private LinearLayout personalInfo;
    private View profileView;
    private LinearLayout securityView;
    private LinearLayout friendView;
    public String mParam1, mParam2;
    private ImageView logout;
    private User user;
    private ImageManager im;
    private ImageView dot;

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
        if (profileView != null) return profileView;
//        profileView = inflater.inflate(R.layout.fragment_profile, container, false);
        user = User.getInstance();
        FragmentProfileBinding bind = DataBindingUtil.inflate(
                inflater, R.layout.fragment_profile, container, false);
        profileView = bind.getRoot();
        imageView = (ImageView) profileView.findViewById(R.id.profile_selectedImage);
        securityView = (LinearLayout) profileView.findViewById(R.id.profile_security);
        personalInfo = (LinearLayout) profileView.findViewById(R.id.profile_personalInfo);
        friendView = (LinearLayout) profileView.findViewById(R.id.profile_friend);
        logout = (ImageView) profileView.findViewById(R.id.profile_logout);
        dot = (ImageView) profileView.findViewById(R.id.profile_dot);
        getNewFirendRequest();
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Ajax a = new Ajax();
                TreeMap<String, String> params = new TreeMap<>();
                params.put("id", User.getInstance().session);
                a.post("/logout", params);
                Map<String, Object>res= a.response();
                if(res != null){
                    User.logout();
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("EXIT", true);
                    startActivity(intent);
                }
            }
        });

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
                startActivityForResult(i, RESULT_LOAD_PROFILE);
            }
        });

        securityView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), SecurityActivity.class);
                startActivity(i);
            }
        });
        friendView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), FriendActivity.class);
                startActivity(i);
            }
        });
        if (user.avartar != null) {
            imageView.setImageBitmap(user.avartar);
        }
        bind.setUser(user);
        if (im == null) im = new ImageManager(getActivity());

        return profileView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (user.avartar == null) {
            Bitmap image = downloadImage(user, im);
            if (image != null) {
                imageView.setImageBitmap(image);
            }
        }
        getNewFirendRequest();
    }

    private void pickImage() {
        Intent pickIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");
        startActivityForResult(pickIntent, RESULT_LOAD_IMAGE);
    }

    public boolean uploadImage(InputStream fileInputStream) {
        boolean done = false;
        Ajax a = new Ajax();
        TreeMap<String, String> params = new TreeMap<>();
        params.put("id", User.getInstance().session);
        String fileField = "image",
                mimeType = "image/jpeg",
                fileName = "avatar";
        a.post("/updateAvatar", params, fileInputStream, fileField, mimeType, fileName);
        Map<String, Object> res = a.response();
        if (res.get("status").toString().equals("ok")) {
            done = true;
        }
        return done;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK && data != null) {
            if (requestCode == RESULT_LOAD_IMAGE) {
                boolean done = false;
                Uri selectedImage = data.getData();
                Bitmap compressedImg = im.compressImage(selectedImage, 320, 320, 70);
                InputStream image = im.Bitmap2InputStream(compressedImg);
                if (image != null) {
                    done = uploadImage(image);
                }
                if (done) {
                    imageView.setImageDrawable(null);
                    imageView.setImageBitmap(compressedImg);
                    user.setAvartar(compressedImg);
                }
            } else if (requestCode == RESULT_LOAD_PROFILE) {
                    user.setUsername(data.getStringExtra("nick_name"));
                    user.setFname(data.getStringExtra("fName"));
                    user.setLname(data.getStringExtra("lName"));
                    user.setDob(data.getStringExtra("dob"));
                    user.setGender(data.getStringExtra("gender"));
            }
        }
    }

    @SuppressWarnings("unchecked")
    public Bitmap downloadImage(User user, ImageManager im) {
        Ajax a = new Ajax();
        TreeMap<String, String> params = new TreeMap<>();
        params.put("id", user.session);
        a.post("/getAvatar", params);
        Map<String, Object> res = a.response();
        if (res != null && res.containsKey("status") && res.get("status").equals("ok")) {
            Map<String, Object> image = (LinkedTreeMap<String, Object>) res.get("image");
            Bitmap avatar = im.listToBitmap((ArrayList<Double>) image.get("data"));
            user.setAvartar(avatar);
            return avatar;
        }
//TODO finish it
//        user.setAvartar(new Bitmap.createBitmap(getResources().getDrawable(R.drawable.ic_face_black_24dp)));
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


    /*
    to get number of friend request and display red dot
     */
    private void getNewFirendRequest(){
        int num = 0;
        Ajax a = new Ajax();
        TreeMap<String, String> params = new TreeMap<>();
        params.put("id", user.session);
        a.get("/getNewFriend", params);
        Map<String, Object> res = a.response();
        if(res!=null&&res.containsKey("status") && res.get("status").equals("ok")){
            num = (int)Float.parseFloat(res.get("friends").toString());
        }
        showDot(num);
    }

    private void showDot(int friendReq){
        newFriend = friendReq != 0;
        dot.setVisibility(newFriend ? View.VISIBLE : View.GONE);
    }
}
