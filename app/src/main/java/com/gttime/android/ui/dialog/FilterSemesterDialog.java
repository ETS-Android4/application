package com.gttime.android.ui.dialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.gttime.android.CallbackListener;
import com.gttime.android.R;
import com.gttime.android.component.Semester;
import com.gttime.android.mapping.KeyValPair;
import com.gttime.android.request.Request;
import com.gttime.android.ui.adapter.SemesterListAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.gttime.android.ui.fragment.StatisticsFragment;
import com.gttime.android.util.IntegerUtil;
import com.gttime.android.util.MapBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FilterSemesterDialog extends BottomSheetDialogFragment {
    public static final String TAG = "FilterSemeterDialog";
    private static final String SELECTED_TERM_KEY = "selectedID";

    public FilterSemesterDialog() {
        super();
    }

    public static FilterSemesterDialog newInstance() {
        return new FilterSemesterDialog();
    }

    public FilterSemesterDialog(int selectedID, CallbackListener callbackListener) {
        super();
        this.selectedID = selectedID;
        this.callbackListener = callbackListener;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) selectedID = getArguments().getInt(SELECTED_TERM_KEY);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_bottomsheet, container, false);
    }


    private ListView semesterView;
    private SemesterListAdapter semesterListAdapter;
    private CallbackListener callbackListener;

    private int selectedID;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Map<String, Integer> semester;
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

            semesterView = getView().findViewById(R.id.semesterID);
            semesterListAdapter = new SemesterListAdapter(getContext(), semester, selectedID);
            semesterListAdapter.setCallback(callbackListener);
            semesterView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            semesterView.setAdapter(semesterListAdapter);
            // TODO: Fix error
            semesterView.setSelection(selectedID);
        } catch (Exception e) {

        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SELECTED_TERM_KEY, semesterView.getSelectedItemPosition());
    }
}
