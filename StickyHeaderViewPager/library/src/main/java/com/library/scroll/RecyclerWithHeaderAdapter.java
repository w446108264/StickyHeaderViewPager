package com.library.scroll;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by sj on 15/11/20.
 */
public abstract class RecyclerWithHeaderAdapter<T extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = -1616;
    private View placeHolderView;

    @Override
    public final RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if(viewType == TYPE_HEADER){
            if(placeHolderView == null){
                placeHolderView = new View(viewGroup.getContext());
                viewGroup.addView(placeHolderView);
            }
            return new RecyclerPlaceViewHolder(placeHolderView);
        }
        return oncreateViewHolder(viewGroup, viewType);
    }

    public View getPlaceHolderView(){
        return placeHolderView;
    }

    public abstract T oncreateViewHolder(ViewGroup viewGroup, int viewType) ;

    @Override
    public final int getItemViewType(int position) {
        return position == 0 ? TYPE_HEADER : super.getItemViewType(position);
    }

    private class RecyclerPlaceViewHolder extends RecyclerView.ViewHolder {
        public RecyclerPlaceViewHolder(View itemView) {
            super(itemView);
        }
    }
}
