package com.example.archi1.piccity.Fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.archi1.piccity.Adapter.PicCitiFriendsPager;
import com.example.archi1.piccity.Constant.Constant;
import com.example.archi1.piccity.Constant.Utils;
import com.example.archi1.piccity.Model.PersonalArtistPage;
import com.example.archi1.piccity.R;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/*** Created by Ravi archi on 1/10/2017.
 */
public class PicCitiFriendsFragment extends Fragment implements TabLayout.OnTabSelectedListener {
    public String userId;
    public Bitmap bitmap;
    public Utils utils;
    TabLayout tabLayout;
    ViewPager viewPager;
    public Dialog dialog;
    public ArrayList<PersonalArtistPage> arrayList;
    public PersonalArtistPage details;
    public CircleImageView imgProfilepic;
    public TextView txtUserName;
    public RelativeLayout layoutUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_picciti_friends, container, false);
        ButterKnife.bind(this, view);
        imgProfilepic = (CircleImageView) view.findViewById(R.id.fragment_piccitifriends_imgloginprofilepic);
        txtUserName = (TextView) view.findViewById(R.id.fragment_piccitifriends_txtloginusername);
        layoutUser = (RelativeLayout) view.findViewById(R.id.fragment_piccitifriends_userlayout);
        init();
        tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        viewPager = (ViewPager) view.findViewById(R.id.pager);
        tabLayout.addTab(tabLayout.newTab().setText("All User"));
        tabLayout.addTab(tabLayout.newTab().setText("Friend List"));
        tabLayout.addTab(tabLayout.newTab().setText("Friend Requests"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        // TODO: 3/22/2017 set view pager
        PicCitiFriendsPager adapter = new PicCitiFriendsPager(getActivity().getSupportFragmentManager(), tabLayout.getTabCount());
        // TODO: 3/22/2017  Adding adapter to pager
        viewPager.setAdapter(adapter);
        // TODO: 3/22/2017  Adding onTabSelectedListener to swipe views
        tabLayout.setOnTabSelectedListener(this);
        click();
        return view;
    }

    // TODO: 5/9/2017 perfrom the click events
    private void click() {
        layoutUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new PersonalArtistPageFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                Gson gson = new Gson();
                Bundle bundle = new Bundle();
                bundle.putString("friendlistdetails",gson.toJson(details));
                if (fragment != null) {
                    fragment.setArguments(bundle);
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.container_body, fragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            }
        });
    }

    // TODO: 4/14/2017 intialization
    private void init() {
        getActivity().setTitle(R.string.personal_artist);
        arrayList = new ArrayList<>();
        utils = new Utils(getActivity());
        userId = Utils.ReadSharePrefrence(getActivity(), Constant.USER_ID);
        new GetUserDetails(userId).execute();
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    // TODO: 5/9/2017 get login user details
    private class GetUserDetails extends AsyncTask<String, String, String> {
        ProgressDialog pd;
        String userid;

        public GetUserDetails(String userId) {
            this.userid = userId;
        }
        @Override
        protected void onPreExecute () {
            super.onPreExecute();
            arrayList = new ArrayList<>();
            pd = new ProgressDialog(getActivity());
            pd.setMessage("Loading...");
            pd.setCancelable(false);
            pd.show();
        }
        @Override
        protected String doInBackground(String... strings) {
            // http://web-medico.com/web1/pic_citi/Api/user_details.php?user_id=203
            return Utils.getResponseofGet(Constant.Base_URL + "user_details.php?user_id=" + userid);
        }
        @Override
        protected void onPostExecute (String s) {
            super.onPostExecute(s);
            Log.d("RESPONSE USER DETAILS", "" + s);
            pd.dismiss();
            try {
                JSONObject jsonObject = new JSONObject(s);
                arrayList = new ArrayList<>();
                if (jsonObject.getString("status").equalsIgnoreCase("true")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        details = new PersonalArtistPage();
                        JSONObject object = jsonArray.getJSONObject(i);
                        details.setUserId(object.getString("id"));
                        details.setUserName(object.getString("name"));
                        details.setUserEmail(object.getString("email"));
                        details.setUserContactNumber(object.getString("phone"));
                        details.setUserPayPalEmail(object.getString("paypal_email"));
                        details.setUserImage(object.getString("user_image"));
                        arrayList.add(details);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (arrayList.size() > 0) {
                showUserDetails();
            }
        }
    }

    // TODO: 5/9/2017 store login user details
    private void showUserDetails() {
        txtUserName.setText(details.getUserName());
        Picasso.with(getActivity()).load(details.getUserImage()).placeholder(R.drawable.ic_placeholder).into(imgProfilepic);
    }
}
