package com.treeleaf.januswebrtc;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.treeleaf.freedrawingdemo.freedrawing.drawmetadata.Picture;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;


public class PictureStackAdapter extends RecyclerView.Adapter<PictureStackAdapter.ViewHolder> {

    public static final String TAG = PictureStackAdapter.class.getSimpleName();
    public final String PICTURE_MAPPINGS = "PICTURE_MAPPINGS";

    private List<Picture> pictures;
    private Context mContext;
    public static final Integer MAX_IN_A_ROW = 6;
    private OnItemClickListener onItemClickListener;
    private JoineeListAdapter.ModeListener mModeListener;
    private LinkedHashMap<String, Integer> mapPicturePosition = new LinkedHashMap<>();

    public PictureStackAdapter(Context context) {
        this.mContext = context;
        pictures = new ArrayList<>();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setModeListener(JoineeListAdapter.ModeListener modeListener) {
        mModeListener = modeListener;
    }

    public void addNewPicture(Picture picture) {
        pictures.add(picture);
        notifyDataSetChanged();
        reorganizePicturePositingMappings();
        showPictureStackLog(pictures, mapPicturePosition);
    }

    public Picture getPictureFromPosition(int position) {
        return pictures.get(position);
    }


    public void removePicture(int position) {
        pictures.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, pictures.size());
        reorganizePicturePositingMappings();
        showPictureStackLog(pictures, mapPicturePosition);
    }

    private void reorganizePicturePositingMappings() {
        mapPicturePosition.clear();
        for (Picture picture : pictures) {
            mapPicturePosition.put(picture.getPictureId(), pictures.indexOf(picture));
        }
    }

    public void onCollabInvite(String imageId) {
        if (mapPicturePosition.get(imageId) != null) {
            try {
                Picture pic = pictures.get(mapPicturePosition.get(imageId));
//            pic.setNewArrival(true);
                pic.setNewArrival(false);
                pic.setRequestedForCollab(true);
                notifyItemChanged(mapPicturePosition.get(imageId));
            } catch (IndexOutOfBoundsException exception) {
                Log.d(TAG, exception.getLocalizedMessage());
            } catch (Exception exception) {
                Log.d(TAG, exception.getLocalizedMessage());
            }
        }
    }

    //TODO: not used but might need later
    public void onDrawOnMinimize(String imageId) {
        if (mapPicturePosition.get(imageId) != null) {
            try {
                Picture pic = pictures.get(mapPicturePosition.get(imageId));//TODO: need to add try catch or if else here later
                pic.setBckDrawCount(pic.getBckDrawCount() + 1);
                notifyItemChanged(mapPicturePosition.get(imageId));
            } catch (IndexOutOfBoundsException exception) {
                Log.d(TAG, exception.getLocalizedMessage());
            } catch (Exception exception) {
                Log.d(TAG, exception.getLocalizedMessage());
            }
        }
    }

    public void updatePicture(int position, Picture picToReplace) {
        /**
         * replace picture data of clicked item
         */
        pictures.remove(position);
        Picture newPicture = VideoCallUtil.updatePictureContents(picToReplace);
        newPicture.setRequestedForCollab(false);//since pic is clicked, its no longer new arrival now
        newPicture.setNewArrival(false);//redundant
//        newPicture.setBckDrawCount(0);
        pictures.add(position, newPicture);
        notifyItemChanged(position);
        reorganizePicturePositingMappings();
        showPictureStackLog(pictures, mapPicturePosition);
    }

    private void showPictureStackLog(List<Picture> pictures, LinkedHashMap<String, Integer> mapPicturePosition) {
        for (Picture picture : pictures) {
            Log.d(PICTURE_MAPPINGS, " pictures list: " + pictures.indexOf(picture) + " id: " + picture.getPictureId());
        }
        for (String id : mapPicturePosition.keySet()) {
            Log.d(PICTURE_MAPPINGS, "picturs map: " + mapPicturePosition.get(id) + " " + id);
        }
    }

    public boolean isOldPicture(String imageId) {
        boolean isOld = false;
        for (Picture picture : pictures) {
            if (picture.getPictureId().equals(imageId)) {
                isOld = true;
                break;
            }
        }
        return isOld;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view =
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_drawing, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Picture picture = pictures.get(position);
        if (picture != null) {

            RequestOptions options = new RequestOptions()
                    .fitCenter()
                    .placeholder(R.drawable.ic_empty_profile_holder_icon)
                    .error(R.drawable.ic_empty_profile_holder_icon);

            Glide.with(mContext)
                    .load(picture.getBitmap())
                    .apply(options)
                    .circleCrop()
                    .into(holder.ivPicture);

            holder.tvPictureId.setText(String.valueOf(picture.getPictureId()));
//            holder.ivPictureNotification.setVisibility((picture.isNewArrival() || picture.isRequestedForCollab()) ?
            holder.ivPictureNotification.setVisibility(picture.isRequestedForCollab() ?
                    View.VISIBLE : View.GONE);
            /*if (picture.getBckDrawCount() > 0) {
                holder.tvDrawNotification.setVisibility(View.VISIBLE);
                holder.tvDrawNotification.setText(String.valueOf(picture.getBckDrawCount()));
            } else {
                holder.tvDrawNotification.setVisibility(View.GONE);
            }*/

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClicked(position, holder.itemView, picture);
                }
            });
        }
    }

    public void onJoineeItemClicked(String joineeId) {

    }


    @Override
    public int getItemCount() {
        return pictures.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        public View itemView;
        public ImageView ivPicture, ivPictureNotification;
        public TextView tvPictureId, tvDrawNotification;

        ViewHolder(final View itemView) {
            super(itemView);
            this.itemView = itemView;
            ivPicture = itemView.findViewById(R.id.iv_picture);
            ivPictureNotification = itemView.findViewById(R.id.iv_picture_notification);
            tvPictureId = itemView.findViewById(R.id.tv_picture_id);
            tvDrawNotification = itemView.findViewById(R.id.tv_draw_notification);
        }
    }

    public interface OnItemClickListener {
        void onItemClicked(int position, View v, Picture joinee);
    }


}
