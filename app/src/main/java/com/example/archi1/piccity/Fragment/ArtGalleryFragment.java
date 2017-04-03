package com.example.archi1.piccity.Fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.archi1.piccity.Adapter.ArtGalleryAdapter;
import com.example.archi1.piccity.Chat.AlistChat;
import com.example.archi1.piccity.Constant.Constant;
import com.example.archi1.piccity.Constant.Utils;
import com.example.archi1.piccity.Model.ArtGallery;
import com.example.archi1.piccity.R;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.quickblox.auth.QBAuth;
import com.quickblox.auth.model.QBSession;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.QBRestChatService;
import com.quickblox.chat.QBSystemMessagesManager;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.chat.model.QBChatMessage;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.QBSettings;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.request.QBPagedRequestBuilder;
import com.quickblox.core.request.QBRequestGetBuilder;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

import org.jivesoftware.smack.SmackException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.archi1.piccity.R.id.progressBar;
import static com.example.archi1.piccity.R.id.progress_bar;
import static com.example.archi1.piccity.R.id.view;
import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by archi1 on 11/25/2016.
 */

public class ArtGalleryFragment extends android.support.v4.app.Fragment implements View.OnClickListener {

    public Utils utils;
    public Bitmap bitmap;
    public ArrayList<ArtGallery> artGalleryArrayList;
    public ArtGalleryAdapter artGalleryAdapter;

    public ArrayList<String> categoryIdArray;
    public ArrayList<String> categoryNameArry;


    public GridView artPhotoGridView;
    public String userId;
    public TextView textView;
    public String strSelectedImage;
    public Spinner spCategory,spPrice,spLocation;

    public ImageView dialogCaptureImage;
    public String strPrice, strLctn, strDesc;
    public LinearLayout fragmentLinearlayout;
    public String selectedCategoryList;
    public ImageView ivSearch;
    public ArrayList<ArtGallery> filterCategoryNameArray;
    public QBUser userData;
    public static QBChatService chatService;
    public String uNameStr, uPwdStr;
    public ArrayList<String> arrayUserName;
    QBUser user;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_art_gallery, container, false);
        init(view);
        new getUploadedImage().execute();
        new getCategory().execute();

        QBSettings.getInstance().init(getActivity(), Constant.APP_ID, Constant.AUTH_KEY, Constant.AUTH_SECRET);
        QBSettings.getInstance().setAccountKey(Constant.ACCOUNT_KEY);

        utils = new Utils(getActivity());
        ((Activity) getActivity()).setTitle(R.string.art_list);
        userId = utils.ReadSharePrefrence(getActivity(), Constant.UserId);
        artPhotoGridView = (GridView) view.findViewById(R.id.fragment_art_gallery_grid);
        fragmentLinearlayout = (LinearLayout) view.findViewById(R.id.fragment_art_gallery_ll_camera);
       /* spCategory = (Spinner) view.findViewById(R.id.spnrCategoryArtGallery);
        ivSearch = (ImageView) view.findViewById(R.id.ivSearchArtGallery);*/

        spCategory = (Spinner)view.findViewById(R.id.spCategory);
        spLocation = (Spinner)view.findViewById(R.id.spLocation);


    spCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
       /* if (filterCategoryNameArray !=                                                                                                null) {
            filterCategoryNameArray.clear();
        }
*/
        Toast.makeText(getApplicationContext(), "ItemSelected"+position, Toast.LENGTH_SHORT).show();
      /*  if (selectedCategoryList.equalsIgnoreCase("all")) {

            artGalleryAdapter = new ArtGalleryAdapter(getActivity(), artGalleryArrayList);
            artPhotoGridView.setAdapter(artGalleryAdapter);
            artGalleryAdapter.notifyDataSetChanged();
            Log.d("msg", "filter if" + artGalleryArrayList.size());

        } else {

            Log.d("msg", "filter else " + filterCategoryNameArray.toString());

            for (int i = 0; i < artGalleryArrayList.size(); i++) {
                String name = artGalleryArrayList.get(i).getCategory();
                if (selectedCategoryList.equalsIgnoreCase(name)) {
                    filterCategoryNameArray.add(artGalleryArrayList.get(i));
                }
            }

            artGalleryAdapter = new ArtGalleryAdapter(getActivity(), filterCategoryNameArray);
            artPhotoGridView.setAdapter(artGalleryAdapter);
            artGalleryAdapter.notifyDataSetChanged();
        }*/
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
});



     // ivSearch.setOnClickListener(this);
        categoryIdArray = new ArrayList<>();
        categoryNameArry = new ArrayList<>();
        filterCategoryNameArray = new ArrayList<>();

        fragmentLinearlayout.setOnClickListener(this);


        uNameStr = Utils.ReadSharePrefrence(getActivity(), Constant.Email);
        uPwdStr = Utils.ReadSharePrefrence(getActivity(), Constant.USER_PASS);
        arrayUserName = new ArrayList<>();
//      new GetFriendList().execute();

        user = new QBUser();
        user.setEmail(uNameStr);
        user.setPassword(uPwdStr);
        Toast.makeText(getActivity(), ""+user, Toast.LENGTH_SHORT).show();
        createSession(user);

        return view;
    }

    private void createSession(final QBUser QBuser) {
        QBAuth.createSession(QBuser).performAsync(new QBEntityCallback<QBSession>() {
            @Override
            public void onSuccess(QBSession result, Bundle params) {
                // session created
                user.setId(result.getUserId());
                Log.d("ID",""+result.getUserId());

                chatService = QBChatService.getInstance();
                chatService.startAutoSendPresence(10);

                // LOG IN CHAT SERVICE
                if (!chatService.isLoggedIn())
                {
                    chatService.login(user, new QBEntityCallback<QBUser>() {
                        @Override
                        public void onSuccess(QBUser qbUser, Bundle args) {
                            Log.e("$$$$$$$$$$$$", "loged chat");
                            Toast.makeText(getApplicationContext(), "Logged in", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(QBResponseException errors) {
                            Log.e("$$$$$$$$$$$", "not loged\n" + errors.getMessage());
                            Toast.makeText(getApplicationContext(), "Fail " + errors, Toast.LENGTH_SHORT).show();
                            //error
                        }
                    });
                }
                loginForQuickBlox(QBuser);
            }

            @Override
            public void onError(QBResponseException responseException) {
                Toast.makeText(getActivity(), "Error :" + responseException.toString(), Toast.LENGTH_SHORT).show();
                Toast.makeText(getActivity(), "Session not created, please try again.", Toast.LENGTH_SHORT).show();
//              createSession(QBuser);
                //pd.setVisibility(View.GONE);

            }
        });

    }
    private boolean loginForQuickBlox(final QBUser user) {
        QBUsers.signIn(user).performAsync(new QBEntityCallback<QBUser>() {
            @Override
            public void onSuccess(QBUser qbUser, Bundle bundle) {
                Log.e("QBUSER", "********* " + qbUser);

                Toast.makeText(getActivity(), "Login Successfully", Toast.LENGTH_LONG).show();
                userData = new QBUser();
                userData.setEmail(uNameStr);
                userData.setPassword(uPwdStr);
                userData.setId(qbUser.getId());

                Log.e("UID "," ##### "+qbUser.getId());
                utils.WriteSharePrefrence(getActivity(), Constant.KEY_QB_USERID, "" + qbUser.getId());
                Log.e("USER", "******** USER DETAILS **********");
                Log.e("ID", "" + qbUser.getId());
                Log.e("LoginId", "" + qbUser.getLogin());
                Log.e("Email", "" + qbUser.getEmail());
                Log.e("NAME", "" + qbUser.getFullName());


                QBPagedRequestBuilder pagedRequestBuilder = new QBPagedRequestBuilder();
                pagedRequestBuilder.setPage(1);
                pagedRequestBuilder.setPerPage(50);
                QBUsers.getUsers(pagedRequestBuilder, bundle).performAsync(new QBEntityCallback<ArrayList<QBUser>>() {
                    @Override
                    public void onSuccess(ArrayList<QBUser> users, Bundle bundle) {
//                        Toast.makeText(getActivity(), "DATA", Toast.LENGTH_LONG).show();
                        int totalEntries = bundle.getInt("total_entries");
                        ArrayList<QBUser> arrySelectedUser = new ArrayList<QBUser>();
                        for (int i = 0; i < bundle.size(); i++) {
                            if (!utils.ReadSharePrefrence(getApplicationContext(), Constant.Email).equalsIgnoreCase(users.get(i).getEmail()) && !users.get(i).getEmail().equalsIgnoreCase("piccitipics@gmail.com")){
                                Log.e(">>>>>>>>> ", "=========== USERS ============" + i);
                                Log.e("ID", "" + users.get(i).getId());
                                Log.e("NAME", "" + users.get(i).getFullName());
                                Log.e("USER_ID", "" + users.get(i).getId());
                                Log.e("Email", "" + users.get(i).getEmail());
                                arrySelectedUser.add(users.get(i));
                            }
                        }

                       /* adapter = new ChatUserListAdapter(getActivity(), arrySelectedUser);
                        lvContacts.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        pd.setVisibility(View.GONE);*/
                    }

                    @Override
                    public void onError(QBResponseException e) {
                        Toast.makeText(getActivity(), "ERR " + e, Toast.LENGTH_SHORT);
                       // pd.setVisibility(View.GONE);
                    }
                });


                QBRequestGetBuilder requestBuilder = new QBRequestGetBuilder();
                requestBuilder.setLimit(100);

                QBRestChatService.getChatDialogs(null, requestBuilder).performAsync(
                        new QBEntityCallback<ArrayList<QBChatDialog>>() {
                            @Override
                            public void onSuccess(ArrayList<QBChatDialog> result, Bundle params) {
                                int totalEntries = params.getInt("total_entries");
                                ArrayList<QBChatDialog> arrySelectedUserChat = new ArrayList<QBChatDialog>();
                                for (int i = 0; i < result.size(); i++) {

                                    if (result.get(i).getType().toString().equalsIgnoreCase("GROUP")) {
                                        Log.e(">>>>>>>>> ", "===========GROUP============" + i);
                                        Log.e("ID", "" + result.get(i).getDialogId());
                                        Log.e("NAME", "" + result.get(i).getName());
                                        Log.e("USER_ID", "" + result.get(i).getUserId());
                                        Log.e("RECEIPNT_ID", "" + result.get(i).getRecipientId());
                                        arrySelectedUserChat.add(result.get(i));
                                    } else {
//                                    for (int k = 0; k < arrayUserName.size(); k++) {
//                                    if (dialogs.get(i).getName().equals(arrayUserName.get(k))) {
                                        Log.e("ADDED ", "" + result.get(i).getName());
                                        Log.e(">>>>>>>>> ", "===========PRIVATE============" + i);
                                        Log.e("ID", "" + result.get(i).getDialogId());
                                        Log.e("NAME", "" + result.get(i).getName());
                                        Log.e("USER_ID", "" + result.get(i).getUserId());
                                        Log.e("RECEIPNT_ID", "" + result.get(i).getRecipientId());
                                        // ADD Record TO the List
                                        arrySelectedUserChat.add(result.get(i));

                                    }
                                }
                              /*  if (arrySelectedUserChat.size() > 0) {
                                    adapterChats = new ChatListQBAdapter(getActivity(), arrySelectedUserChat);
                                    lvChats.setAdapter(adapterChats);
                                    adapterChats.notifyDataSetChanged();
                                }
                                pd.setVisibility(View.GONE);*/
                            }

                            @Override
                            public void onError(QBResponseException responseException) {
                               // pd.setVisibility(View.GONE);
                            }
                        });


            }

            @Override
            public void onError(QBResponseException e) {
                Toast.makeText(getActivity(), "Exception " + e, Toast.LENGTH_SHORT).show();
               // pd.setVisibility(View.GONE);
            }
        });

        return true;
    }


    private void init(View view) {

    }


    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(R.string.art_list);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.fragment_art_gallery_ll_camera:
                Log.d("gopu", "click");

                Bundle bundle = new Bundle();
                bundle.putString("Royalty", "isCameara");
                Fragment fragment = new SaleStuffFragment();
                fragment.setArguments(bundle);
                android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_body, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

                break;

           /* case R.id.spCategory:


                Log.d("msg", "filter " + filterCategoryNameArray.toString());
                break;*/
        }
    }



    /*@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 40, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                strSelectedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
                dialogCaptureImage.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }*/

    /*private String getPath(Uri uri) {
        String result;
        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
        if (cursor == null) {
            result = uri.getPath();
        } else {

            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;

    }*/

    public class getCategory extends AsyncTask<String, String, String> {
        public ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String urlGetCategory = Constant.Base_URL + "get_category_list.php";
            return utils.MakeServiceCall(urlGetCategory);

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            try {

                categoryIdArray.add("5");
                categoryNameArry.add("all");

                JSONObject jsonObject = new JSONObject(s);
                String status = jsonObject.getString("status");

                if (status.equalsIgnoreCase("true")) {
                    JSONArray jsonArrayData = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArrayData.length(); i++) {

                        JSONObject jsonObjectData = jsonArrayData.getJSONObject(i);
                        String categoryid = jsonObjectData.getString("category_id");
                        String categoryName = jsonObjectData.getString("category_name");

                        categoryIdArray.add(categoryid);
                        categoryNameArry.add(categoryName);


                    }


                    ArrayAdapter<String> spnerCategoryadapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item_view, categoryNameArry);
                    spCategory.setAdapter(spnerCategoryadapter);

                    spCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            selectedCategoryList = categoryNameArry.get(position);
                            Log.d("msg", "selected category " + selectedCategoryList);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });


                }
            } catch (JSONException e1) {
                e1.printStackTrace();
            }

        }
    }

    /*private class uploadImage extends AsyncTask<String,String,String>{

        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Please Wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("user_id",Utils.ReadSharePrefrence(getActivity(),Constant.UserId));
            hashMap.put("image",strSelectedImage);
            hashMap.put("price",strPrice);
            hashMap.put("description",strDesc);
            hashMap.put("location",strLctn);
            return utils.getResponseofPost(Constant.Base_URL+"image_upload.php?",hashMap);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject object = new JSONObject(s);
                if (object.getString("successful").equalsIgnoreCase("true")){
                    Toast.makeText(getActivity(), ""+object.getString("msg"), Toast.LENGTH_SHORT).show();
                    new getUploadedImage().execute();
                }else {
                    Toast.makeText(getActivity(), "Something Went Wrong", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            progressDialog.dismiss();
        }
    }*/

    private class getUploadedImage extends AsyncTask<String, String, String> {
        ProgressDialog pd;//;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

          /*//=new ProgressDialog(getActivity());
            //.setMessage("Loading...");
            //.setCancelable(false);
            //.show();*/
            artGalleryArrayList = new ArrayList<>();
        }


        @Override
        protected String doInBackground(String... params) {
            /*http://web-medico.com/web1/pic_citi/Api/get_upload_image.php?user_id=25*/

            /*http://web-medico.com/web1/pic_citi/Api/get_alluser_upload.php*/
            String Url = Constant.Base_URL + "get_alluser_upload.php";
            Log.d("UploadUrl", Url);

            return utils.getResponseofGet(Url);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
           /* //.dismiss();*/
            Log.d("msg", "ArtGAllery" + s);
            try {

                JSONObject jsonObject = new JSONObject(s);

                if (jsonObject.getString("status").equalsIgnoreCase("true")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject object = jsonArray.getJSONObject(i);
                        ArtGallery artGallery = new ArtGallery();
                        artGallery.setId(object.getString("id"));
                        artGallery.setImage(object.getString("image"));
                        artGallery.setPrice(object.getString("price"));
                        artGallery.setDescription(object.getString("description"));
                      //  artGallery.setLocation(object.getString("location"));
                        artGallery.setUsername(object.getString("user_name"));
                        artGallery.setName(object.getString("name"));
                        artGallery.setUserId(object.getString("user_id"));
                        artGallery.setCategory(object.getString("category_name"));
                        artGallery.setCurrency(object.getString("currency"));
                        Log.d("Image",object.getString("image"));
                        artGalleryArrayList.add(artGallery);
                    }

                    Log.d("artGAllery", artGalleryArrayList.toString());
                    artGalleryAdapter = new ArtGalleryAdapter(getActivity(), artGalleryArrayList);
                    artPhotoGridView.setAdapter(artGalleryAdapter);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

/*    public static QBChatMessage createChatNotificationForGroupChatCreation(QBChatDialog dialog) {
        String dialogId = String.valueOf(dialog.getDialogId());
        String roomJid = dialog.getRoomJid();
        String occupantsIds = TextUtils.join(",", dialog.getOccupants());
        String dialogName = dialog.getName();
        String dialogTypeCode = String.valueOf(dialog.getType().ordinal());
        Log.e("DATA ", ">>>>>>>>>>>>>>>>>>>>>>>>>>>");
        Log.e("dialogID", ">> " + dialogId);
        Log.e("roomJid", ">> " + roomJid);
        Log.e("occupantsIds", ">> " + occupantsIds);
        Log.e("dialogName", ">> " + dialogName);
        Log.e("dialogTypeCode", ">> " + dialogTypeCode);


        QBChatMessage chatMessage = new QBChatMessage();
        chatMessage.setBody("optional text");

        // Add notification_type=1 to extra params when you created a group chat
        //
        chatMessage.setProperty("notification_type", "2");

        chatMessage.setProperty("_id", dialogId);
        if (!TextUtils.isEmpty(roomJid)) {
            chatMessage.setProperty("room_jid", roomJid);
        }
        chatMessage.setProperty("occupants_ids", occupantsIds);
        if (!TextUtils.isEmpty(dialogName)) {
            chatMessage.setProperty("name", dialogName);
        }
        chatMessage.setProperty("type", dialogTypeCode);
        return chatMessage;
    }

    private QBChatMessage buildSystemMessageAboutCreatingGroupDialog(QBChatDialog dialog) {
        QBChatMessage qbChatMessage = new QBChatMessage();
        qbChatMessage.setDialogId(dialog.getDialogId());
        qbChatMessage.setProperty(PROPERTY_OCCUPANTS_IDS, String.valueOf(dialog.getOccupants()));
        qbChatMessage.setProperty(PROPERTY_DIALOG_TYPE, String.valueOf(dialog.getType().getCode()));
        qbChatMessage.setProperty(PROPERTY_DIALOG_NAME, String.valueOf(dialog.getName()));
        qbChatMessage.setProperty(PROPERTY_NOTIFICATION_TYPE, CREATING_DIALOG);

        return qbChatMessage;
    }

    //Let's notify occupants
    public void sendSystemMessageAboutCreatingDialog(QBSystemMessagesManager systemMessagesManager, QBChatDialog dialog) {
        QBChatMessage systemMessageCreatingDialog = buildSystemMessageAboutCreatingGroupDialog(dialog);

        for (Integer recipientId : dialog.getOccupants()) {
            if (!recipientId.equals(chatService.getUser().getId())) {
                systemMessageCreatingDialog.setRecipientId(recipientId);
                try {
                    systemMessagesManager.sendSystemMessage(systemMessageCreatingDialog);
                } catch (SmackException.NotConnectedException e) {
                    e.printStackTrace();
                }
            }
        }
    }*/
}
