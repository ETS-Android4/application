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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.gttime.android.component.Course;
import com.gttime.android.component.CourseSeat;
import com.gttime.android.component.Seat;
import com.gttime.android.net.HttpConnection;
import com.gttime.android.request.Request;
import com.gttime.android.ui.adapter.CourseListAdapter;
import com.gttime.android.R;
import com.gttime.android.util.MapArray;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CourseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CourseFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
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

    private String selectedUniversity;
    private String selectedTerm;
    private String selectedArea;

    private List<CourseSeat> courseSeats;
    private MapArray<String, String> semester;

    private int universityID;
    private int termID;
    private int areaID;
    private int subjectID;

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
    // TODO: Rename and change types and number of parameters
    public static CourseFragment newInstance(String param1, String param2) {
        CourseFragment fragment = new CourseFragment();
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
            this.universityID = savedInstanceState.getInt("universityID");
            this.subjectID = savedInstanceState.getInt("subjectID");
            this.termID = savedInstanceState.getInt("termID");
            this.areaID = savedInstanceState.getInt("areaID");
        }

        else {
            this.universityID = R.id.undergraduateID;
            this.subjectID = 0;
            this.termID = 0;
            this.areaID = 0;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_course, container, false);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("universityID", universityGroupID.getCheckedRadioButtonId());
        outState.putInt("subjectID", subjectSpinner.getSelectedItemPosition());
        outState.putInt("termID", termSpinner.getSelectedItemPosition());
        outState.putInt("areaID", areaSpinner.getSelectedItemPosition());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // TODO: Update info from database
        final ProgressDialog progress = new ProgressDialog(getActivity());
        progress.setMessage("Wait while loading...");
        progress.setTitle("Loading");
        progress.show();

        final AlertDialog alertDialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(CourseFragment.this.getActivity());
        alertDialog = builder.setMessage("Connection Error")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        System.exit(1);
                    }
                })
                .create();


        selectedUniversity = "";
        selectedTerm = "";
        selectedArea = "";

        universityGroupID = getView().findViewById(R.id.universityGroupID);
        termSpinner = getView().findViewById(R.id.semesterID);
        subjectSpinner = getView().findViewById(R.id.subjectID);
        areaSpinner = getView().findViewById(R.id.areaID);

        final ExecutorService service = Executors.newSingleThreadExecutor();

        universityGroupID.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                final RadioButton gradeID = getView().findViewById(checkedId);
                selectedUniversity = gradeID.getText().toString();
                progress.show();
                try {
                    Callable<String[]> task =  new Callable<String[]>() {
                        @Override
                        public String[] call() throws Exception {
                            return Request.queryTerm(selectedUniversity);
                        }
                    };
                    Future<String[]> future = service.submit(task);
                    termAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item, future.get());
                } catch (Exception e) {
                    alertDialog.show();
                }
                termSpinner.setAdapter(termAdapter);
                progress.dismiss();
            }
        });

        termSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                progress.show();

                selectedTerm = termSpinner.getSelectedItem().toString();

                try {
                    Callable<String[]> task = new Callable<String[]>() {
                        @Override
                        public String[] call() throws Exception {
                            return Request.queryMajor(selectedUniversity, selectedTerm);
                        }
                    };
                    Future<String[]> future = service.submit(task);
                    subjectAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item, future.get());
                } catch (Exception e) {
                    alertDialog.show();
                }
                subjectSpinner.setAdapter(subjectAdapter);
                progress.dismiss();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO: Show Error dialog
            }
        });

    subjectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            progress.show();

            selectedArea = subjectSpinner.getSelectedItem().toString();

            try {
                Callable<String[]> task = new Callable<String[]>() {
                    @Override
                    public String[] call() throws Exception {
                        return Request.queryArea(selectedUniversity, selectedTerm, selectedArea);
                    }
                };
                Future<String[]> future = service.submit(task);
                areaAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item, future.get());
            } catch (Exception e) {
                alertDialog.show();
            }
            areaSpinner.setAdapter(areaAdapter);
            progress.dismiss();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            // TODO: Show Error dialog
        }
    });

    semester = new MapArray<String, String>(getResources().getStringArray(R.array.semesterText), getResources().getStringArray(R.array.semesterID));
    courseListView = getView().findViewById(R.id.courseListID);
    courseSeats = new ArrayList<CourseSeat>();
    adapter = new CourseListAdapter(getContext(), courseSeats, this);
    courseListView.setAdapter(adapter);


    Button courseSearch = getView().findViewById(R.id.courseSearchButton);
    courseSearch.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            progress.show();
            new BackgroundTask().execute();
            progress.dismiss();
            }
        });

    progress.dismiss();
    }

    class BackgroundTask extends AsyncTask {
        String target;

        @Override
        protected void onPreExecute() {
            try {
                target = "http://ec2-3-238-0-205.compute-1.amazonaws.com/CourseList.php?courseUniversity="+ URLEncoder.encode(selectedUniversity,"UTF-8")
                        +"&courseTerm="+URLEncoder.encode(semester.get(termSpinner.getSelectedItem().toString()),"UTF-8")+"&courseMajor="+URLEncoder.encode(subjectSpinner.getSelectedItem().toString(),"UTF-8")
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
                courseSeats.clear();
                String result = (String) o;
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("response");

                int count = 0;

                String courseTerm;
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
                    courseTerm = object.getString("courseTerm");
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
                    courseSeats.add(new CourseSeat(course, seat));
                    count++;
                }

                if(count == 0) {
                    alertDialog.show();
                }
                adapter.setSemester(semester.get(termSpinner.getSelectedItem().toString()));
                adapter.notifyDataSetChanged();
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }

    }
}
