package com.treeleaf.januswebrtc.tickets.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.treeleaf.januswebrtc.R;
import com.treeleaf.januswebrtc.tickets.model.Attachment;

import java.util.ArrayList;
import java.util.List;


public class AttachmentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "AttachmentAdapter";
    public static final int TYPE_IMAGE = 1;
    public static final int TYPE_PDF = 2;
    public static final int TYPE_WORD = 3;
    public static final int TYPE_EXCEL = 4;
    public static final int TYPE_ADD = 5;

    private List<Attachment> attachmentList;
    private Context mContext;
    private OnAddAttachmentListener onAttachClickListener;
    private OnAttachmentRemoveListener attachmentRemoveListener;
    private OnAttachmentImageClickListener attachmentImageClickListener;
    private OnDocClickListener onDocClickListener;

    public AttachmentAdapter(List<Attachment> attachmentList, Context mContext) {
        this.attachmentList = attachmentList;
        this.mContext = mContext;
    }

    public void addData(List<Attachment> attachmentList) {
        this.attachmentList = attachmentList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_IMAGE:
                View imageView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.attachment_image, parent, false);
                return new ImageViewHolder(imageView);

            case TYPE_PDF:
                View pdfView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.attachment_pdf, parent, false);
                return new PDFViewHolder(pdfView);

            case TYPE_WORD:
                View wordView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.attachment_word, parent, false);
                return new WordViewHolder(wordView);

            case TYPE_EXCEL:
                View excelView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.attachment_excel, parent, false);
                return new ExcelViewHolder(excelView);

            default:
                View defaultView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.attachment_image, parent, false);
                return new ImageViewHolder(defaultView);
        }
    }

    @Override
    public int getItemViewType(int position) {
        Attachment attachment = attachmentList.get(position);
        switch (attachment.getType()) {
            case 1:
                return TYPE_IMAGE;

            case 2:
                return TYPE_PDF;

            case 3:
                return TYPE_WORD;

            case 4:
                return TYPE_EXCEL;

        }
        return super.getItemViewType(position);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Attachment attachment = attachmentList.get(position);

        switch (holder.getItemViewType()) {
            case TYPE_IMAGE:
                ((ImageViewHolder) holder).bind(attachment);
                break;

            case TYPE_PDF:
                ((PDFViewHolder) holder).bind(attachment);
                break;

            case TYPE_WORD:
                ((WordViewHolder) holder).bind(attachment);
                break;

            case TYPE_EXCEL:
                ((ExcelViewHolder) holder).bind(attachment);
                break;

        }
    }

    @Override
    public int getItemCount() {
        return attachmentList.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle;
        private ImageView ivImage;
        private RelativeLayout rlImage;
        private ImageView ivClose;
        private ProgressBar progress;

        public ImageViewHolder(View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tv_image_title);
            ivImage = itemView.findViewById(R.id.iv_image);
            rlImage = itemView.findViewById(R.id.rl_image);
            ivClose = itemView.findViewById(R.id.iv_close);
            progress = itemView.findViewById(R.id.pb_progress);

            rlImage.setOnClickListener(v -> {
                if (attachmentImageClickListener != null && getAdapterPosition() !=
                        RecyclerView.NO_POSITION) {
                    List<String> imageUrlList = new ArrayList<>();
                    for (Attachment attachment : attachmentList
                    ) {
                        if (attachment.getType() == 1) {
                            imageUrlList.add(attachment.getUrl());
                        }
                    }

                    int pos = 0;
                    Attachment attachment = attachmentList.get(getAdapterPosition());
                    for (String url : imageUrlList
                    ) {
                        if (url.equalsIgnoreCase(attachment.getUrl())) {
                            pos = imageUrlList.indexOf(url);
                        }
                    }
                    attachmentImageClickListener.onImageClick(pos, imageUrlList);
                }
            });

            ivClose.setOnClickListener(v -> {
                if (attachmentRemoveListener != null) {
                    attachmentRemoveListener.onRemove(attachmentList.get(getAdapterPosition()));
                }
            });
        }

        RequestOptions options = new RequestOptions()
                .fitCenter()
                .placeholder(R.drawable.ic_imageholder)
                .error(R.drawable.ic_imageholder);

        void bind(Attachment attachment) {
            tvTitle.setText(attachment.getTitle());
            Glide.with(mContext)
                    .load(attachment.getUrl())
                    .apply(options)
                    .into(ivImage);
        }
    }

    public class ExcelViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle;
        private ImageView ivExcel;
        private RelativeLayout rlExcel;
        private ImageView ivClose;
        private ProgressBar progress;

        public ExcelViewHolder(View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tv_excel_title);
            ivExcel = itemView.findViewById(R.id.iv_excel);
            rlExcel = itemView.findViewById(R.id.rl_excel);
            ivClose = itemView.findViewById(R.id.iv_close);
            progress = itemView.findViewById(R.id.pb_progress);

            rlExcel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            ivClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }


        void bind(Attachment attachment) {
            tvTitle.setText(attachment.getTitle());
        }
    }

    public class PDFViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle;
        private ImageView ivPdf;
        private RelativeLayout rlPdf;
        private ImageView ivClose;
        private ProgressBar progress;

        public PDFViewHolder(View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tv_pdf_title);
            ivPdf = itemView.findViewById(R.id.iv_pdf);
            rlPdf = itemView.findViewById(R.id.rl_pdf);
            ivClose = itemView.findViewById(R.id.iv_close);
            progress = itemView.findViewById(R.id.pb_progress);

            rlPdf.setOnClickListener(v -> {
                if (onDocClickListener != null && getAdapterPosition() !=
                        RecyclerView.NO_POSITION) {
                    Attachment attachment = attachmentList.get(getAdapterPosition());
                    onDocClickListener.onDocClick(attachment);
                }
            });

            ivClose.setOnClickListener(v -> {
                if (attachmentRemoveListener != null) {
                    attachmentRemoveListener.onRemove(attachmentList.get(getAdapterPosition()));
                }
            });
        }

        void bind(Attachment attachment) {
            tvTitle.setText(attachment.getTitle());
        }

    }

    public class WordViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle;
        private ImageView ivWord;
        private RelativeLayout rlWord;
        private ImageView ivClose;
        private ProgressBar progress;

        public WordViewHolder(View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tv_doc_title);
            ivWord = itemView.findViewById(R.id.iv_word);
            rlWord = itemView.findViewById(R.id.rl_word);
            ivClose = itemView.findViewById(R.id.iv_close);
            progress = itemView.findViewById(R.id.pb_progress);

            rlWord.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            ivClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

        void bind(Attachment attachment) {
            tvTitle.setText(attachment.getTitle());
        }

    }

    public interface OnAddAttachmentListener {
        void onAddAttachment();
    }

    public void setOnAddAttachmentClickListener(OnAddAttachmentListener listener) {
        this.onAttachClickListener = listener;
    }

    public interface OnDocClickListener {
        void onDocClick(Attachment attachment);
    }

    public void setOnDocClickListener(OnDocClickListener listener) {
        this.onDocClickListener = listener;
    }

    public interface OnAttachmentImageClickListener {
        void onImageClick(int pos, List<String> imageUrlList);
    }

    public void setOnAttachmentImageClickListener(OnAttachmentImageClickListener listener) {
        this.attachmentImageClickListener = listener;
    }

    public interface OnAttachmentRemoveListener {
        void onRemove(Attachment attachment);
    }

    public void setOnAttachmentRemoveListener(OnAttachmentRemoveListener listener) {
        this.attachmentRemoveListener = listener;
    }


}
