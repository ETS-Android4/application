package com.gttime.android.viewmodel.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.gttime.android.model.Course;
import com.gttime.android.R;
import com.gttime.android.util.CreditParser;
import com.gttime.android.view.fragment.StatisticsFragment;
import com.gttime.android.util.IOUtil;
import com.gttime.android.util.IntegerUtil;
import com.gttime.android.util.JSONUtil;

import java.io.File;
import java.util.List;

public class StatisticsCourseListAdapter extends BaseAdapter {
    private Context context;
    private List<Course> courseList;
    private Fragment parent;

    public StatisticsCourseListAdapter(Context context, List<Course> courseList, Fragment parent) {
        this.context = context;
        this.courseList = courseList;
        this.parent = parent;
    }

    @Override
    public int getCount() {
        return courseList.size();
    }

    @Override
    public Object getItem(int position) {
        return courseList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup viewgroup) {
        View v = View.inflate(context, R.layout.statistics, null);
        Button deleteButton = v.findViewById(R.id.deleteButton);
        TextView courseArea = v.findViewById(R.id.courseArea);
        TextView courseCRN = v.findViewById(R.id.courseCRN);
        TextView courseTitle = v.findViewById(R.id.courseStatisticTitle);
        TextView courseSection = v.findViewById(R.id.courseSection);
        TextView courseTime = v.findViewById(R.id.statisticTimeID);

        courseArea.setText(courseList.get(position).getCourseArea());
        courseCRN.setText(courseList.get(position).getCourseCRN());
        courseTitle.setText(courseList.get(position).getCourseTitle());
        courseSection.setText(courseList.get(position).getCourseSection());
        courseTime.setText(courseList.get(position).getCourseTime());

        v.setTag(courseList.get(position).getCourseCRN());

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try
                    {
                        String semesterID = Integer.toString(courseList.get(position).getCourseTerm());
                        boolean success = JSONUtil.deleteCourse(new File(parent.getActivity().getFilesDir(), IOUtil.getFileName(semesterID)), courseList.get(position).getCourseCRN());
                        if(success)
                        {
                            AlertDialog.Builder alert = new AlertDialog.Builder(parent.getActivity());
                            AlertDialog dialog = alert.setMessage("Course has been deleted from schedule")
                                    .setPositiveButton("OK",null)
                                    .create();
                            dialog.show();

                            String creditStr = courseList.get(position).getCourseCredit();

                            StatisticsFragment.totalCredit -= CreditParser.parse(creditStr);
                            StatisticsFragment.statCredit.setText(StatisticsFragment.totalCredit + " Credits");
                            courseList.remove(position);
                            notifyDataSetChanged();
                        }

                        else
                        {
                            AlertDialog.Builder alert = new AlertDialog.Builder(parent.getActivity());
                            AlertDialog dialog = alert.setMessage("Course has not been removed from your schedule")
                                    .setPositiveButton("OK",null)
                                    .create();
                            dialog.show();
                        }
                        return;
                    }
                    catch(Exception e)
                    {
                        e.printStackTrace();
                    }
                    };
                });
        return v;
    }
}

