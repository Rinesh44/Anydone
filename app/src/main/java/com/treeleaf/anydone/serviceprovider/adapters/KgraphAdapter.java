package com.treeleaf.anydone.serviceprovider.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.treeleaf.anydone.entities.KGraphProto;
import com.treeleaf.anydone.serviceprovider.R;
import com.treeleaf.anydone.serviceprovider.realm.model.KGraph;

import java.util.List;

public class KgraphAdapter extends RecyclerView.Adapter<KgraphAdapter.KgraphHolder> {
    private static final String TAG = "KgraphAdapter";
    private List<KGraph> kGraphList;
    private Context mContext;
    private OnItemClickListener listener;

    public KgraphAdapter(List<KGraph> kGraphList, Context mContext) {
        this.kGraphList = kGraphList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public KgraphHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_kgraph_row, parent, false);
        return new KgraphHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull KgraphHolder holder, int position) {
        KGraph kGraph = kGraphList.get(position);

        holder.tvSuggestion.setText(kGraph.getTitle());
        if (kGraphList.indexOf(kGraph) == kGraphList.size() - 1) {
            holder.separator.setVisibility(View.GONE);
        }

     /*   if (kGraph.getAnswerType() != null &&
                kGraph.getAnswerType().equalsIgnoreCase(KGraphProto.AnswerType.ANSWER_TYPE.name())) {
            holder.tvSuggestion.setTextColor(mContext.getResources().getColor(R.color.black));
            holder.tvSuggestion.setTypeface(Typeface.DEFAULT);
        }*/

    }

    @Override
    public int getItemCount() {
        return kGraphList.size();
    }


    class KgraphHolder extends RecyclerView.ViewHolder {
        private TextView tvSuggestion;
        private View separator;

        KgraphHolder(@NonNull View itemView) {
            super(itemView);
            tvSuggestion = itemView.findViewById(R.id.tv_suggestion);
            separator = itemView.findViewById(R.id.v_separator);

            tvSuggestion.setOnClickListener(v -> {
                int position = getAdapterPosition();
                KGraph selectedKGraph = kGraphList.get(position);
                if (!(selectedKGraph.getAnswerType() != null &&
                        selectedKGraph.getAnswerType()
                                .equalsIgnoreCase(KGraphProto.KnowledgeType.ANSWER_TYPE.name()))) {
                    tvSuggestion.setBackgroundColor(mContext.getResources()
                            .getColor(R.color.colorPrimary));
                    tvSuggestion.setTextColor(mContext.getResources().getColor(R.color.white));
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(kGraphList.get(position));
                    }
                }
            });
        }
    }


    public interface OnItemClickListener {
        void onItemClick(KGraph kGraph);
    }

    public void setOnItemClickListener(KgraphAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }

}
