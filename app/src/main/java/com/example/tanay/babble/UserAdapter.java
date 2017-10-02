package com.example.tanay.babble;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class UserAdapter extends BaseAdapter {

    private static final String TAG = "useradapter";

    private ArrayList<User> userList;

    public UserAdapter(ArrayList<User> userList) {
        Log.d(TAG, "UserAdapter: "+userList.size());
        this.userList = userList;

    }

    @Override
    public int getCount() {

        return userList.size();

    }

    @Override
    public Object getItem(int i) {
        return userList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        User user = userList.get(i);

        if(view == null) {
            View userView = viewGroup.inflate(viewGroup.getContext(), R.layout.user_item, null);

            TextView name = (TextView) userView.findViewById(R.id.name);
            name.setText(user.getName());
            Log.d(TAG, "getView: ");
            return userView;
        }else {

            TextView name = (TextView) view.findViewById(R.id.name);
            name.setText(user.getName());

            return view;

        }
    }
}
