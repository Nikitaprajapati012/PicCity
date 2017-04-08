package com.example.archi1.piccity.Activity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.archi1.piccity.Adapter.ChatAdapter;
import com.example.archi1.piccity.Constant.Constant;
import com.example.archi1.piccity.Constant.Utils;
import com.example.archi1.piccity.Model.MessageDetails;
import com.example.archi1.piccity.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.example.archi1.piccity.Constant.Constant.MESSAGE_MAX_LENGTH;
import static com.example.archi1.piccity.Constant.Constant.MESSAGE_MIN_LENGTH;

public class ChatActivity extends AppCompatActivity {

    public static ChatAdapter chatAdapter;
    public static List<MessageDetails> listmsgdetails;
    public String strMsg;


    @BindView(R.id.activity_test_msg_edt)
    public EditText msgEdt;
    @BindView(R.id.activity_test_send)
    public ImageView sendIV;
    String user_id_img, user_id;
    Utils utils;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        recyclerView = (RecyclerView) findViewById(R.id.activity_test_msg_recycler);
        utils = new Utils(this);
        //user_id =Utils.ReadSharePrefrence(this,Constant.USER_ID);  //"86";
        user_id_img = getIntent().getStringExtra("user_id");
        Log.d("ID@#",""+user_id_img);

        sendIV = (ImageView) findViewById(R.id.activity_test_send);
        msgEdt = (EditText) findViewById(R.id.activity_test_msg_edt);
        user_id = Utils.ReadSharePrefrence(this, Constant.USER_ID);


        sendIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strMsg = msgEdt.getText().toString();
                new SendMessage().execute();

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // TODO: 07-Apr-17 API Call for get chat
        new GetChat().execute();
    }

    private class GetChat extends AsyncTask<String, String, String> {
        private ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            listmsgdetails = new ArrayList<>();
            pd = new ProgressDialog(ChatActivity.this);
            pd.setMessage("Loading...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String receivemessage = Constant.Base_URL + "get_chat_msg.php?" + "sender_id=" + user_id + "&recipient_id=" + user_id_img;
            Log.d("URL", receivemessage);
            return Utils.getResponseofGet(receivemessage);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
            Log.d("response", s.toString());
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("true")) {

                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {

                        MessageDetails messageDetails = new MessageDetails();
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        messageDetails.setId(jsonObject1.getString("id"));
                        messageDetails.setSender(jsonObject1.getString("sender_id"));
                        messageDetails.setRecipient(jsonObject1.getString("recipient_id"));
                        messageDetails.setText(jsonObject1.getString("msg"));

                        listmsgdetails.add(messageDetails);

                    }
                    chatAdapter = new ChatAdapter(ChatActivity.this, listmsgdetails);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(chatAdapter);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class SendMessage extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            String sendmessage = Constant.Base_URL + "insert_chat_msg.php?sender_id=" + user_id + "&recipient_id=" + user_id_img + "&msg=" + strMsg;
            Log.d("URL", "" + sendmessage);
            return Utils.getResponseofGet(sendmessage);
        }

        @Override
        protected void onPostExecute(String s) {
            Log.d("POST EXECUTE", "" + s);
            try {
                if (msgEdt.length() > MESSAGE_MIN_LENGTH && msgEdt.length() < MESSAGE_MAX_LENGTH) {

                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.getString("status").equalsIgnoreCase("true")) {

                        Toast.makeText(ChatActivity.this, "Message Sent", Toast.LENGTH_SHORT).show();

                        MessageDetails details = new MessageDetails();
                        details.setSender("5");
                        details.setRecipient("1");
                        details.setName("archi");
                        details.setSubject("Archirayan");
                        details.setText(msgEdt.getText().toString());

                        listmsgdetails.add(details);
                        msgEdt.setText("");
                        chatAdapter.notifyDataSetChanged();

                        Log.d("Length@", "" + listmsgdetails.size());

                    } else {
                        Toast.makeText(ChatActivity.this, "Message Sent Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            super.onPostExecute(s);
        }
    }
}
