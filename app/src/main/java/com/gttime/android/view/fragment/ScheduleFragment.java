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

import com.gttime.android.model.Course;
import com.gttime.android.model.CourseSchedule;
import com.gttime.android.R;
import com.gttime.android.mapping.KeyValPair;
import com.gttime.android.request.Request;
import com.gttime.android.util.IOUtil;
import com.gttime.android.util.IntegerUtil;
import com.gttime.android.util.JSONUtil;
import com.github.tlaabs.timetableview.Schedule;
import com.github.tlaabs.timetableview.TimetableView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.gttime.android.util.MapBuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ScheduleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScheduleFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private ChipGroup chipGroup;
    private ArrayList<Schedule> schedules;
    private TimetableView timeTable;

    private int chipID;
    private int timeTableStickerID;

    private Map<Integer, String> semester;
    private Map<Integer, String> timeTableCRN;

    private ProgressDialog progress;

    public ScheduleFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ScheduleFragment.
     */

    public static ScheduleFragment newInstance(String param1, String param2) {
        ScheduleFragment fragment = new ScheduleFragment();
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_calendar, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progress = new ProgressDialog(getActivity(), ProgressDialog.STYLE_SPINNER);
        progress.setMessage("Wait while loading...");
        progress.setTitle("Loading");
        timeTable = getView().findViewById(R.id.timetable);
        chipGroup = getView().findViewById(R.id.semesterGroup);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        schedules = new ArrayList<Schedule>();
        Map<Integer, String> semester;
        String[] semesterText;
        int[] semesterVal;

        try {
            Callable<int[]> task =  new Callable<int[]>() {
                @Override
                public int[] call() throws Exception {
                    return Request.queryTerm();
                }
            };
            ExecutorService service = Executors.newSingleThreadExecutor();
            Future<int[]> future = service.submit(task);
            semesterVal = future.get();
            semesterText = KeyValPair.mapTerm(semesterVal);

            semester = (new MapBuilder(IntegerUtil.parseIntegerArr(semesterVal), semesterText).build());

            for (int i=0; i<semesterVal.length; i++) {
                Chip chip = (Chip) getLayoutInflater().inflate(R.layout.chip_choice, chipGroup, false);
                chip.setId(semesterVal[i]);
                chip.setText(semester.get(semesterVal[i]));
                chipGroup.addView(chip);
            }

            chipID = chipID==0? semesterVal[0]:chipID;

        } catch (Exception e) {

        }

        chipGroup.setSingleSelection(true);
        chipGroup.check(chipID);
        chipGroup.setSelectionRequired(true);
        chipGroup.setChipSpacing(5);
        chipGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup group, int checkedId) {
                chipID = chipGroup.getCheckedChipId();
                timeTable.removeAll();
                new BackgroundTask().execute();
            }
        });


        timeTableStickerID =  0;
        timeTableCRN = new HashMap<Integer, String>();

        new BackgroundTask().execute();

        timeTable.setOnStickerSelectEventListener(new TimetableView.OnStickerSelectedListener() {
            @Override
            public void OnStickerSelected(final int idx, ArrayList<Schedule> schedulesParam) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ScheduleFragment.this.getActivity());
                AlertDialog alertDialog = builder.setMessage("Delete from schedule?")
                        .setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String targetCRN = timeTableCRN.get(idx);
                                JSONUtil.deleteCourse(new File(getActivity().getFilesDir(), IOUtil.getFileName(Integer.toString(chipID))), targetCRN);
                                timeTable.remove(idx);
                            }
                        })
                        .show();
            }
        });
    }


    class BackgroundTask extends AsyncTask {
        String filename;
        @Override
        protected void onPreExecute() {
            try {
                filename = IOUtil.getFileName(String.valueOf(chipGroup.getCheckedChipId()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        @Override
        protected String doInBackground(Object[] objects) {
            return JSONUtil.readJson(new File(getActivity().getFilesDir(), filename));
        }

        @Override
        protected void onProgressUpdate(Object[] values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Object o) {
            List<Course> registeredCourses = JSONUtil.fetchCourse((String) o);

            timeTableCRN.clear();
            for(int i = 0; i < registeredCourses.size(); i++) {
                schedules.clear();
                int days = registeredCourses.get(i).getCourseDay().length();

                // XXX: ERROR with timetable duplicate adding course to sticker
                String courseInstructor = registeredCourses.get(i).getCourseInstructor();
                String courseTitle = registeredCourses.get(i).getCourseTitle();
                String courseCRN = registeredCourses.get(i).getCourseCRN();
                String courseLocation = registeredCourses.get(i).getCourseLocation();
                String courseDay = registeredCourses.get(i).getCourseDay();
                String courseTime = registeredCourses.get(i).getCourseTime();
                for(int j = 0; j < days; j++) {
                    CourseSchedule cs = new CourseSchedule(courseTitle, courseInstructor, courseLocation, courseTime, courseDay.charAt(j));
                    schedules.add(cs);
                }

                if(!schedules.isEmpty()) {
                    timeTable.add(schedules);
                    timeTableCRN.put(timeTableStickerID, courseCRN);
                    timeTableStickerID++;
                }

            }
        }
    }

    public boolean alreadyIn(List<String> courseIDList, String item) {
        for(int i = 0; i < courseIDList.size(); i++) {
            if(courseIDList.get(i) == item) {
                return false;
            }
        }
        return true;
    }
}
