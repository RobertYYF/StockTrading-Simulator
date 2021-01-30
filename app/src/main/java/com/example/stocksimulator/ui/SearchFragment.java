package com.example.stocksimulator.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.stocksimulator.R;
import com.example.stocksimulator.model.MarketInfo;
import com.example.stocksimulator.model.Stock;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Map;


public class SearchFragment extends Fragment {

    ListView listView;
    SearchView searchView;
    ArrayList<String> stocks = getStocks(MarketInfo.getInstance().getStocks());
    ImageButton backBtn;

    public SearchFragment() {
        // Required empty public constructor
    }

//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
//            @Override
//            public void handleOnBackPressed() {
//                // Handle the back button event
//            }
//        };
//        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
//
//    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_search, container, false);

        BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.nav_view);
        bottomNavigationView.setVisibility(View.INVISIBLE);
        getActivity().findViewById(R.id.toolbar1).setVisibility(View.INVISIBLE);
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar2);
        toolbar.setVisibility(View.VISIBLE);

        backBtn = (ImageButton) toolbar.getChildAt(0);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                int destination_id = 0;
                while (!navController.getBackStack().isEmpty()) {
                    int temp_id = navController.getBackStack().pop().getDestination().getId();
                    System.out.println(temp_id);
                    if (temp_id != getId() && temp_id != R.layout.fragment_search) {
                        destination_id = temp_id;
                        break;
                    }
                }
                navController.navigate(destination_id);
                bottomNavigationView.setVisibility(View.VISIBLE);
                getActivity().findViewById(R.id.toolbar1).setVisibility(View.VISIBLE);
                Toolbar toolbar = getActivity().findViewById(R.id.toolbar2);
                toolbar.setVisibility(View.INVISIBLE);

            }
        });

        listView = root.findViewById(R.id.outputList);
        listView.setAdapter(new ArrayAdapter(root.getContext(), android.R.layout.simple_list_item_1, stocks));
        listView.setTextFilterEnabled(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //showEditDialog();
                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                Bundle extras = new Bundle();
                extras.putString("selected_stock", MarketInfo.getInstance().getStocks().get(adapterView.getItemAtPosition(i)).getCode());
                extras.putString("selected_name", MarketInfo.getInstance().getStocks().get(adapterView.getItemAtPosition(i)).getName());
                System.out.println(MarketInfo.getInstance().getStocks().get(adapterView.getItemAtPosition(i)).getCode() + MarketInfo.getInstance().getStocks().get(adapterView.getItemAtPosition(i)).getName());
                navController.navigate(R.id.singleStockFragment, extras);
            }
        });


        searchView = (SearchView) toolbar.getChildAt(1);
        searchView.setSubmitButtonEnabled(true);
        searchView.setIconifiedByDefault(true);
        searchView.setFocusable(true);
        searchView.setIconified(false);
        searchView.requestFocusFromTouch();
        searchView.setImeOptions(6);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {//点击提交按钮时
                Log.i("Input: ", query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {//搜索框内容变化时

                if (TextUtils.isEmpty(newText)) {
                    //清除ListView的过滤
                    listView.clearTextFilter();
                } else {
                    //使用用户输入的内容对ListView的列表项进行过滤
                    listView.setFilterText(newText);
                }
                return true;
            }
        });


        return root;
    }



    private ArrayList<String> getStocks(Map<String, Stock> map) {

        ArrayList<String> stocks = new ArrayList<>();

        for (String key : map.keySet()) {
            stocks.add(map.get(key).getName());
        }

        return stocks;
    }

}