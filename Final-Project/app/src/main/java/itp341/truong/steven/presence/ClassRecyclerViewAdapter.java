package itp341.truong.steven.presence;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.TextView;

import itp341.truong.steven.presence.ClassFragment.OnListFragmentInteractionListener;
import itp341.truong.steven.presence.dummy.DummyContent.DummyItem;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class ClassRecyclerViewAdapter extends RecyclerView.Adapter<ClassRecyclerViewAdapter.ViewHolder> {

    private Context context;
    private final List<Class> mValues;
    private final OnListFragmentInteractionListener mListener;
    private int mCheckedPosition;

    public ClassRecyclerViewAdapter(List<Class> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mValues.add(new Class("PAST_END", "PAST_END"));
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_class_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.mItem = mValues.get(position);
        if (holder.mItem.details.equals("PAST_END") || holder.mItem.id.equals("PAST_END")) {
            //This will be the '+' button.
            holder.mIdView.setText("Add a class");

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Go to new activity to add a class...
                    Intent i = new Intent(context,ManageClassActivity.class);
                    ((Activity) context).startActivityForResult(i, 1);
                    Log.d("Test", "+ button pressed");
                }
            });
            ((TextView) holder.mView.findViewById(R.id.content)).setVisibility(View.GONE);
            ((CheckedTextView) holder.mView.findViewById(R.id.selectedClassesFragmentCheckedTextView)).setCheckMarkDrawable(R.drawable.ic_add_white_24dp);
            ((CheckedTextView) holder.mView.findViewById(R.id.selectedClassesFragmentCheckedTextView)).setVisibility(View.VISIBLE);
            ((CheckedTextView) holder.mView.findViewById(R.id.selectedClassesFragmentCheckedTextView)).setChecked(true);

        }
        else {
            holder.mIdView.setText(mValues.get(position).id);
            holder.mContentView.setText(mValues.get(position).name);

            if (position == mCheckedPosition) {
                ((CheckedTextView) holder.mView.findViewById(R.id.selectedClassesFragmentCheckedTextView)).setVisibility(View.VISIBLE);
                ((CheckedTextView) holder.mView.findViewById(R.id.selectedClassesFragmentCheckedTextView)).setChecked(true);
            }
            else {
                ((CheckedTextView) holder.mView.findViewById(R.id.selectedClassesFragmentCheckedTextView)).setVisibility(View.INVISIBLE);
            }

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (position == mCheckedPosition) {
                        holder.mItem.isSelected = false;
                        mCheckedPosition = -1;
                    }
                    else {
                        mCheckedPosition = position;
                        notifyDataSetChanged();
                    }
                }
            });
        }


    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public Class mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
