package com.codepath.nytquest.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.codepath.nytquest.R;
import com.codepath.nytquest.models.Filter;
import com.codepath.nytquest.utils.DateHelper;

import org.parceler.Parcels;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by rpraveen on 10/23/16.
 */
public class FilterFragment extends DialogFragment implements DatePickerFragment.OnDateSetListener{

  @BindView(R.id.etBeginDate) EditText etBeginDate;
  @BindView(R.id.etEndDate) EditText etEndDate;
  @BindView(R.id.spSortBy) Spinner spSortBy;
  @BindView(R.id.cbSports) CheckBox cbSports;
  @BindView(R.id.cbPolitics) CheckBox cbPolitics;
  @BindView(R.id.cbUS) CheckBox cbUS;
  @BindView(R.id.cbWorld) CheckBox cbWorld;
  @BindView(R.id.btCancel) Button btCancel;
  @BindView(R.id.btFilter) Button btFilter;

  public static final String FILTER_PARAMS = "filter_params";

  public interface OnSearchFilterListener {
    public void onFilter();
  }

  private Filter mFilter;

  public FilterFragment() {
  }

  public static FilterFragment newInstance(Filter filter) {
    FilterFragment fragment = new FilterFragment();
    Bundle args = new Bundle();
    args.putParcelable(FILTER_PARAMS, Parcels.wrap(filter));
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.query_filter_fragment, container);
    return view;
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstance) {

    super.onViewCreated(view, savedInstance);
    mFilter = Parcels.unwrap(getArguments().getParcelable(FILTER_PARAMS));
    getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    ButterKnife.bind(this, view);
    final Calendar calendar = Calendar.getInstance();
    final int year = calendar.get(Calendar.YEAR);
    final int month = calendar.get(Calendar.MONTH);
    final int day = calendar.get(Calendar.DAY_OF_MONTH);
    etBeginDate.setText(mFilter.beginDate);
    etBeginDate.setOnClickListener(
      new View.OnClickListener() {
        @Override
        public void onClick(View view) {

          DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
          new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(
            DatePicker datePicker, int year, int month, int day) {
              etBeginDate.setText(DateHelper.getFormatteDate(
              year, month, day)
              );
            }
          }, year, month, day);
          datePickerDialog.show();
        }
      }
    );
    etEndDate.setText(mFilter.endDate);
    etEndDate.setOnClickListener(
      new View.OnClickListener() {
        @Override
        public void onClick(View view) {

          DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
          new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(
            DatePicker datePicker, int year, int month, int day) {
              etEndDate.setText(DateHelper.getFormatteDate(
              year, month, day)
              );
            }
          }, year, month, day);
          datePickerDialog.show();
        }
      }
    );
    if (mFilter.sortByNewest) {
      spSortBy.setSelection(0);
    } else {
      spSortBy.setSelection(1);
    }
    for (String news_desk : mFilter.newsDesk) {
      if (news_desk.equals("Sports")) {
        cbSports.setChecked(true);
      } else if (news_desk.equals("U.S")) {
        cbUS.setChecked(true);
      } else if (news_desk.equals("Politics")) {
        cbPolitics.setChecked(true);
      } else if (news_desk.equals("World")) {
        cbWorld.setChecked(true);
      }
    }
    btCancel.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        dismiss();
      }
    });
    btFilter.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        mFilter.beginDate = etBeginDate.getText().toString();
        mFilter.endDate = etEndDate.getText().toString();
        if (spSortBy.getSelectedItemPosition() == 0) {
          mFilter.sortByNewest = true;
        } else {
          mFilter.sortByNewest = false;
        }
        mFilter.newsDesk.clear();
        if (cbWorld.isChecked()) {
          mFilter.newsDesk.add("World");
        }
        if (cbSports.isChecked()) {
          mFilter.newsDesk.add("Sports");
        }
        if (cbUS.isChecked()) {
          mFilter.newsDesk.add("U.S");
        }
        if (cbPolitics.isChecked()) {
          mFilter.newsDesk.add("Politics");
        }
        OnSearchFilterListener listener = (OnSearchFilterListener) getActivity();
        listener.onFilter();
        dismiss();
      }
    });
  }

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    Dialog dialog = super.onCreateDialog(savedInstanceState);
    // request a window without the title
    dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
    return dialog;
  }

  @Override
  public void onResume() {
    // Get existing layout params for the window
    ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
    // Assign window properties to fill the parent
    params.width = WindowManager.LayoutParams.MATCH_PARENT;
    params.height = WindowManager.LayoutParams.MATCH_PARENT;
    getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
    // Call super onResume after sizing
    super.onResume();
  }

  public void onDateSet(DatePicker picker, int year, int month, int day) {

  }
}
