package itp341.truong.steven.presence;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.TextView;

import com.github.ivbaranov.mli.MaterialLetterIcon;

import itp341.truong.steven.presence.ClassesInfoFragment.OnListFragmentInteractionListener;
import itp341.truong.steven.presence.dummy.DummyContent.DummyItem;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.*;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MemberRecyclerViewAdapter extends RecyclerView.Adapter<MemberRecyclerViewAdapter.MemberViewHolder> {

    private Context context;
    private final List<Member> mValues;

    private int mCheckedPosition;
    private ArrayList<String> usedPins;
    private String currentClass;

    public MemberRecyclerViewAdapter(List<Member> members, String classID) {

        mValues = members;
        currentClass = classID;

        if (mValues.size() == 0) {
            mValues.add(new Member("PAST_END"));
        }
        else {
            if(!mValues.get((mValues.size()-1)).memberID.equals("PAST_END"))
            {
                mValues.add(new Member("PAST_END"));
            }
        }
    }

    @Override
    public MemberViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.member_list_item, parent, false);
        return new MemberViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MemberViewHolder holder, final int position) {

        holder.mItem = mValues.get(position);
        if (holder.mItem.memberID.equals("PAST_END")) {
            //Icon '+'
            ((MaterialLetterIcon) holder.mView.findViewById(R.id.icon)).setShapeColor(R.color.colorAccent);
            ((MaterialLetterIcon) holder.mView.findViewById(R.id.icon)).setLetterColor(R.color.black);
            ((MaterialLetterIcon) holder.mView.findViewById(R.id.icon)).setLetter("+");

            holder.mTitleView.setText("Add class member");
            holder.mTitleView.setTextAlignment(TEXT_ALIGNMENT_CENTER);
            holder.mDetailView.setVisibility(INVISIBLE);

            holder.mView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Adding a new class member
                    Intent i = new Intent(context, ManageMemberActivity.class);
                    i.putExtra("newmember", true);
                    i.putExtra("classID", currentClass);

                    usedPins = new ArrayList<String>();

                    for (Member m : mValues) {
                        if (m.memberID != "PAST_END") {
                            usedPins.add(m.pin);
                        }
                    }
                    i.putStringArrayListExtra("usedPins", usedPins);

                    ((Activity) context).startActivityForResult(i, ActivityConstants.Activities.MANAGE_CLASS_MEMBER_ACTIVITY.toInteger());
                }
            });
        }
       else {
            //Icon color
            int[] colors = context.getResources().getIntArray(R.array.androidcolors);
            ((MaterialLetterIcon) holder.mView.findViewById(R.id.icon)).setLettersNumber(2);
            ((MaterialLetterIcon) holder.mView.findViewById(R.id.icon)).setShapeColor(colors[position % colors.length]);
            ((MaterialLetterIcon) holder.mView.findViewById(R.id.icon)).setLetter(holder.mItem.name.substring(0,1).toUpperCase());

            holder.mTitleView.setText(mValues.get(position).name);
            holder.mDetailView.setText(String.valueOf(mValues.get(position).pin));

            holder.mView.setOnLongClickListener(new OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    //Go to new activity to add a class...
                    Intent i = new Intent(context, ManageMemberActivity.class);
                    i.putExtra("newmember", false);
                    i.putExtra("classID", currentClass);
                    i.putExtra("memberID", holder.mItem.memberID);
                    i.putExtra("name", holder.mItem.name);
                    i.putExtra("pin", holder.mItem.pin);

                    usedPins = new ArrayList<String>();

                    for (Member m : mValues) {
                        if (m.memberID != "PAST_END") {
                            usedPins.add(m.pin);
                        }
                    }
                    i.putStringArrayListExtra("usedPins", usedPins);


                    ((Activity) context).startActivityForResult(i, ActivityConstants.Activities.MANAGE_CLASS_MEMBER_ACTIVITY.toInteger());
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class MemberViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mTitleView;
        public final TextView mDetailView;
        public Member mItem;

        public MemberViewHolder(View view) {
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
