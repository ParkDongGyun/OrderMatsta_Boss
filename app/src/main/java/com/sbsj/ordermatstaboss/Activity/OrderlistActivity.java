package com.sbsj.ordermatstaboss.Activity;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.annotations.SerializedName;
import com.sbsj.ordermatstaboss.Adapter.OrderlistAdapter;
import com.sbsj.ordermatstaboss.DB.GoodsInfoColumn;
import com.sbsj.ordermatstaboss.DB.OrderDetailInfoColumn;
import com.sbsj.ordermatstaboss.Util.OnItemClickListener;
import com.sbsj.ordermatstaboss.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


public class OrderlistActivity extends BaseActivity {

    private final String TAG = "orderlist_activity";

    private Toolbar toolbar;
    private TextView tv_noorder;

    private RecyclerView recyclerView;
    private OrderlistAdapter adapter;

    ArrayList<GoodsInfoColumn> goodslist = new ArrayList<>();
    ArrayList<String> tablelist = new ArrayList<>();

    DatabaseReference firebaseRef = FirebaseDatabase.getInstance().getReference();

    ArrayList<String> orderlist_table = new ArrayList<>();
    ArrayList<String> orderlist_goodsinfo = new ArrayList<>();

//    ArrayList<OrderlistAdapter.Item> datalist = new ArrayList<>();

    private String store_id = Integer.toString(BaseActivity.menucateinfolist.get(0).getStore_id());
    private ArrayList<Order> orderlist = new ArrayList<>();

    private final String FCM_MESSAGE_URL = "https://fcm.googleapis.com/fcm/send";
    private final String SERVER_KEY = "AAAAxMlWc-Q:APA91bGQVfI3T-sR2NWbEzK4CK0zVnsRI4QEkE2iqbo74vxt20N_LpboDFC5gN3CY36eWfPagWOrQK5HuaFJ3cJX7gY38mTChuIUSdPwWfjTA4YgkwBf7XACNDvsvlNA6eLGvtBIGo9-";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderlist);

        toolbar = findViewById(R.id.tb_orderlist);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tv_noorder = findViewById(R.id.tv_noorderlist);
        recyclerView = findViewById(R.id.rcv_orderlist);

        /*adapter = new OrderlistAdapter(datalist, new OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
//                switch (v.getId()) {
//                    case R.id.btn_accept:
//                        new Thread(new MyThread(true, orderlist.get(position).token)).start();
//                        firebaseRef.child(store_id).child(orderlist.get(position).getId()).removeValue();
//                        orderlist.remove(position);
//                        adapter.getDatalist().remove(position);
//                        adapter.notifyDataSetChanged();
//                        break;
//                    case R.id.btn_decline:
//                        new Thread(new MyThread(false, orderlist.get(position).token)).start();
//                        firebaseRef.child(store_id).child(orderlist.get(position).getId()).removeValue();
//                        orderlist.remove(position);
//                        adapter.getDatalist().remove(position);
//                        adapter.notifyDataSetChanged();
//                        break;
//                }
            }
        });*/

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

//        firebaseRef.child(store_id).addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                Order order = (Order) dataSnapshot.getValue(Order.class);
//                order.setId(dataSnapshot.getKey());
//                orderlist.add(order);
//
//                OrderlistAdapter.Item places = new OrderlistAdapter.Item(OrderlistAdapter.HEADER, Integer.toString(order.getOrder_id()));
//                places.invisibleChildren = new ArrayList<>();
//                for(int i=0;i<order.orderdetail.size();++i) {
//                    places.invisibleChildren.add(new OrderlistAdapter.Item(CHILD, Integer.toString(order.getOrderdetail().get(i).getGoods_id())));
//                }
//
//                adapter.getDatalist().add(places);
//                adapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
    }

    private void deleteOrderKey(int position) {
        orderlist_goodsinfo.remove(position);
        orderlist_table.remove(position);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

//    private void updateAdapter(ArrayList<OrderlistAdapter.Item> list) {
//        if (!(list.get(0).text.isEmpty())) {
//            adapter.setDatalist(list);
//            adapter.notifyDataSetChanged();
//
//            tv_noorder.setVisibility(View.INVISIBLE);
//        } else {
//            tv_noorder.setVisibility(View.VISIBLE);
//        }
//    }

    private void pushFCM(boolean isOK, String client_token) {
        try {
            JSONObject jsonObject = new JSONObject();
            JSONObject notification = new JSONObject();
            if(isOK) {
                notification.put("body", "주문 완료");
                notification.put("title", "주문이 성공적으로 접수되었습니다.");
            } else {
                notification.put("body", "주문 취소");
                notification.put("title", "주문 접수가 취소되었습니다.");
            }
            jsonObject.put("notification", notification);
            jsonObject.put("to", client_token);

            URL url = new URL(FCM_MESSAGE_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.addRequestProperty("Authorization", "key=" + SERVER_KEY);
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Content-type", "application/json");
            OutputStream os = conn.getOutputStream();
            os.write(jsonObject.toString().getBytes("utf-8"));
            os.flush();
            conn.getResponseCode();
            Log.i(TAG, "Pass");

        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.i(TAG, "SendFCM MalformedURLException Error : " + e.getMessage());
        } catch (JSONException e) {
            e.printStackTrace();
            Log.i(TAG, "SendFCM MalformedURLException Error : " + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            Log.i(TAG, "SendFCM MalformedURLException Error : " + e.getMessage());
        }
    }

    @Keep
    public static class Order implements Serializable {
        @SerializedName("id")
        public String id;
        @SerializedName("order_id")
        public int order_id;
        @SerializedName("token")
        public String token;
        @SerializedName("orderdetail")
        public ArrayList<OrderDetailInfoColumn> orderdetail;

        public Order() {

        }

        public Order(String id, int order_id, String token, ArrayList<OrderDetailInfoColumn> orderdetail) {
            this.id = id;
            this.order_id = order_id;
            this.token = token;
            this.orderdetail = orderdetail;
        }

        public String getId() {
            return id;
        }

        public int getOrder_id() {
            return order_id;
        }

        public String getToken() {
            return token;
        }

        public ArrayList<OrderDetailInfoColumn> getOrderdetail() {
            return orderdetail;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setOrder_id(int order_id) {
            this.order_id = order_id;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public void setOrderdetail(ArrayList<OrderDetailInfoColumn> orderdetail) {
            this.orderdetail = orderdetail;
        }
    }

    class MyThread implements Runnable {
        private boolean isOK;
        private String client_token;

        public MyThread(boolean isOK, String client_token) {
            this.isOK = isOK;
            this.client_token = client_token;
        }

        @Override
        public void run() {
            pushFCM(isOK, client_token);
        }
    }
}
