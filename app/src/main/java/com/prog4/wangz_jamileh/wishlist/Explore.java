package com.prog4.wangz_jamileh.wishlist;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.internal.LinkedTreeMap;
import com.prog4.wangz_jamileh.wishlist.Model.Post;
import com.prog4.wangz_jamileh.wishlist.Model.User;
import com.prog4.wangz_jamileh.wishlist.adpater.PostAdapter;
import com.prog4.wangz_jamileh.wishlist.magic.Ajax;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

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

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View exploreView;
    private OnFragmentInteractionListener mListener;

    private ListView list;
    public static ArrayList<Post> posts;
    private SwipeRefreshLayout swipeLayout;

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
        exploreView = inflater.inflate(R.layout.fragment_explore, container, false);
        swipeLayout = (SwipeRefreshLayout) exploreView.findViewById(R.id.explore_swiperefresh);
        toggleRefresh(false);
        list = (ListView) exploreView.findViewById(R.id.explore_list);
        posts = loading();
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
        list.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position ,long id)
            {
                if(posts != null){
                    Intent i = new Intent(getActivity(), GiftDetailActivity.class);
                    i.putExtra("pos",  position);
                    startActivity(i);
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
//                        toggleRefresh(true);
                        return;
                    }
                } else if (totalItemCount - visibleItemCount == firstVisibleItem){
                    View v =  list.getChildAt(totalItemCount-1);
                    int offset = (v == null) ? 0 : v.getTop();
                    if (offset == 0) {
                        //TODO load more data when reach the bottom
                        return;
                    }
                }
            }
        });

    }



    private void toggleRefresh(boolean flag){
        swipeLayout.setRefreshing( flag );
        swipeLayout.setEnabled( flag );
    }

    @SuppressWarnings("unchecked")
    private ArrayList<Post> loading(){
        String id = User.getInstance().session;
        TreeMap<String, String> params = new TreeMap<>();
        params.put("id", id);
        params.put("view_id", id);
        Ajax a = new Ajax();
        a.get("/showUserGift", params);
        Map<String, Object> res = a.response();
        if(res != null  && res.containsKey("status") && res.get("status").equals("ok")){
            LinkedTreeMap<String, Object> data = (LinkedTreeMap<String, Object>) res.get("data");
            if(data != null && data.containsKey("post")){
                convertPosts((ArrayList<LinkedTreeMap>) data.get("post"));
            }
        }

        return posts;
    }

    private void convertPosts(ArrayList<LinkedTreeMap> data){
        posts = new ArrayList<Post>();
        String[] update = null;
        String date = null;
        LinkedTreeMap<String, Object> one = null;
        for(int i = 0, len = data.size(); i < len; i++){
            one = data.get(i);
            update = one.get("updatedAt").toString().split("T");
            date = update[0] + " " + (update[1].split("\\."))[0];
            posts.add(new Post(one.get("_id").toString(), one.get("title").toString(), one.get("desc").toString(), Float.parseFloat(one.get("desire_level").toString()), Float.parseFloat(one.get("cost_level").toString()), Boolean.valueOf(one.get("isMarked").toString()), null, date));
        }
    }



}
