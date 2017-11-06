package com.prog4.wangz_jamileh.wishlist;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.gson.internal.LinkedTreeMap;
import com.prog4.wangz_jamileh.wishlist.Model.Post;
import com.prog4.wangz_jamileh.wishlist.Model.User;
import com.prog4.wangz_jamileh.wishlist.adpater.PostAdapter;
import com.prog4.wangz_jamileh.wishlist.magic.Ajax;
import com.prog4.wangz_jamileh.wishlist.utility_manager.ImageManager;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Explore.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Explore#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Explore extends Fragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private  static final int SEARCH_RES_CODE = 4;
    private  static final int DETAIL_RES_CODE = 5;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View exploreView;
    private OnFragmentInteractionListener mListener;
    private SearchView search;

    private ListView list;
    public static ArrayList<Post> posts;
    private SwipeRefreshLayout swipeLayout;
    private TextView noResView;
    private String view_id; //the id of user which want to go to
    private TextView userInfoView;
    private CircleImageView userImgView;
    private ImageButton closeButton;
    private ImageManager im;
    private Bitmap otherAva;

    public Explore() {
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
     * @return A new instance of fragment Explore.
     */
    // TODO: Rename and change types and number of parameters
    public static Explore newInstance(String param1, String param2) {
        Explore fragment = new Explore();
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
        if(exploreView != null) return  exploreView;
        if(posts == null)posts = new ArrayList<Post>();
        if(im == null) im = new ImageManager(getActivity());
        exploreView = inflater.inflate(R.layout.fragment_explore, container, false);
        swipeLayout = (SwipeRefreshLayout) exploreView.findViewById(R.id.explore_swiperefresh);
        search = (SearchView) exploreView.findViewById(R.id.search);
        noResView = (TextView) exploreView.findViewById(R.id.exp_none);
        userInfoView = (TextView) exploreView.findViewById(R.id.exp_uname);
        userImgView = (CircleImageView) exploreView.findViewById(R.id.exp_userImg);
        closeButton = (ImageButton) exploreView.findViewById(R.id.exp_resume);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view_id = User.getInstance().session;
                posts = loading(view_id);
                createListView();
            }
        });
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent i = new Intent(getActivity(), SearchUserResultActivity.class);
                i.putExtra("query", query);
                startActivityForResult(i, SEARCH_RES_CODE);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        toggleRefresh(false);
        list = (ListView) exploreView.findViewById(R.id.explore_list);
        view_id = User.getInstance().session;
        posts = loading(view_id);
        createListView();
        return exploreView;
    }



    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SEARCH_RES_CODE && resultCode == getActivity().RESULT_OK && data != null) {
                view_id = data.getStringExtra("user");
                otherAva = data.getParcelableExtra("avatar");
                posts = loading(view_id);
                createListView();

//                view_id = null;
        }else if(requestCode == DETAIL_RES_CODE && resultCode == getActivity().RESULT_CANCELED && data != null){
            int pos = data.getIntExtra("pos", -1);
            if(posts.remove(pos) != null){
                createListView();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        search.clearFocus();
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
     * See the Android Training lesson <a href
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    private void createListView()
    {
        //Create an adapter for the listView and add the ArrayList to the adapter.
        list.setAdapter(new PostAdapter(getContext(), android.R.layout.simple_gallery_item, posts));
        if(posts == null || posts.size() == 0) noResView.setVisibility(View.VISIBLE);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position ,long id)
            {
                if(posts != null){
                    Intent i = new Intent(getActivity(), GiftDetailActivity.class);
                    i.putExtra("pos",  position);
                    startActivityForResult(i, DETAIL_RES_CODE);
                }
            }
        });

        list.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem == 0) {
                    // check if we reached the top or bottom of the list
                    View v = list.getChildAt(0);
                    int offset = (v == null) ? 0 : v.getTop();
                    if (offset == 0) {
                        //TODO refresh when reach top
                        Log.i("exp", "top");

                        return;
                    }
                } else if (totalItemCount - visibleItemCount == firstVisibleItem){
                    View v =  list.getChildAt(totalItemCount-1);
                    int offset = (v == null) ? 0 : v.getTop();
                    if (offset == 0) {
                        Log.i("exp", "down");
                        //TODO load more data when reach the bottom

                        return;
                    }
                }
            }
        });

    }

    /**
     * disable drag to refresh function
     * @param flag
     */
    private void toggleRefresh(boolean flag){
        swipeLayout.setRefreshing( flag );
        swipeLayout.setEnabled( flag );
    }

    @SuppressWarnings("unchecked")
    private ArrayList<Post> loading(String view_id){
        String id = User.getInstance().session;
        TreeMap<String, String> params = new TreeMap<>();
        params.put("id", id); //current login user
        params.put("view_id", view_id); //user data want to see
        Ajax a = new Ajax();
        Bitmap ava= null;
        ArrayList<LinkedTreeMap> posts_data = null;
        a.get("/showUserGift", params);
        Map<String, Object> res = a.response();
        if(res != null  && res.containsKey("status") && res.get("status").equals("ok")){
            LinkedTreeMap<String, Object> data = (LinkedTreeMap<String, Object>) res.get("data");
            if(data != null && data.containsKey("post")){
                posts_data = (ArrayList<LinkedTreeMap>)  data.get("post");
                if(posts_data.size() > 0){
                    noResView.setVisibility(View.GONE);
                }
                LinkedTreeMap<String, Object> names = ((LinkedTreeMap<String, Object>)(data.get("full_name")));
                String fullName = names.get("fName") + " " + names.get("lName");
                userInfoView.setText(fullName);
                if(!view_id.equals(id)) closeButton.setVisibility(View.VISIBLE);
                else closeButton.setVisibility(View.GONE);
                ava = view_id.equals(id) ? User.getInstance().avartar : otherAva;
                getUserAvatar(ava);
                convertPosts(posts_data, (LinkedTreeMap<String, Object>) (res.get("imageData")), view_id);
            }
        }

        return posts;
    }

    @SuppressWarnings("unchecked")
    private void convertPosts(ArrayList<LinkedTreeMap> data, LinkedTreeMap<String, Object> imageData, String view_id){
        if(posts.size() > 0) posts.clear();
        String[] update = null;
        String date = null;
        LinkedTreeMap<String, Object> one = null;
        LinkedTreeMap<String, Object> imgObj = null;
        Bitmap image = null;
        String post_id=null;
        for(int i = 0, len = data.size(); i < len; i++){
            image = null;
            one = data.get(i);
            update = one.get("updatedAt").toString().split("T");
            date = update[0] + " " + (update[1].split("\\."))[0];
            post_id = one.get("_id").toString();
            if(one.get("image").equals("file")){
                if(imageData != null && imageData.containsKey(post_id)){
                    imgObj = (LinkedTreeMap<String, Object>)(imageData.get(post_id));
                    image = im.listToBitmap((ArrayList<Double>) (imgObj.get("data")));
                }
            }
            posts.add(new Post(post_id, one.get("title").toString(), one.get("desc").toString(), Float.parseFloat(one.get("desire_level").toString()), Float.parseFloat(one.get("cost_level").toString()), !one.get("isMarked").toString().equals("none"), image, date, view_id));
        }
    }

    private void getUserAvatar(Bitmap avatar){
            if(avatar != null) userImgView.setImageBitmap(avatar);
            else userImgView.setImageDrawable(getResources().getDrawable(R.drawable.ic_face_black_24dp));
    }

}
