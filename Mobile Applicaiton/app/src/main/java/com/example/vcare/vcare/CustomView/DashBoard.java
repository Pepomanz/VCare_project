package com.example.vcare.vcare.CustomView;

import android.content.Context;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vcare.vcare.R;

public class DashBoard extends FrameLayout {
    private TextView tvSpeedData,tvRpmData;
    private String speed,rpm;

    public DashBoard(@NonNull Context context) {
        super(context);
        setup(null);
    }

    public DashBoard(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setup(attrs);
    }

    public DashBoard(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setup(attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public DashBoard(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setup(attrs);
    }

    private void setup(AttributeSet attrs) {
        inflate(getContext(), R.layout.dashboard, this);
        tvSpeedData = (TextView)findViewById(R.id.speed_data);
        tvRpmData = (TextView)findViewById(R.id.rpm_data);
    }

    public void setSpeed(String speed) {
        this.speed = speed;
        tvSpeedData.setText(speed);
    }

    public void setRpm(String rpm) {
        this.rpm = rpm;
        tvRpmData.setText(rpm);
    }

    private static class SavedState extends BaseSavedState {
        String speed,rpm;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            this.speed = in.readString();
            this.rpm = in.readString();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeString(this.speed);
            out.writeString(this.rpm);
        }

        public static final Creator<DashBoard.SavedState> CREATOR = new Creator<DashBoard.SavedState>() {
            public DashBoard.SavedState createFromParcel(Parcel in) {
                return new DashBoard.SavedState(in);
            }

            public DashBoard.SavedState[] newArray(int size) {
                return new DashBoard.SavedState[size];
            }
        };
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        DashBoard.SavedState temp = new DashBoard.SavedState(superState);
        temp.rpm = rpm;
        temp.speed = speed;
        return temp;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof DashBoard.SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }
        DashBoard.SavedState temp = (DashBoard.SavedState) state;
        super.onRestoreInstanceState(temp.getSuperState());
        this.speed = temp.speed;
        this.rpm = temp.rpm;
        setSpeed(speed);
        setRpm(rpm);
    }
}
