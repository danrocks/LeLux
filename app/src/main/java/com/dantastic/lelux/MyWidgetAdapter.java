package com.dantastic.lelux;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dantastic.lelux.databinding.WidgetViewBinding;

import java.util.List;


/**
 * Created by ALL on 11/2/2016.
 * Adapt BrightnessCommands to the, displayed by widget view, for the
 * https://guides.codepath.com/android/using-the-recyclerview
 */

// This adapter allows list of BrightnessCommands to be displayed be recycler view.
// Note the custom viewholder is specified., this gives access to the views.
// The adapter's role is to convert an object at a position into a list row item to be inserted.
// DJK - it seems to be convention to set-up wthe generic RecyclerView.Adapter with an Inner class. I find this odd.
// Android -  Adapter provide a binding from an app-specfic data set to views that are displayed within a recycler view.
public class MyWidgetAdapter extends RecyclerView.Adapter<MyWidgetAdapter.MyBindingHolder> {
    List<BrightnessCommand> mBrightnessCommands;

    //pass in list of commands
    public MyWidgetAdapter(List<BrightnessCommand> brightnessCommands) {
        this.mBrightnessCommands = brightnessCommands;
    }

    // Usually involves inflating a layout form XML and cretes then return the holder.
    @Override
    public MyBindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view =(LayoutInflater.from(parent.getContext())).inflate( R.layout.widget_view, parent,false);
        Log.e(parent.getContext().getString(R.string.Tag), "onCreateViewHolder");
        return new MyBindingHolder(view);
    }


    // Involves populating data into the ite through the holder.
    // Ususally set view attribute values based on data.
    // This is using data binding instead, so DataBinding methoda re used.
    @Override
    public void onBindViewHolder(MyBindingHolder holder, int position) {
        final BrightnessCommand brightnessCommand = mBrightnessCommands.get(position);
        holder.getBinding().setVariable(BR.brightnessCommand, brightnessCommand);
        holder.getBinding().executePendingBindings();
        Log.e("DebugMessage", "onBindViewHolder");
    }

    // Returns the total count of items in the list, used by internal mechanism.
    @Override
    public int getItemCount() {
        Log.e("DebugMessage", "mBrightnessCommands.size(): " + String.valueOf(mBrightnessCommands.size()));
        return mBrightnessCommands.size();
    }

    //Provide a direct reference to each of the views that the RecyclerView will be showing
    //Used to cache the view within the item latout for fast access.
    // Adapter requires existence of view holderobject (which describes and provides access to all views within each item row)
    // DJK - I find it odd that this is static - it feels like one is being created for each row of brightnessCommands.
    public static class MyBindingHolder extends RecyclerView.ViewHolder{
        //Using DataBinding, so WidgetViewBinding is automatically created.
        // the view holder refeneces this( rather than individual components of the widget)
        private WidgetViewBinding binding;

        // constructor accepts entire view row.
        // traditionally would have set final member variables for each subview(eg text view, button)
        // that would need set with data.
        // In this case though, we just set-up binding using the convenience DataBindingUtil.bind.
        public MyBindingHolder(View itemView) {
            super(itemView);
            Log.e(itemView.getContext().getString(R.string.Tag), "MyBindingHolder created");
            binding =DataBindingUtil.bind(itemView);
            binding.setPresenter(new Presenter());
        }

        public WidgetViewBinding getBinding(){
            return binding;
        }
    }
}
