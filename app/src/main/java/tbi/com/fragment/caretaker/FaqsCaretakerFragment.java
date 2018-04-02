package tbi.com.fragment.caretaker;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import tbi.com.R;
import tbi.com.adapter.CaretakerFAQAdapter;
import tbi.com.custom_view.DailogView;
import tbi.com.model.FaqList;
import tbi.com.pagination.EndlessRecyclerViewScrollListener;
import tbi.com.session.Session;
import tbi.com.util.Constant;
import tbi.com.util.Utils;
import tbi.com.vollyemultipart.VolleyMultipartRequest;
import tbi.com.vollyemultipart.VolleySingleton;

public class FaqsCaretakerFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = FaqsCaretakerFragment.class.getSimpleName();
    public CaretakerFAQAdapter caretakerFAQAdapter;
    public ArrayList<FaqList> faqLists;
    public ImageView iv_for_delete;
    private RecyclerView recycler_view;
    private Session session;
    private LinearLayout layout_for_addDelete;
    private RelativeLayout mainLayout;
    private TextView tv_for_noData;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private int start = 0;

    public FaqsCaretakerFragment() {
        // Required empty public constructor
    }

    public static FaqsCaretakerFragment newInstance(String param1) {
        FaqsCaretakerFragment fragment = new FaqsCaretakerFragment();
        Bundle args = new Bundle();
        args.putString("param1", param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String mParam1 = getArguments().getString("param1");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_faqs_caretaker, container, false);
        initView(view);
        session = new Session(getContext());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recycler_view.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setStackFromEnd(false);

        faqLists = new ArrayList<>();
        caretakerFAQAdapter = new CaretakerFAQAdapter(faqLists, getContext(), this);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                faqLists.clear();
                start = 0;
                getAllFAQListAPI();
            }
        });
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
                faqLists.clear();
                start = 0;
                getAllFAQListAPI();
            }
        });
        recycler_view.setAdapter(caretakerFAQAdapter);

        EndlessRecyclerViewScrollListener scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                loadNextDataFromApi(page, totalItemsCount);
            }
        };
        recycler_view.addOnScrollListener(scrollListener);

        return view;
    }

    public void loadNextDataFromApi(int page, int totalItemsCount) {
        getAllFAQListAPI();
    } // pagination

    private void initView(View view) {
        recycler_view = view.findViewById(R.id.recycler_view);
        mainLayout = view.findViewById(R.id.mainLayout);
        tv_for_noData = view.findViewById(R.id.tv_for_noData);
        mSwipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);

        layout_for_addDelete = getActivity().findViewById(R.id.layout_for_addDelete);
        iv_for_delete = getActivity().findViewById(R.id.iv_for_delete);

        getActivity().findViewById(R.id.tv_for_addNew).setOnClickListener(this);
        getActivity().findViewById(R.id.tv_for_faqDelete).setOnClickListener(this);
        iv_for_delete.setOnClickListener(this);
        mainLayout.setOnClickListener(this);
    }

    public void getAllFAQListAPI() {

        if (Utils.isNetworkAvailable(getContext())) {

            int limit = 20;
            VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.GET, Constant.URL_WITH_LOGIN + "faqList?" + "limit=" + limit + "&start=" + start, new Response.Listener<NetworkResponse>() {
                @Override
                public void onResponse(NetworkResponse response) {
                    String data = new String(response.data);
                    Log.e("Response", data);

                    try {
                        JSONObject jsonObject = new JSONObject(data);

                        String status = jsonObject.getString("status");

                        if (status.equalsIgnoreCase("success")) {
                            mSwipeRefreshLayout.setRefreshing(false);
                            tv_for_noData.setVisibility(View.GONE);
                            faqLists.clear();
                            JSONArray jsonArray = jsonObject.getJSONArray("faqList");
                            if (jsonArray != null) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    FaqList faqList = new Gson().fromJson(object.toString(), FaqList.class);
                                    faqLists.add(faqList);
                                }

                                if (start == 0) {
                                    recycler_view.setAdapter(caretakerFAQAdapter);
                                    caretakerFAQAdapter.isShow = false;
                                }
                                start = start + 20;
                                caretakerFAQAdapter.notifyDataSetChanged();
                            }
                        } else {
                            mSwipeRefreshLayout.setRefreshing(false);
                        }

                    } catch (Throwable t) {
                        Log.e("My App", "Could not parse malformed JSON: \"" + response + "\"");
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    NetworkResponse networkResponse = error.networkResponse;
                    Constant.errorHandle(error, getActivity());
                    Constant.snackbar(mainLayout, networkResponse + "");
                    mSwipeRefreshLayout.setRefreshing(false);
                    error.printStackTrace();
                }
            }) {

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<String, String>();
                    headers.put("authToken", session.getAuthToken());
                    return headers;
                }

            };

            multipartRequest.setRetryPolicy(new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            VolleySingleton.getInstance(getContext()).addToRequestQueue(multipartRequest, TAG);
        } else {
            Constant.snackbar(mainLayout, getResources().getString(R.string.check_net_connection));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_for_addNew:
                layout_for_addDelete.setVisibility(View.GONE);
                iv_for_delete.setVisibility(View.GONE);
                caretakerFAQAdapter.isShow = false;
                caretakerFAQAdapter.notifyDataSetChanged();
                DailogView dailogView = new DailogView();
                dailogView.addNewFAQDailog(getContext(), this);
                break;
            case R.id.tv_for_faqDelete:
                iv_for_delete.setVisibility(View.VISIBLE);
                layout_for_addDelete.setVisibility(View.GONE);
                caretakerFAQAdapter.isShow = true;
                caretakerFAQAdapter.notifyDataSetChanged();
                break;
            case R.id.iv_for_delete:
                dailogView = new DailogView();
                dailogView.deletelFAQDailog(getContext(), this);
                break;
        }
    }

}
