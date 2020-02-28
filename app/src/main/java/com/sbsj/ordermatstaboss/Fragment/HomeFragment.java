package com.sbsj.ordermatstaboss.Fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.sbsj.ordermatstaboss.Activity.BaseActivity;
import com.sbsj.ordermatstaboss.Activity.RegisterStoreActivity;
import com.sbsj.ordermatstaboss.Activity.StoreActivity;
import com.sbsj.ordermatstaboss.Activity.StoreDetailActivity;
import com.sbsj.ordermatstaboss.Adapter.StoreListAdapter;
import com.sbsj.ordermatstaboss.DB.MasterDeviceInfo;
import com.sbsj.ordermatstaboss.DB.MasterDeviceInfoColumn;
import com.sbsj.ordermatstaboss.DB.MasterInfo;
import com.sbsj.ordermatstaboss.DB.StoreInfo;
import com.sbsj.ordermatstaboss.DB.StoreInfoColumn;
import com.sbsj.ordermatstaboss.R;
import com.sbsj.ordermatstaboss.Util.OnItemClickListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.sbsj.ordermatstaboss.Activity.BaseActivity.ADD_CODE;
import static com.sbsj.ordermatstaboss.Activity.BaseActivity.PERMISSION_CODE_PHONE;
import static com.sbsj.ordermatstaboss.Activity.BaseActivity.b_directory;
import static com.sbsj.ordermatstaboss.Activity.BaseActivity.b_sns_id;
import static com.sbsj.ordermatstaboss.Activity.BaseActivity.masterinfo;

public class HomeFragment extends Fragment {

    private final String TAG = "Store_Home_Fragment";

    private StoreActivity storeActivity;

    private RecyclerView recyclerView;
    private StoreListAdapter adapter;
    private TextView tv_noStore;

    private OnFragmentInteractionListener mListener;

    public HomeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        storeActivity = (StoreActivity)getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        tv_noStore = view.findViewById(R.id.tv_nostore);

        recyclerView = view.findViewById(R.id.rcv_storehome);
        ArrayList<StoreInfoColumn> arrayList = new ArrayList<>();
        adapter = new StoreListAdapter(getContext(), arrayList, new OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Intent intent = new Intent(storeActivity, StoreDetailActivity.class);
                intent.putExtra(getString(R.string.store_id), adapter.getItem(position).getId());
                startActivity(intent);
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        getMasterInfo();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case PERMISSION_CODE_PHONE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getMasterInfo();
                } else {
                    Toast.makeText(getContext(), getString(R.string.permission_msg), Toast.LENGTH_LONG).show();
                }
                return;
        }
    }

    public void getMasterInfo() {
        if (!storeActivity.networkConnect()) {
            Toast.makeText(storeActivity, getString(R.string.check_network), Toast.LENGTH_SHORT).show();
            return;
        }

        Call<MasterInfo> call = storeActivity.getDbService().getMasterInfo(b_directory, b_sns_id);
        call.enqueue(new Callback<MasterInfo>() {
            @Override
            public void onResponse(Call<MasterInfo> call, Response<MasterInfo> response) {
                if (response.message().equals(getString(R.string.NetworkCheck))) {
                    BaseActivity.masterinfo = response.body().getMasterlist().get(0);
                    storeActivity.getMasterDeviceInfo(BaseActivity.masterinfo.getId());
                    getStoreDB(masterinfo.getId());
                    Log.i(TAG, "master_id : " + masterinfo.getId());
                } else {
                    Log.i(TAG, "Fail : " + response.message());
                }
            }

            @Override
            public void onFailure(Call<MasterInfo> call, Throwable t) {
                Log.i(TAG, "Error : " + t.getMessage());
            }
        });
    }

    private void getStoreDB(int master_id) {
        if (!storeActivity.networkConnect()) {
            Toast.makeText(storeActivity, getString(R.string.check_network), Toast.LENGTH_SHORT).show();
            return;
        }

        Call<StoreInfo> call = storeActivity.getDbService().getstoreinfobymasterid(master_id);
        call.enqueue(new Callback<StoreInfo>() {
            @Override
            public void onResponse(Call<StoreInfo> call, Response<StoreInfo> response) {
                if (response.message().equals(getString(R.string.NetworkCheck))) {
                    BaseActivity.storeinfolist = response.body().getStorelist();
                    updateAdapter(response.body().getStorelist());
                } else {
                    Log.i(TAG, "result : " + response.message());
                }
            }

            @Override
            public void onFailure(Call<StoreInfo> call, Throwable t) {
                Log.i(TAG, "error : " + t.getMessage());
                tv_noStore.setVisibility(View.VISIBLE);
            }
        });
    }

    private void updateAdapter(ArrayList<StoreInfoColumn> list) {
        if (list.get(0).getId() != -1) {
            adapter.getDatalist().clear();
            adapter.setDatalist(list);
            tv_noStore.setVisibility(View.INVISIBLE);
        } else {
            adapter.getDatalist().clear();
            tv_noStore.setVisibility(View.VISIBLE);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
