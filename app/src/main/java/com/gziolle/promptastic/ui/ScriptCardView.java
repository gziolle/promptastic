package com.gziolle.promptastic.ui;

import android.content.Context;
import android.util.AttributeSet;

import com.gziolle.promptastic.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

public class ScriptCardView extends CardView {
    public ScriptCardView(@NonNull Context context) {
        super(context);
    }

    public ScriptCardView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ScriptCardView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        if(isSelected()){
            this.setCardBackgroundColor(getContext().getResources().getColor(R.color.list_item_background_color_selected));
        } else{
            this.setCardBackgroundColor(getContext().getResources().getColor(R.color.list_item_background_color_normal));
        }
    }
}
