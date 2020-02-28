package com.sbsj.ordermatstaboss.Fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.sbsj.ordermatstaboss.Activity.RegisterStoreActivity;
import com.sbsj.ordermatstaboss.Activity.StoreActivity;
import com.sbsj.ordermatstaboss.Adapter.OrderlistAdapter;
import com.sbsj.ordermatstaboss.DB.OrderDetailInfoColumn;
import com.sbsj.ordermatstaboss.R;

import java.util.ArrayList;

import static com.sbsj.ordermatstaboss.Activity.BaseActivity.ADD_CODE;
import static com.sbsj.ordermatstaboss.Activity.BaseActivity.masterinfo;

public class OrderFragment extends Fragment {

    private RecyclerView recyclerView;
    private OrderlistAdapter adapter;
    private TextView tv_noOrder;

    private OnFragmentInteractionListener mListener;

    public OrderFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order, container, false);

        tv_noOrder = view.findViewById(R.id.tv_storeorder);
        tv_noOrder.setVisibility(View.INVISIBLE);

        recyclerView = view.findViewById(R.id.rcv_storeorder);
        OrderDetailInfoColumn column = new OrderDetailInfoColumn();
        ArrayList<OrderDetailInfoColumn> list = new ArrayList<>();
        list.add(column);

        adapter = new OrderlistAdapter(getContext(), list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        MenuInflater menuInflater = getActivity().getMenuInflater();
        menuInflater.inflate(R.menu.tb_refresh, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_refresh:
                Toast.makeText(getActivity(), getString(R.string.prepare_service), Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
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
