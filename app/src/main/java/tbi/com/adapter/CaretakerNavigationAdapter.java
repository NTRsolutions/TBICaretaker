package tbi.com.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import tbi.com.R;
import tbi.com.activity.main_activity.CaretakerHomeActivity;
import tbi.com.fragment.caretaker.AddSuffererFragment;
import tbi.com.fragment.caretaker.FaqsCaretakerFragment;
import tbi.com.chat.fragment.MessageCaretakerFragment;
import tbi.com.fragment.caretaker.MyProfileCaretakerFragment;
import tbi.com.fragment.caretaker.MySuffererFragment;
import tbi.com.fragment.caretaker.NotificationsCaretakerFragment;
import tbi.com.fragment.caretaker.ReminderCaretakerFragment;
import tbi.com.model.NavigationListModel;
import tbi.com.session.Session;

public class CaretakerNavigationAdapter extends RecyclerView.Adapter<CaretakerNavigationAdapter.ViewHolder> {
    public int lastclick = -1;
    private List<NavigationListModel> navigationList;
    private CaretakerHomeActivity mContext;

    public CaretakerNavigationAdapter(ArrayList<NavigationListModel> navigationList, CaretakerHomeActivity mContext) {
        this.navigationList = navigationList;
        this.mContext = mContext;
    }

    @Override
    public CaretakerNavigationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.navigation_list_layout, parent, false);
        return new CaretakerNavigationAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final CaretakerNavigationAdapter.ViewHolder holder, final int position) {
        NavigationListModel navigationListModel = navigationList.get(position);

        holder.iv_for_image.setImageResource(navigationListModel.image);
        holder.tv_for_nameTittle.setText(navigationListModel.name);

        if (lastclick != -1) {
            if (lastclick == position) {
                holder.view_for_click.setVisibility(View.VISIBLE);
                holder.iv_for_image.setImageResource(navigationListModel.selectedImage);
                holder.tv_for_nameTittle.setTextColor(Color.parseColor("#5e8d93"));
            } else {
                holder.view_for_click.setVisibility(View.GONE);
                holder.iv_for_image.setImageResource(navigationListModel.image);
                holder.tv_for_nameTittle.setTextColor(Color.parseColor("#333333"));
            }
        }
    }

    @Override
    public int getItemCount() {
        return navigationList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView iv_for_image;
        private TextView tv_for_nameTittle;
        private View view_for_click;
        private LinearLayout layout_for_item;

        ViewHolder(View itemView) {
            super(itemView);

            iv_for_image = itemView.findViewById(R.id.iv_for_image);
            tv_for_nameTittle = itemView.findViewById(R.id.tv_for_nameTittle);
            view_for_click = itemView.findViewById(R.id.view_for_click);
            layout_for_item = itemView.findViewById(R.id.layout_for_item);
            layout_for_item.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.layout_for_item:
                    view_for_click.setVisibility(View.VISIBLE);
                    if (getAdapterPosition() == 0) {
                        lastclick = getAdapterPosition();
                        notifyDataSetChanged();
                        Session session = new Session(mContext);
                        if (session.getLogin().equals("0")) {
                            ((CaretakerHomeActivity) mContext).replaceFragment(AddSuffererFragment.newInstance(""), true, R.id.framlayout);
                            mContext.tv_for_tittle.setText(R.string.add_sufferer);
                            mContext.iv_for_calender.setVisibility(View.GONE);
                            mContext.iv_for_menu.setVisibility(View.VISIBLE);
                            mContext.iv_for_backIco.setVisibility(View.GONE);
                            mContext.iv_for_edit.setVisibility(View.GONE);
                            mContext.iv_for_more.setVisibility(View.GONE);
                            mContext.iv_for_delete.setVisibility(View.GONE);
                        } else {
                            ((CaretakerHomeActivity) mContext).replaceFragment(MySuffererFragment.newInstance(""), true, R.id.framlayout);
                            mContext.tv_for_tittle.setText(R.string.my_sufferer);
                            mContext.iv_for_calender.setVisibility(View.GONE);
                            mContext.iv_for_menu.setVisibility(View.VISIBLE);
                            mContext.iv_for_backIco.setVisibility(View.GONE);
                            mContext.iv_for_edit.setVisibility(View.GONE);
                            mContext.iv_for_more.setVisibility(View.GONE);
                            mContext.iv_for_delete.setVisibility(View.GONE);
                        }
                        mContext.drawer.closeDrawers();
                    } else if (getAdapterPosition() == 1) {
                        lastclick = getAdapterPosition();
                        notifyDataSetChanged();
                        ((CaretakerHomeActivity) mContext).replaceFragment(ReminderCaretakerFragment.newInstance(""), false, R.id.framlayout);
                        mContext.drawer.closeDrawers();
                        mContext.tv_for_tittle.setText(R.string.reminders);
                        mContext.iv_for_calender.setVisibility(View.VISIBLE);
                        mContext.iv_for_menu.setVisibility(View.VISIBLE);
                        mContext.iv_for_backIco.setVisibility(View.GONE);
                        mContext.iv_for_edit.setVisibility(View.GONE);
                        mContext.iv_for_more.setVisibility(View.GONE);
                        mContext.iv_for_delete.setVisibility(View.GONE);
                    } else if (getAdapterPosition() == 2) {
                        lastclick = getAdapterPosition();
                        notifyDataSetChanged();
                        ((CaretakerHomeActivity) mContext).replaceFragment(MyProfileCaretakerFragment.newInstance(""), true, R.id.framlayout);
                        mContext.drawer.closeDrawers();
                        mContext.tv_for_tittle.setText(R.string.my_profile);
                        mContext.iv_for_calender.setVisibility(View.GONE);
                        mContext.iv_for_menu.setVisibility(View.VISIBLE);
                        mContext.iv_for_backIco.setVisibility(View.GONE);
                        mContext.iv_for_edit.setVisibility(View.VISIBLE);
                        mContext.iv_for_more.setVisibility(View.GONE);
                        mContext.iv_for_delete.setVisibility(View.GONE);
                    } else if (getAdapterPosition() == 3) {
                        lastclick = getAdapterPosition();
                        notifyDataSetChanged();
                        ((CaretakerHomeActivity) mContext).replaceFragment(MessageCaretakerFragment.newInstance(""), true, R.id.framlayout);
                        mContext.drawer.closeDrawers();
                        mContext.tv_for_tittle.setText(R.string.messages);
                        mContext.iv_for_calender.setVisibility(View.GONE);
                        mContext.iv_for_menu.setVisibility(View.VISIBLE);
                        mContext.iv_for_backIco.setVisibility(View.GONE);
                        mContext.iv_for_edit.setVisibility(View.GONE);
                        mContext.iv_for_more.setVisibility(View.GONE);
                        mContext.iv_for_delete.setVisibility(View.GONE);
                    } else if (getAdapterPosition() == 4) {
                        lastclick = getAdapterPosition();
                        notifyDataSetChanged();
                        ((CaretakerHomeActivity) mContext).replaceFragment(NotificationsCaretakerFragment.newInstance(""), true, R.id.framlayout);
                        mContext.drawer.closeDrawers();
                        mContext.tv_for_tittle.setText(R.string.notifications);
                        mContext.iv_for_calender.setVisibility(View.GONE);
                        mContext.iv_for_menu.setVisibility(View.VISIBLE);
                        mContext.iv_for_backIco.setVisibility(View.GONE);
                        mContext.iv_for_edit.setVisibility(View.GONE);
                        mContext.iv_for_more.setVisibility(View.GONE);
                        mContext.iv_for_delete.setVisibility(View.GONE);
                    } else if (getAdapterPosition() == 5) {
                        lastclick = getAdapterPosition();
                        notifyDataSetChanged();
                        ((CaretakerHomeActivity) mContext).replaceFragment(FaqsCaretakerFragment.newInstance(""), true, R.id.framlayout);
                        mContext.drawer.closeDrawers();
                        mContext.tv_for_tittle.setText(R.string.faq_s);
                        mContext.iv_for_calender.setVisibility(View.GONE);
                        mContext.iv_for_menu.setVisibility(View.VISIBLE);
                        mContext.iv_for_backIco.setVisibility(View.GONE);
                        mContext.iv_for_edit.setVisibility(View.GONE);
                        mContext.iv_for_more.setVisibility(View.VISIBLE);
                        mContext.iv_for_delete.setVisibility(View.GONE);
                    }
                    break;
            }
        }
    }

}
