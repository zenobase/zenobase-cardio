package com.zenobase.cardio;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;
import com.google.common.primitives.Ints;

public class TimerView extends TextView {

	public TimerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setValue(0);
	}

    public void setValue(TimerValue value) {
		setValue(Ints.checkedCast(value.getMillis() / 1000));
	}

    private void setValue(int seconds) {
		setText(String.format("%02d:%02d", seconds / 60, seconds % 60));
	}
}
