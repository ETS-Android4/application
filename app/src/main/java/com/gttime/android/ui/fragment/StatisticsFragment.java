package com.gttime.android.ui.fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.gttime.android.CallbackListener;
import com.gttime.android.component.Course;
import com.gttime.android.mapping.KeyValPair;
import com.gttime.android.request.Request;
import com.gttime.android.ui.dialog.FilterSemesterDialog;
import com.gttime.android.R;
import com.gttime.android.ui.adapter.StatisticsCourseListAdapter;
import com.gttime.android.util.IOUtil;
import com.gttime.android.util.IntegerUtil;
import com.gttime.android.util.JSONUtil;
import com.gttime.android.util.MapBuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StatisticsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StatisticsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FilterSemesterDialog filterSemesterDialog;

    private ListView courseListView;
    private StatisticsCourseListAdapter adapter;
    private List<Course> courseList;

    public static int totalCredit = 0;
    public static TextView statCredit;
    public TextView semesterTextView;

    private LinearLayout filterSemesterButton;
    private Map<String, Integer> semester;

    private String selectedSemester;
    private int[] semesterVal;
    private String[] semesterText;

    public StatisticsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StatisticsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StatisticsFragment newInstance(String param1, String param2) {
        StatisticsFragment fragment = new StatisticsFragment();
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

        if(savedInstanceState != null) {
            this.selectedSemester = savedInstanceState.getString("selectedSemester");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_statistics, container, false);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("selectedSemester", semesterTextView.getText().toString());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final ProgressDialog progress = new ProgressDialog(getActivity());
        progress.setMessage("Wait while loading...");
        progress.setTitle("Loading");
        progress.show();

        final AlertDialog alertDialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(StatisticsFragment.this.getActivity());
        alertDialog = builder.setMessage("Connection Error")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        System.exit(1);
                    }
                })
                .create();

        progress.show();


        statCredit = getView().findViewById(R.id.totalCredit);
        semesterTextView = getView().findViewById(R.id.semesterText);
        semesterTextView.setText(selectedSemester);

        try {
            Callable<int[]> task =  new Callable<int[]>() {
                @Override
                public int[] call() throws Exception {
                    return Request.queryTerm();
                }
            };
            ExecutorService service = Executors.newSingleThreadExecutor();
            Future<int[]> future = service.submit(task);
            this.semesterVal = future.get();
            this.semesterText = KeyValPair.mapTerm(semesterVal);
            this.setSemester(semesterText[0]);
            semester = (new MapBuilder(semesterText, IntegerUtil.parseIntegerArr(semesterVal)).build());
        } catch (Exception e) {
            alertDialog.show();
        }

        filterSemesterButton = getView().findViewById(R.id.statisticFilter);
        filterSemesterDialog = new FilterSemesterDialog(new CallbackListener<String>() {
            @Override
            public void callback(String param) {
                setSemester(param);
                adapter.notifyDataSetChanged();
                new BackgroundTask().execute();
            }
        });

        courseListView = getView().findViewById(R.id.courseListView);
        courseList = new ArrayList<Course>();
        adapter = new StatisticsCourseListAdapter(getContext(), courseList,this);
        courseListView.setAdapter(adapter);

        filterSemesterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterSemesterDialog.show(getActivity().getSupportFragmentManager(), FilterSemesterDialog.TAG);
            }
        });


        new BackgroundTask().execute();
        totalCredit = 0;

        progress.dismiss();

    }

    // TODO: complete statistics fragment fetch by semester
    class BackgroundTask extends AsyncTask {
        String filename;
        @Override
        protected void onPreExecute() {
            try {
                String v = semesterTextView.getText().toString();
                filename = semester.get(semesterTextView.getText().toString()).toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        @Override
        protected String doInBackground(Object[] objects) {

            return JSONUtil.readJson(new File(getActivity().getFilesDir(), IOUtil.getFileName(filename)));
        }

        @Override
        protected void onProgressUpdate(Object[] values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Object o) {
            courseList.clear();
            totalCredit = 0;

            courseList.addAll(JSONUtil.fetchCourse((String) o));

            for(int i = 0; i < courseList.size(); i++) totalCredit += IntegerUtil.parseInt(courseList.get(i).getCourseCredit().split(" ")[0]);

            adapter.notifyDataSetChanged();
            statCredit.setText(totalCredit + " Credits");
        }
    }


    public void setSemester(String semester) {
        this.selectedSemester = semester;
        this.semesterTextView.setText(selectedSemester);
    }

}
