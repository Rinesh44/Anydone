package com.treeleaf.januswebrtc;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.treeleaf.freedrawingdemo.freedrawing.drawmetadata.Picture;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;


public class JoineeListAdapter extends RecyclerView.Adapter<JoineeListAdapter.ViewHolder> {

    public static final String JOINEE_DRAW_STAT = "JOINEE_DRAW_STAT";
    public static final String LOCAL_STATE = "LOCAL_STATE";
    public static final String REMOTE_STATE = "REMOTE_STATE";
    private List<Joinee> joinees;
    private Context mContext;
    public static final Integer MAX_IN_A_ROW = 6;
    private LinkedHashMap<String, Joinee> mapRemainingJoinees;
    private LinkedHashMap<String, Joinee> mapTotalJoinees;
    private JoineeListToggleUpdate joineeListToggleUpdate;
    public JoineeListState joineeListState;
    private OnItemClickListener onItemClickListener;
    private ModeListener mModeListener;
    private boolean isJoineeSoloDrawing = false;
    private String joineeStateUpdateMode = REMOTE_STATE;
    private HashMap<String, Integer> mapJoineePosition = new HashMap<>();

    /**
     * later add map for joinee id and position that only particular joinee position can be updated
     * instead of updating entire list
     */

    public JoineeListAdapter(Context context) {
        this.mContext = context;
        joinees = new ArrayList<>();
        mapRemainingJoinees = new LinkedHashMap<>();
        mapTotalJoinees = new LinkedHashMap<>();
        joineeListState = JoineeListState.CONTRACT;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setModeListener(ModeListener modeListener) {
        mModeListener = modeListener;
    }

    public Boolean isJoineePresent() {
        return mapTotalJoinees.size() > 1;
    }

    public void setJoineeListToggleUpdate(JoineeListToggleUpdate joineeListToggleUpdate) {
        this.joineeListToggleUpdate = joineeListToggleUpdate;
    }

    public ArrayList<Joinee> fetchAllJoinee() {
        return new ArrayList<>(mapTotalJoinees.values());
    }

    public void addNewJoinee(Joinee joinee, Boolean showFullList) {
        mapTotalJoinees.put(joinee.getAccountId(), joinee);
        if (showFullList) {
            joinees.clear();
            mapJoineePosition.clear();
            joinees.addAll(mapTotalJoinees.values());
            for (Joinee joinee1 : joinees) {
                mapJoineePosition.put(joinee1.getAccountId(), joinees.indexOf(joinee1));
            }

        } else {
            if (!joineesCountExceededMax()) {
                joinees.clear();
                mapJoineePosition.clear();
                joinees.addAll(mapTotalJoinees.values());
                for (Joinee joinee1 : joinees) {
                    mapJoineePosition.put(joinee1.getAccountId(), joinees.indexOf(joinee1));
                }
            } else {
                mapRemainingJoinees.put(joinee.getAccountId(), joinee);
            }
        }
        notifyDataSetChanged();
        joineeListToggleUpdate.onShowHideJoineeList(mapTotalJoinees.size() > 0);
        joineeListToggleUpdate.onShowHideToggleIcon(mapTotalJoinees.size() > MAX_IN_A_ROW);
    }

    private void fillJoinees(Boolean showFullList) {
        if (showFullList) {
            joinees.clear();
            mapJoineePosition.clear();
            joinees.addAll(mapTotalJoinees.values());
            for (Joinee joinee1 : joinees) {
                mapJoineePosition.put(joinee1.getAccountId(), joinees.indexOf(joinee1));
            }
        } else {
            Iterator<String> iterator = mapTotalJoinees.keySet().iterator();
            int i = 0;
            joinees.clear();
            mapJoineePosition.clear();
            if (mapTotalJoinees.size() > 0) {
                while (iterator.hasNext() && i < MAX_IN_A_ROW) {
                    joinees.add(mapTotalJoinees.get(iterator.next()));
                    i++;
                }
                for (Joinee joinee1 : joinees) {
                    mapJoineePosition.put(joinee1.getAccountId(), joinees.indexOf(joinee1));
                }
            }
        }
        notifyDataSetChanged();
    }

    public void removeJoinee(String accountId, Boolean showFullList) {
        if (accountId != null && !accountId.isEmpty() &&
                mapTotalJoinees != null && !mapTotalJoinees.isEmpty()) {
            Joinee joineeToRemove = mapTotalJoinees.get(accountId);
            if (joineeToRemove != null) {
                if (mapRemainingJoinees.containsValue(joineeToRemove))
                    mapRemainingJoinees.remove(accountId);
                if (mapTotalJoinees.containsValue(joineeToRemove))
                    mapTotalJoinees.remove(accountId);
            }
            fillJoinees(showFullList);

            if (mapTotalJoinees.size() == 0) {
                joineeListToggleUpdate.onShowHideJoineeList(false);
            }
            if (mapTotalJoinees.size() <= MAX_IN_A_ROW) {
                //call callback
                joineeListToggleUpdate.onShowHideToggleIcon(false);
            }
        }
    }

    public boolean joineesCountExceededMax() {
        return mapTotalJoinees.size() > MAX_IN_A_ROW;
    }

    public void toggleJoineeList(boolean showFullList) {
        fillJoinees(showFullList);
        joineeListState = showFullList ? JoineeListState.EXPAND : JoineeListState.CONTRACT;
        joineeListToggleUpdate.onListExpandContract(showFullList);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view =
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_joinee, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Joinee joinee = joinees.get(position);
        if (joinee != null) {
            String imgUri = joinee.getProfileUrl();
            RequestOptions options = new RequestOptions()
                    .fitCenter()
                    .placeholder(R.drawable.ic_empty_profile_holder_icon)
                    .error(R.drawable.ic_empty_profile_holder_icon);

            Glide.with(mContext)
                    .load(imgUri)
                    .apply(options)
                    .circleCrop()
                    .into(holder.ivJoinee);
            if (position == (MAX_IN_A_ROW - 1) && joineeListState == (JoineeListState.CONTRACT) && joineesCountExceededMax()) {
                holder.tvCountAdditionalJoinees.setVisibility(View.VISIBLE);
                int remaining = (mapTotalJoinees.size() - (MAX_IN_A_ROW - 1));
                holder.tvCountAdditionalJoinees.setText("+" + remaining);
            } else {
                holder.tvCountAdditionalJoinees.setVisibility(View.GONE);
                //TODO: for highlighting profile during draw
                if (mModeListener.getCurrentMode().equals(Mode.IMAGE_DRAW) && joinee.isDrawing()) {
                    holder.ivCurrentDrawer.setVisibility(View.VISIBLE);
                } else {
                    holder.ivCurrentDrawer.setVisibility(View.GONE);
                }

                /*if (mModeListener.getCurrentMode().equals(Mode.IMAGE_DRAW)) {
                    GradientDrawable drawable = (GradientDrawable) holder.flDrawHighlight.getBackground();
                    drawable.setStroke(6, joinee.getDrawColor());
                    holder.flDrawHighlight.setVisibility(View.VISIBLE);
                } else {
                    holder.flDrawHighlight.setVisibility(View.GONE);
                }*/

                /**
                 * - show colored border for profiles only in image draw mode
                 * - only green when its own picture
                 * - 
                 */

                if (mModeListener.getCurrentMode().equals(Mode.IMAGE_DRAW)) {
                    if (joinee.isSelfAccount()) {
                        GradientDrawable drawable = (GradientDrawable) holder.flDrawMode.getBackground();
                        drawable.setStroke(2, mContext.getResources().getColor(R.color.color_green));
                        holder.flDrawMode.setVisibility(View.VISIBLE);
                    } else {
                        if (joineeStateUpdateMode.equals(REMOTE_STATE)) {
                            if (joinee.getJoineeDrawStateRemote().equals(Joinee.JoineeDrawState.CLOSED)) {
                                //means joinee has not received image yet or closed the image
                                GradientDrawable drawable = (GradientDrawable) holder.flDrawMode.getBackground();
                                drawable.setStroke(2, mContext.getResources().getColor(R.color.color_red));
                                holder.flDrawMode.setVisibility(View.VISIBLE);
                            } else if (joinee.getJoineeDrawStateRemote().equals(Joinee.JoineeDrawState.MINIMIZED)) {
                                //means joinee has minimized the image
                                GradientDrawable drawable = (GradientDrawable) holder.flDrawMode.getBackground();
                                drawable.setStroke(2, mContext.getResources().getColor(R.color.color_yellow));
                                holder.flDrawMode.setVisibility(View.VISIBLE);
                            } else if (joinee.getJoineeDrawStateRemote().equals(Joinee.JoineeDrawState.MAXIMIZED)) {
                                //means joinee is on the same image
                                GradientDrawable drawable = (GradientDrawable) holder.flDrawMode.getBackground();
                                drawable.setStroke(2, mContext.getResources().getColor(R.color.color_green));
                                holder.flDrawMode.setVisibility(View.VISIBLE);
                            } else {
                                holder.flDrawMode.setVisibility(View.GONE);
                            }
                        } else {
                            if (joinee.getJoineeDrawStateLocal().equals(Joinee.JoineeDrawState.CLOSED)) {
                                //means joinee has not received image yet or closed the image
                                GradientDrawable drawable = (GradientDrawable) holder.flDrawMode.getBackground();
                                drawable.setStroke(2, mContext.getResources().getColor(R.color.color_red));
                                holder.flDrawMode.setVisibility(View.VISIBLE);
                            } else if (joinee.getJoineeDrawStateLocal().equals(Joinee.JoineeDrawState.MINIMIZED)) {
                                //means joinee has minimized the image
                                GradientDrawable drawable = (GradientDrawable) holder.flDrawMode.getBackground();
                                drawable.setStroke(2, mContext.getResources().getColor(R.color.color_yellow));
                                holder.flDrawMode.setVisibility(View.VISIBLE);
                            } else if (joinee.getJoineeDrawStateLocal().equals(Joinee.JoineeDrawState.MAXIMIZED)) {
                                //means joinee is on the same image
                                GradientDrawable drawable = (GradientDrawable) holder.flDrawMode.getBackground();
                                drawable.setStroke(2, mContext.getResources().getColor(R.color.color_green));
                                holder.flDrawMode.setVisibility(View.VISIBLE);
                            } else {
                                holder.flDrawMode.setVisibility(View.GONE);
                            }
                        }
                    }
                } else {
                    holder.flDrawMode.setVisibility(View.GONE);
                }

                if (mModeListener.getCurrentMode().equals(Mode.IMAGE_DRAW)) {
                    holder.viewCurrentColor.setBackgroundColor(joinee.getDrawColor());
                    holder.viewCurrentColor.setVisibility(View.VISIBLE);
                } else {
                    holder.viewCurrentColor.setVisibility(View.GONE);
                }


                if (joinee.isSoloDrawing()) {
                    holder.ivJoinee.setAlpha(255);//255 is fully opaque
                } else if (isJoineeSoloDrawing) {
                    holder.ivJoinee.setAlpha(90);
                } else
                    holder.ivJoinee.setAlpha(255);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemClickListener.onItemClicked(position, holder.itemView, joinee);
                    }
                });
            }
        }
    }

    public void onJoineeItemClicked(String joineeId) {
        Joinee joinee = mapTotalJoinees.get(joineeId);
        joinee.setSoloDrawing(!joinee.isSoloDrawing());
        isJoineeSoloDrawing = joinee.isSoloDrawing();
        for (Joinee otherJoinee : mapTotalJoinees.values()) {
            if (otherJoinee.getAccountId() != joineeId) {
                otherJoinee.setSoloDrawing(false);
            }
        }
        notifyDataSetChanged();
    }

    public void makeAllJoineesVisible() {
        for (Joinee joinee : mapTotalJoinees.values()) {
            joinee.setSoloDrawing(false);
        }
        isJoineeSoloDrawing = false;
        notifyDataSetChanged();
    }

    public void highlightCurrentDrawer(String accountId, boolean isCurrentDrawing,
                                       Integer drawColor) {
        Joinee currentDrawer = mapTotalJoinees.get(accountId);
        if (currentDrawer != null) {
            currentDrawer.setDrawing(isCurrentDrawing);
            currentDrawer.setDrawColor(drawColor);
            notifyItemChanged(mapJoineePosition.get(accountId));
        }
    }

    public void updateJoineeDrawStat(String accountId, Joinee.JoineeDrawState drawState,
                                     String imageId, boolean notifyAdapter) {
        Joinee joinee = mapTotalJoinees.get(accountId);
        //if maximize status comes, make this one maximize and other all minimize
        if (joinee != null && joinee.getMapImageDrawState() != null) {
            joinee.getMapImageDrawState().put(imageId, drawState);
            joinee.setJoineeDrawStateLocal(drawState);
            joinee.setJoineeDrawStateRemote(drawState);
            if (notifyAdapter) {
                setJoineeStatusUpdateMode(REMOTE_STATE);
                notifyItemChanged(mapJoineePosition.get(accountId));
            }
            if (drawState.equals(Joinee.JoineeDrawState.MAXIMIZED)) {
                /**
                 * if state is maximized set state of all other images
                 * other than this to minimized
                 */
                for (String imgId : joinee.getMapImageDrawState().keySet()) {
                    if (!imgId.equals(imageId) &&
                            !joinee.getMapImageDrawState().get(imgId).equals(Joinee.JoineeDrawState.CLOSED)) {
                        joinee.getMapImageDrawState().put(imgId, Joinee.JoineeDrawState.MINIMIZED);
                        joinee.setJoineeDrawStateLocal(Joinee.JoineeDrawState.MINIMIZED);
                        joinee.setJoineeDrawStateRemote(Joinee.JoineeDrawState.MINIMIZED);
                    }
                }
            }
        }

    }

    public void checkIfAllJoineesOnSamePicture(Picture picture) {
        if (picture != null) {
            for (Joinee joinee : new ArrayList<>(mapTotalJoinees.values())) {
                if (!joinee.isSelfAccount()) {
                    if (joinee.getMapImageDrawState().get(picture.getPictureId()) == null) {
                        joinee.setJoineeDrawStateLocal(Joinee.JoineeDrawState.CLOSED);
                    } else if (joinee.getMapImageDrawState().get(picture.getPictureId())
                            .equals(Joinee.JoineeDrawState.MAXIMIZED)) {
                        joinee.setJoineeDrawStateLocal(Joinee.JoineeDrawState.MAXIMIZED);
                    } else if (joinee.getMapImageDrawState().get(picture.getPictureId())
                            .equals(Joinee.JoineeDrawState.MINIMIZED)) {
                        joinee.setJoineeDrawStateLocal(Joinee.JoineeDrawState.MINIMIZED);
                    } else if (joinee.getMapImageDrawState().get(picture.getPictureId())
                            .equals(Joinee.JoineeDrawState.CLOSED)) {
                        joinee.setJoineeDrawStateLocal(Joinee.JoineeDrawState.CLOSED);
                    } else {
                        joinee.setJoineeDrawStateLocal(Joinee.JoineeDrawState.MINIMIZED);
                    }
                }
            }
            setJoineeStatusUpdateMode(LOCAL_STATE);
            notifyDataSetChanged();
        }
    }

    public void setJoineeStatusUpdateMode(String mode) {
        this.joineeStateUpdateMode = mode;
    }

    @Override
    public int getItemCount() {
        return joinees.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        public View itemView;
        public ImageView ivJoinee, ivCurrentDrawer;
        public TextView tvCountAdditionalJoinees;
        public FrameLayout flDrawMode;
        public View viewCurrentColor;

        ViewHolder(final View itemView) {
            super(itemView);
            this.itemView = itemView;
            ivJoinee = itemView.findViewById(R.id.iv_joinee);
            ivCurrentDrawer = itemView.findViewById(R.id.iv_current_drawer);
            flDrawMode = itemView.findViewById(R.id.fl_draw_mode);
            viewCurrentColor = itemView.findViewById(R.id.view_current_color);
            tvCountAdditionalJoinees = itemView.findViewById(R.id.tv_count_additional_joinees);
        }
    }

    public interface JoineeListToggleUpdate {

        void onListExpandContract(Boolean expand);

        void onShowHideToggleIcon(Boolean show);

        void onShowHideJoineeList(Boolean show);

    }

    public enum JoineeListState {
        EXPAND, CONTRACT
    }

    public enum Mode {
        IMAGE_DRAW, VIDEO_STREAM
    }

    public interface OnItemClickListener {
        void onItemClicked(int position, View v, Joinee joinee);
    }

    public interface ModeListener {
        Mode getCurrentMode();
    }

}
