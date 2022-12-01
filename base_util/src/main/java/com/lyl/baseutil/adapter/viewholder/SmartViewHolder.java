package com.lyl.baseutil.adapter.viewholder;


import android.content.res.Resources;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;


public class SmartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private AdapterView.OnItemClickListener mListener;
    private int mPosition = -1;

    public SmartViewHolder(View itemView) {
        super(itemView);
        mListener = null;
    }

    public SmartViewHolder(View itemView, AdapterView.OnItemClickListener mListener) {
        super(itemView);
        this.mListener = mListener;
        itemView.setOnClickListener(this);

        /*
         * 设置水波纹背景
         */
        if (itemView.getBackground() == null) {
            TypedValue typedValue = new TypedValue();
            Resources.Theme theme = itemView.getContext().getTheme();
            int top = itemView.getPaddingTop();
            int bottom = itemView.getPaddingBottom();
            int left = itemView.getPaddingLeft();
            int right = itemView.getPaddingRight();
            if (theme.resolveAttribute(android.R.attr.selectableItemBackground, typedValue, true)) {
                itemView.setBackgroundResource(typedValue.resourceId);
            }
            itemView.setPadding(left, top, right, bottom);
        }
    }

    public void setPosition(int position) {
        mPosition = position;
    }

    @Override
    public void onClick(View v) {
        if (mListener != null) {
            int position = getAdapterPosition();
            if (position >= 0) {
                mListener.onItemClick(null, v, position, getItemId());
            } else if (mPosition > -1) {
                mListener.onItemClick(null, v, mPosition, getItemId());
            }
        }
    }

    private View findViewById(int id) {
        return id == 0 ? itemView : itemView.findViewById(id);
    }

    public SmartViewHolder text(int id, CharSequence sequence) {
        View view = findViewById(id);
        if (view instanceof TextView) {
            if (sequence == null) {
                sequence = "";
            }
            ((TextView) view).setText(sequence);
        }
        return this;
    }

    public SmartViewHolder text(int id, @StringRes int stringRes) {
        View view = findViewById(id);
        if (view != null) {
            if (view instanceof TextView) {
                ((TextView) view).setText(stringRes);
            }
        }
        return this;
    }

    public SmartViewHolder textColorId(int id, int colorId) {
        View view = findViewById(id);
        if (view instanceof TextView) {
            ((TextView) view).setTextColor(ContextCompat.getColor(view.getContext(), colorId));
        }
        return this;
    }

    public SmartViewHolder textColor(int id, int colorId) {
        View view = findViewById(id);
        if (view instanceof TextView) {
            ((TextView) view).setTextColor(colorId);
        }
        return this;
    }

    public SmartViewHolder image(int id, int imageId) {
        View view = findViewById(id);
        if (view instanceof ImageView) {
            ((ImageView) view).setImageResource(imageId);
        }
        return this;
    }

    public SmartViewHolder gone(int id) {
        View view = findViewById(id);
        if (view != null) {
            view.setVisibility(View.GONE);
        }
        return this;
    }

    public SmartViewHolder goneArr(int... ids) {
        for (int id : ids) {
            gone(id);
        }
        return this;
    }

    public SmartViewHolder visibleArr(int... ids) {
        for (int id : ids) {
            visible(id);
        }
        return this;
    }

    public SmartViewHolder visible(int id) {
        View view = findViewById(id);
        if (view != null) {
            view.setVisibility(View.VISIBLE);
        }
        return this;
    }

    public SmartViewHolder setBackgroundColor(int id, int color) {
        View view = findViewById(id);
        if (view != null) {
            view.setBackgroundColor(view.getContext().getResources().getColor(color));
        }
        return this;
    }

    public SmartViewHolder setBackgroundResource(int id, int brId) {
        View view = findViewById(id);
        if (view != null) {
            view.setBackgroundResource(brId);
        }
        return this;
    }

    public SmartViewHolder unable(int id) {
        View view = findViewById(id);
        if (view != null) {
            view.setEnabled(false);
        }
        return this;
    }

    public SmartViewHolder enable(int id) {
        View view = findViewById(id);
        if (view != null) {
            view.setEnabled(true);
        }
        return this;
    }

    public SmartViewHolder addTextWatcher(TextWatcher textWatcher, int... ids) {
        for (int id : ids) {
            View view = findViewById(id);
            if (view != null) {
                if (view instanceof EditText) {
                    ((EditText) view).addTextChangedListener(textWatcher);
                }
            }
        }
        return this;
    }

    public SmartViewHolder setOnFocusChangeListener(View.OnFocusChangeListener onFocusChangeListener, int... ids) {
        for (int id : ids) {
            View view = findViewById(id);
            if (view instanceof EditText) {
                view.setOnFocusChangeListener(onFocusChangeListener);
            }
        }
        return this;
    }

    public SmartViewHolder setOnClickListener(View.OnClickListener onClickListener, int... ids) {
        for (int id : ids) {
            View view = findViewById(id);
            if (view != null) {
                view.setOnClickListener(onClickListener);
            }
        }
        return this;
    }

    public EditText findEditText(int id) {
        View view = findViewById(id);
        if (view != null) {
            if (view instanceof EditText) {
                return (EditText) view;
            }
        }
        return null;
    }

    public boolean isEnable(int id) {
        return findViewById(id).isEnabled();
    }

    public SmartViewHolder setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        this.mListener = onItemClickListener;
        return this;
    }

    public SmartViewHolder select(int id, boolean b) {
        View view = findViewById(id);
        if (view != null) {
            view.setSelected(b);
        }
        return this;
    }

    public void setVisible(int id, boolean b) {
        if (b) {
            visible(id);
        } else {
            gone(id);
        }
    }
}