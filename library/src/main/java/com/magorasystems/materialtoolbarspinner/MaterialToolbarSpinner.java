package com.magorasystems.materialtoolbarspinner;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatSpinner;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;


/**
 * Created at Magora Systems (http://magora-systems.com) on 20.07.16
 *
 * @author Stanislav S. Borzenko
 */
public class MaterialToolbarSpinner extends LinearLayout {
    private Spinner spinner;

    public MaterialToolbarSpinner(Context context) {
        super(context);
        init();
    }

    public MaterialToolbarSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setOrientation(HORIZONTAL);

        spinner = new AppCompatSpinner(getContext(), null,
                R.attr.toolbarSpinnerStyle);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int dropDownVerticalOffset = getResources().getDimensionPixelSize(
                    R.dimen.popup_dropdown_v_offset_ge_21);
            spinner.setDropDownVerticalOffset(dropDownVerticalOffset);
        }

        addView(spinner, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
    }

    public void setAdapter(Adapter adapter) {
        spinner.setAdapter(adapter);
    }

    public void setOnItemSelectedListener(
            @Nullable AdapterView.OnItemSelectedListener listener) {
        spinner.setOnItemSelectedListener(listener);
    }

    public static abstract class Adapter extends BaseAdapter {
        public abstract View getToolbarView(
                int position, View convertView, ViewGroup parent);

        public abstract View getDownView(
                int position, View convertView, ViewGroup parent);

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            if (convertView == null) {
                convertView = getToolbarView(position, null, parent);

                LinearLayout itemContainer = (LinearLayout) LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.item_wrapper_spinner_toolbar,
                                parent, false);
                itemContainer.addView(convertView, 0,
                        new LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.MATCH_PARENT));

                // TODO: Understand logic. It doesn't work properly.
                int itemWidth = convertView.getContext()
                        .getResources().getDimensionPixelSize(
                        R.dimen.item_toolbar_width);
                setViewWidth(itemContainer, itemWidth);

                view = itemContainer;
            } else {
                View realConvertView
                        = ((LinearLayout) convertView).getChildAt(0);
                getToolbarView(position, realConvertView, parent);
                view = convertView;
            }

            return view;
        }

        @Override
        public View getDropDownView(int position,
                                    View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getDownView(position, null, parent);

                // TODO: Understand logic. It doesn't work properly.
                int itemWidth = convertView.getContext()
                        .getResources().getDimensionPixelSize(
                        R.dimen.item_toolbar_width);
                setViewWidth(convertView, itemWidth);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    convertView.setBackgroundResource(
                            getSelectableItemBackground(
                                    convertView.getContext()));
                }
            } else {
                convertView = getDownView(position, convertView, parent);
            }

            return convertView;
        }
    }

    @DrawableRes
    public static int getSelectableItemBackground(Context context) {
        int[] attrs = new int[]{R.attr.selectableItemBackground};
        TypedArray typedArray = context.obtainStyledAttributes(attrs);
        int backgroundResource = typedArray.getResourceId(0, 0);
        typedArray.recycle();

        return backgroundResource;
    }

    public static void setViewWidth(View view, int width) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.width = width;
        view.setLayoutParams(layoutParams);
    }
}
