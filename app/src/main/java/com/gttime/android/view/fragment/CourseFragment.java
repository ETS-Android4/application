package com.gttime.android.view.fragment;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.gttime.android.model.Course;
import com.gttime.android.model.CourseInfo;
import com.gttime.android.model.Seat;
import com.gttime.android.mapping.KeyValPair;
import com.gttime.android.net.HttpConnection;
import com.gttime.android.request.Request;
import com.gttime.android.viewmodel.adapter.CourseListAdapter;
import com.gttime.android.R;
import com.gttime.android.util.IntegerUtil;
import com.gttime.android.util.MapBuilder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CourseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CourseFragment extends Fragment {
    // TODO: Figure out what these params mean
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;


    private RadioGroup universityGroupID;
    private ArrayAdapter termAdapter;
    private Spinner termSpinner;
    private ArrayAdapter areaAdapter;
    private Spinner areaSpinner;
    private ArrayAdapter subjectAdapter;
    private Spinner subjectSpinner;
    private ListView courseListView;
    private CourseListAdapter adapter;

    private List<CourseInfo> courseInfos;
    private Map<String, Integer> semester;

    private int[] semesterVal;
    private String[] semesterText;
    private String[] subjectVal;
    private String[] areaVal;

    // container variables for database query
    private String selectedUniversity;
    private String selectedTermID;
    private String selectedArea;

    // container variables for id store
    private int universityID;
    private int termID;
    private int areaID;
    private int subjectID;

    private ProgressDialog progress;
    private AlertDialog alertDialog;

    public CourseFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CourseFragment.
     */
    public static CourseFragment newInstance(String param1, String param2) {
        CourseFragment fragment = new CourseFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    // TODO: Use savedInstanceState to load configs
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
        return inflater.inflate(R.layout.fragment_course, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        universityGroupID = getView().findViewById(R.id.universityGroupID);
        termSpinner = getView().findViewById(R.id.semesterID);
        subjectSpinner = getView().findViewById(R.id.subjectID);
        areaSpinner = getView().findViewById(R.id.areaID);
        courseListView = getView().findViewById(R.id.courseListID);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        progress = new ProgressDialog(getActivity(), ProgressDialog.STYLE_SPINNER);
        progress.setTitle("Loading");
        progress.setMessage("Please wait while loading");
        progress.setCancelable(true);
        progress.create();
        progress.show();

        AlertDialog.Builder builder = new AlertDialog.Builder(CourseFragment.this.getActivity());
        alertDialog = builder.setMessage("Connection Error")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        System.exit(1);
                    }
                })
                .create();

        universityGroupID.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                progress.show();
                RadioButton gradeID = group.findViewById(checkedId);
                gradeID.setChecked(true);
                selectedUniversity = gradeID.getText().toString();
                universityID = checkedId;

                try {
                    // TODO: Figure out way to call Request.ExecuteQuery with primitives
                    Callable<int[]> task =  new Callable<int[]>() {
                        @Override
                        public int[] call() throws Exception {
                            return Request.queryTerm();
                        }
                    };
                    ExecutorService service = Executors.newSingleThreadExecutor();
                    Future<int[]> future = service.submit(task);
                    semesterVal = future.get();

                } catch (Exception e) {
                    alertDialog.show();
                }

                semesterText = KeyValPair.mapTerm(semesterVal);
                termAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item, semesterText);
                termSpinner.setAdapter(termAdapter);

                if(termID >= semesterText.length) {
                    termID = 0;
                }

                termSpinner.setSelection(termID);

                progress.dismiss();
            }
        });

        termSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                progress.show();
                String selectedTerm = termSpinner.getSelectedItem().toString();
                selectedTermID = String.valueOf(semester.get(selectedTerm));
                termID = termSpinner.getSelectedItemPosition();

                try {
                    subjectVal = Request.ExecuteQuery(new Callable<String[]>() {
                        @Override
                        public String[] call() throws Exception {
                            return Request.queryMajor(selectedUniversity, selectedTermID);
                        }
                    });
                } catch (Exception e) {
                    alertDialog.show();
                }

                subjectAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item, subjectVal);
                subjectSpinner.setAdapter(subjectAdapter);

                if(subjectID >= subjectVal.length) {
                    subjectID = 0;
                }

                subjectSpinner.setSelection(subjectID);

                progress.dismiss();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        subjectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                progress.show();
                selectedArea = subjectSpinner.getSelectedItem().toString();
                subjectID = subjectSpinner.getSelectedItemPosition();

                try {
                    areaVal = Request.ExecuteQuery(new Callable<String[]>() {
                        @Override
                        public String[] call() throws Exception {
                            return Request.queryArea(selectedUniversity, selectedTermID, selectedArea);
                        }
                    });
                } catch (Exception e) {
                    alertDialog.show();
                }

                areaAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item, areaVal);
                areaSpinner.setAdapter(areaAdapter);

                if(areaID >= areaVal.length) {
                    areaID = 0;
                }

                areaSpinner.setSelection(areaID);

                progress.dismiss();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        areaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                areaID = areaSpinner.getSelectedItemPosition();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Callable<int[]> task = new Callable<int[]>() {
            @Override
            public int[] call() throws Exception {
                return Request.queryTerm();
            }
        };
        ExecutorService service = Executors.newSingleThreadExecutor();
        Future<int[]> future = service.submit(task);

        try {
            semesterVal = future.get();
        } catch (Exception e) {
            alertDialog.show();
        }

        semesterText = KeyValPair.mapTerm(semesterVal);
        semester = (new MapBuilder(semesterText, IntegerUtil.parseIntegerArr(semesterVal)).build());
        courseInfos = courseInfos == null? new ArrayList<CourseInfo>():courseInfos;
        adapter = new CourseListAdapter(getContext(), courseInfos, this);
        courseListView.setAdapter(adapter);

        // load stored state
        if(universityID != 0) {
            //universityGroupID.check(universityID);
            ((RadioButton) universityGroupID.findViewById(universityID)).setChecked(true);
            termSpinner.setSelection(termID);
            subjectSpinner.setSelection(subjectID);
            areaSpinner.setSelection(areaID);
        }

        Button courseSearch = getView().findViewById(R.id.courseSearchButton);
        courseSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new BackgroundTask().execute();
                }
            });

        progress.dismiss();
    }

    class BackgroundTask extends AsyncTask {
        String target;

        @Override
        protected void onPreExecute() {
            progress.show();
            courseInfos.clear();
            try {
                target = "http://ec2-3-238-0-205.compute-1.amazonaws.com/CourseList.php?courseUniversity="+ URLEncoder.encode(selectedUniversity,"UTF-8")
                        +"&courseTerm="+URLEncoder.encode(String.valueOf(semester.get(termSpinner.getSelectedItem())),"UTF-8")+"&courseMajor="+URLEncoder.encode(subjectSpinner.getSelectedItem().toString(),"UTF-8")
                        +"&courseArea="+URLEncoder.encode(areaSpinner.getSelectedItem().toString(),"UTF-8");
            }

            catch(Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            return HttpConnection.read(target);
        }

        @Override
        protected void onProgressUpdate(Object[] values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Object o) {
            AlertDialog alertDialog;
            AlertDialog.Builder builder = new AlertDialog.Builder(CourseFragment.this.getActivity());
            alertDialog = builder.setMessage("Lecture not found")
                    .setPositiveButton("OK",null)
                    .create();

            try {
                String result = (String) o;
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("response");

                int count = 0;

                int courseTerm;
                String courseMajor;
                String courseTitle;
                String courseCRN;
                String courseArea;
                String courseSection;
                String courseClass;
                String courseTime;
                String courseDay;
                String courseLocation;
                String courseInstructor;
                String courseUniversity;
                String courseCredit;
                String courseAttribute;

                int seatCapacity;
                int seatActual;
                int seatRemaining;
                int waitlistCapacity;
                int waitlistActual;
                int waitlistRemaining;

                while(count < jsonArray.length()) {
                    JSONObject object = jsonArray.getJSONObject(count);
                    courseTerm = object.getInt("courseTerm");
                    courseMajor = object.getString("courseMajor");
                    courseTitle = object.getString("courseTitle");
                    courseCRN = object.getString("courseCRN");
                    courseArea = object.getString("courseArea");
                    courseSection = object.getString("courseSection");
                    courseClass = object.getString("courseClass");
                    courseTime = object.getString("courseTime");
                    courseDay = object.getString("courseDay");
                    courseLocation = object.getString("courseLocation");
                    courseInstructor = object.getString("courseInstructor");
                    courseUniversity = object.getString("courseUniversity");
                    courseCredit = object.getString("courseCredit");
                    courseAttribute = object.getString("courseAttribute");

                    seatCapacity = object.getInt("seatCapacity");
                    seatActual = object.getInt("seatActual");
                    seatRemaining = object.getInt("seatRemaining");
                    waitlistCapacity = object.getInt("waitlistCapacity");
                    waitlistActual = object.getInt("waitlistActual");
                    waitlistRemaining = object.getInt("waitlistRemaining");

                    Course course = new Course(courseTerm, courseDay, courseMajor, courseTitle, courseCRN, courseArea, courseSection, courseClass, courseTime, courseLocation, courseInstructor, courseUniversity, courseCredit, courseAttribute);
                    Seat seat = new Seat(seatCapacity, seatActual, seatRemaining, waitlistCapacity, waitlistActual, waitlistRemaining);
                    courseInfos.add(new CourseInfo(course, seat));
                    count++;
                }

                if(count == 0) {
                    alertDialog.show();
                }
                adapter.setSemester(String.valueOf(semester.get(termSpinner.getSelectedItem())));
                adapter.notifyDataSetChanged();
            }
            catch(Exception e) {
                e.printStackTrace();
            }

            progress.dismiss();
        }

    }
}
