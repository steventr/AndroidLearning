package itp341.truong.steven.presence;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.TextView;

import itp341.truong.steven.presence.ClassesInfoFragment.OnListFragmentInteractionListener;
import itp341.truong.steven.presence.dummy.DummyContent.DummyItem;

import java.util.List;

import static android.view.View.*;

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

        if (mValues.size() == 0) {
            mValues.add(new Class("PAST_END", "PAST_END"));
        }
        else {
            if(!mValues.get((mValues.size()-1)).id.equals("PAST_END"))
            {
                mValues.add(new Class("PAST_END", "PAST_END"));
            }
        }

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
            holder.mTitleView.setText("Add a class");

            holder.mView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Go to new activity to add a class...
                    Intent i = new Intent(context,ManageClassActivity.class);
                    i.putExtra("newclass", true);
                    ((Activity) context).startActivityForResult(i, ActivityConstants.Activities.MANAGE_CLASS_ACTIVITY.toInteger());
                }
            });
            holder.mView.findViewById(R.id.detail).setVisibility(GONE);
            ((CheckedTextView) holder.mView.findViewById(R.id.selectedClassesFragmentCheckedTextView)).setCheckMarkDrawable(R.drawable.ic_add_white_24dp);
            holder.mView.findViewById(R.id.selectedClassesFragmentCheckedTextView).setVisibility(VISIBLE);
            ((CheckedTextView) holder.mView.findViewById(R.id.selectedClassesFragmentCheckedTextView)).setChecked(true);

        }
        else {
            holder.mTitleView.setText(mValues.get(position).name);
            holder.mDetailView.setText(mValues.get(position).details);

            holder.mView.setOnLongClickListener(new OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    //Go to new activity to add a class...
                    Intent i = new Intent(context, ManageClassActivity.class);
                    i.putExtra("name", mValues.get(position).name);
                    i.putExtra("classID", mValues.get(position).id);
                    i.putExtra("details", mValues.get(position).details);
                    i.putExtra("newclass", false);
                    ((Activity) context).startActivityForResult(i, ActivityConstants.Activities.MANAGE_CLASS_ACTIVITY.toInteger());
                    return false;
                }
            });

            holder.mView.setOnClickListener(new OnClickListener() {
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

            if (position == mCheckedPosition) {
                holder.mView.findViewById(R.id.selectedClassesFragmentCheckedTextView).setVisibility(VISIBLE);
                ((CheckedTextView) holder.mView.findViewById(R.id.selectedClassesFragmentCheckedTextView)).setChecked(true);
            }
            else {
                holder.mView.findViewById(R.id.selectedClassesFragmentCheckedTextView).setVisibility(INVISIBLE);
            }
        }


    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mTitleView;
        public final TextView mDetailView;
        public Class mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mTitleView = (TextView) view.findViewById(R.id.title);
            mDetailView = (TextView) view.findViewById(R.id.detail);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mDetailView.getText() + "'";
        }
    }
}
