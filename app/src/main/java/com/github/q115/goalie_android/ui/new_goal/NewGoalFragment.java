package com.github.q115.goalie_android.ui.new_goal;

import static android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.github.q115.goalie_android.Constants;
import com.github.q115.goalie_android.R;
import com.github.q115.goalie_android.services.AlarmService;
import com.github.q115.goalie_android.ui.DelayedProgressDialog;
import com.github.q115.goalie_android.ui.friends.AddContactDialog;

import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

/*
 * Copyright 2017 Qi Li
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

public class NewGoalFragment extends Fragment implements NewGoalFragmentView, AddContactDialog.AddContactOnAddedListener {
    private DelayedProgressDialog mProgressDialog;
    private NewGoalFragmentPresenter mNewGoalPresenter;
    private String mTitle;

    private EditText mGoalTitle;
    private TextView mGoalEnd;
    private TextView mGoalWager;
    private TextView mGoalWagerPercentage;
    private EditText mGoalEncouragement;
    private Spinner mGoalRefereeSpinner;
    private Spinner mAlarmReminderSpinner;
    private Switch isGoalPublicFeed;

    public NewGoalFragment() {
    }

    public static NewGoalFragment newInstance(String title) {
        NewGoalFragment fm = new NewGoalFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        fm.setArguments(bundle);
        return fm;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTitle = getArguments().getString("title");
    }

    @Override
    @SuppressWarnings("unchecked")
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_new_goal, container, false);

        // title
        mGoalTitle = rootView.findViewById(R.id.goal_title);
        if (mTitle != null && mGoalTitle.getText().length() == 0)
            mGoalTitle.setText(mTitle);

        // end time
        mGoalEnd = rootView.findViewById(R.id.goal_end_text);
        View.OnClickListener showPicker = mNewGoalPresenter.getTimePickerClickedListener();
        rootView.findViewById(R.id.goal_end_btn).setOnClickListener(showPicker);

        // wager
        mGoalWager = rootView.findViewById(R.id.goal_wager);
        mGoalWagerPercentage = rootView.findViewById(R.id.goal_wager_middle);
        View.OnClickListener wagerClicked = mNewGoalPresenter.getWagerClickedListener();
        rootView.findViewById(R.id.goal_wager_minus).setOnClickListener(wagerClicked);
        rootView.findViewById(R.id.goal_wager_plus).setOnClickListener(wagerClicked);

        // Referee
        mGoalRefereeSpinner = rootView.findViewById(R.id.goal_referee_spinner);
        mGoalRefereeSpinner.setAdapter(new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1, mNewGoalPresenter.getRefereeArray(getActivity())));
        mGoalRefereeSpinner.setOnItemSelectedListener(mNewGoalPresenter.getRefereeSelectionChangedListener());

        // alarm
        mAlarmReminderSpinner = rootView.findViewById(R.id.goal_reminder_alarm);
        mAlarmReminderSpinner.setAdapter(new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1, mNewGoalPresenter.getAlarmReminderArray()));

        // is public
        isGoalPublicFeed = rootView.findViewById(R.id.is_public_goal);

        // encouragement
        mGoalEncouragement = rootView.findViewById(R.id.goal_encouragement);

        // set goal
        rootView.findViewById(R.id.set_goal).setOnClickListener(view -> {
            String referee = (String) mGoalRefereeSpinner.getSelectedItem();
            long alarmMillisecondBeforeEnd = mNewGoalPresenter.getAlarmMillisecondBeforeEndDate(mAlarmReminderSpinner.getSelectedItemPosition());
            mNewGoalPresenter.setGoal(getActivity(), mGoalTitle.getText().toString().trim(),
                    mGoalEncouragement.getText().toString().trim(), referee, alarmMillisecondBeforeEnd, isGoalPublicFeed.isChecked());
        });

        mProgressDialog = new DelayedProgressDialog();
        mProgressDialog.setCancelable(false);

        if (savedInstanceState != null) {
            mNewGoalPresenter.restore((HashMap<String, String>) savedInstanceState.getSerializable("presenter"));
        }

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mNewGoalPresenter.start();

        if (mGoalTitle != null) {
            if (mGoalTitle.getText().length() == 0) {
                mGoalTitle.requestFocus();
            } else {
                mGoalTitle.clearFocus();
            }
        }
    }

    @Override
    public void setPresenter(NewGoalFragmentPresenter presenter) {
        mNewGoalPresenter = presenter;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // Edittext values are saved by default, so no need to save title & others, just timer etc.
        outState.putSerializable("presenter", mNewGoalPresenter.getSaveHash());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void showTimePicker(long endEpoch) {
        Bundle bundle = new Bundle();
        bundle.putLong("endEpoch", endEpoch);

        DateTimePickerDialog.DateTimePickerDialogCallback onTimePickedCallback = mNewGoalPresenter.getTimePickerCallbackListener();
        DateTimePickerDialog pickerFrag = new DateTimePickerDialog();
        pickerFrag.mListener = onTimePickedCallback;
        pickerFrag.setArguments(bundle);
        pickerFrag.show(getActivity().getSupportFragmentManager(), "DateTimePickerDialog");
    }

    @Override
    public void updateTime(String date) {
        mGoalEnd.setText(date);
    }

    @Override
    public void updateWager(long wagering, long total, int percent) {
        mGoalWager.setText(String.format(getString(R.string.wagered), wagering, total));
        mGoalWagerPercentage.setText(String.format(getString(R.string.percent), percent));
    }

    @Override
    public void updateRefereeOnSpinner(int position) {
        mGoalRefereeSpinner.setSelection(position);
    }

    @Override
    public void showNewUsernameDialog() {
        AddContactDialog addContactDialog = new AddContactDialog();
        addContactDialog.setOnAdded(this);
        addContactDialog.show(getActivity().getSupportFragmentManager(), "AddContactDialog");
    }

    @Override
    public void onAdded(String username) {
        mGoalRefereeSpinner.setAdapter(new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1, mNewGoalPresenter.getRefereeArray(getActivity())));

        for (int i = 0; i < mGoalRefereeSpinner.getAdapter().getCount(); i++) {
            if (mGoalRefereeSpinner.getAdapter().getItem(i).equals(username)) {
                mGoalRefereeSpinner.setSelection(i);
                break;
            }
        }
    }

    @Override
    public void resetReferee(boolean isFromSpinner) {
        mGoalRefereeSpinner.setSelection(0);
    }

    @Override
    public void onSetGoal(boolean isSuccessful, String errMsg) {
        if (isSuccessful) {
            Toast.makeText(getActivity(), getString(R.string.ok_goal), Toast.LENGTH_SHORT).show();
            getActivity().setResult(Activity.RESULT_OK);
            getActivity().finish();
        } else {
            AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
            alertDialog.setTitle(getString(R.string.error_goal));
            alertDialog.setMessage(errMsg);
            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.ok), (DialogInterface.OnClickListener) null);
            alertDialog.show();
        }
    }

    @Override
    public void updateProgress(boolean shouldShow) {
        if (shouldShow) {
            mProgressDialog.show(getActivity().getSupportFragmentManager(), "DelayedProgressDialog");
        } else {
            mProgressDialog.cancel();
        }
    }

    @Override
    public void setAlarmTime(long epoch, String guid) {
        Intent intent = AlarmService.newIntent(getActivity(), guid);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), (guid + "_alarm").hashCode(), intent,
                PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmMgr = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        alarmMgr.setExact(AlarmManager.RTC_WAKEUP, epoch, pendingIntent);
    }

    @Override
    public boolean isAlarmPermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            int permission2 = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.POST_NOTIFICATIONS);
            if (permission2 != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS}, Constants.REQUEST_PERMISSIONS_ALARM);
                return false;
            }

            AlarmManager alarmMgr = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
            if (!alarmMgr.canScheduleExactAlarms()) {
                Toast.makeText(getActivity(), getString(R.string.no_permission_alarm), Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ACTION_REQUEST_SCHEDULE_EXACT_ALARM));
                return false;
            }
        }

        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case Constants.REQUEST_PERMISSIONS_ALARM:
                boolean isAllPermissionsGranted = true;
                for (int grantResult : grantResults) {
                    isAllPermissionsGranted &= grantResult == PackageManager.PERMISSION_GRANTED;
                }

                if (!isAllPermissionsGranted)
                    Toast.makeText(getActivity(), getString(R.string.no_permission_alarm), Toast.LENGTH_SHORT).show();
            default:
                break;
        }
    }
}
