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

public class DetailCard extends FrameLayout{
    private TextView tvDetial,tvHeader;
    private ImageView imgIcon;
    private String header,detail;
    public DetailCard(@NonNull Context context) {
        super(context);
        setup(null);
    }

    public DetailCard(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setup(attrs);
    }

    public DetailCard(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setup(attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public DetailCard(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setup(attrs);
    }

    private void setup(AttributeSet attrs) {
        inflate(getContext(), R.layout.detail_card, this);
        imgIcon = (ImageView)findViewById(R.id.imgIcon);
        tvDetial = (TextView)findViewById(R.id.tvDetail);
        tvHeader = (TextView)findViewById(R.id.tvHeader);
    }

    public void setDetail(String detail) {
        this.detail = detail;
        tvDetial.setText(detail);
    }

    public void setHeader(String header) {
        this.header = header;
        tvHeader.setText(header);
    }

    public void setImage (int imgId) {
       imgIcon.setImageResource(imgId);
    }

    private static class SavedState extends BaseSavedState {
        String header;
        String detail;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            this.header = in.readString();
            this.detail = in.readString();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeString(this.header);
            out.writeString(this.detail);
        }

        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);
        ss.header = this.header ;
        ss.detail = this.detail;
        return ss;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }
        SavedState temp = (SavedState) state;
        super.onRestoreInstanceState(temp.getSuperState());
        this.header= temp.header;
        this.detail = temp.detail;
        setHeader(header);
        setDetail(detail);
    }
}
