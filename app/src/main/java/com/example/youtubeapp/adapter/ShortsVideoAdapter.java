package com.example.youtubeapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.youtubeapp.R;
import com.example.youtubeapp.fragment.ShortsFragment;
import com.example.youtubeapp.model.itemrecycleview.VideoItem;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.YouTubePlayerUtils;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ShortsVideoAdapter extends RecyclerView.Adapter<ShortsVideoAdapter.ShortsViewHolder> {
    ArrayList<VideoItem> listItems;
    public void setData(ArrayList<VideoItem> listItems) {

        this.listItems = listItems;
    }
    @NonNull
    @Override
    public ShortsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_video_shorts, parent, false);
        return new ShortsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShortsViewHolder holder, int position) {
        VideoItem item = listItems.get(position);
        if (item == null) {
            return;
        }
        holder.setData(item);
    }

    @Override
    public int getItemCount() {
        if (listItems != null) {
            return listItems.size();
        }
        return 0;
    }

    class ShortsViewHolder extends RecyclerView.ViewHolder {

        YouTubePlayerView ypvShorts;
        TextView tvLike, tvCmtCount, tvTitleChannel, tvDesc;
        CircleImageView civLogoChannel;
        ProgressBar pbLoading;
        public ShortsViewHolder(@NonNull View itemView) {
            super(itemView);
            ypvShorts = itemView.findViewById(R.id.ypv_shorts);
            tvLike = itemView.findViewById(R.id.tv_like_count_shorts);
            tvCmtCount = itemView.findViewById(R.id.tv_comment_count_shorts);
            tvTitleChannel = itemView.findViewById(R.id.tv_title_channel_shorts);
            tvDesc = itemView.findViewById(R.id.tv_desc_shorts);
            civLogoChannel = itemView.findViewById(R.id.civ_logo_channel_shorts);
//            pbLoading = itemView.findViewById(R.id.pb_loading_shorts);
        }
        public void setData(VideoItem item) {
            View customPlayerUi = ypvShorts.inflateCustomPlayerUi(R.layout.custom_player_ui);
            String urlLogoChannel = item.getUrlLogoChannel();
            String titleChannel = item.getTvTitleChannel();
            String titleVideo = item.getTvTitleVideo();
//            String likeCount = Util.convertViewCount(Double.parseDouble(item.getLikeCountVideo()));
//            String cmtCount = Util.convertViewCount(Double.parseDouble(item.getCommentCount()));
            String idVideo = item.getIdVideo();
            String idChannel = item.getIdChannel();
            String urlVideo = "https://www.youtube.com/shorts/" + idVideo;

            YouTubePlayerListener listener = new AbstractYouTubePlayerListener() {
                @Override
                public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                    CustomPlayerUiController customPlayerUiController = new CustomPlayerUiController( customPlayerUi, youTubePlayer, ypvShorts);
                    youTubePlayer.addListener(customPlayerUiController);
                    ypvShorts.addFullScreenListener(customPlayerUiController);

                    youTubePlayer.loadVideo(idVideo, 0);
                }
            };

            IFramePlayerOptions options = new IFramePlayerOptions.Builder().controls(0).build();

            ypvShorts.initialize(listener, options);

//            vvShorts.setVideoPath(urlVideo);
//            vvShorts.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//                @Override
//                public void onPrepared(MediaPlayer mp) {
//                    pbLoading.setVisibility(View.GONE);
//                    mp.start();
//                    float videoRatio = mp.getVideoWidth() / (float) mp.getVideoHeight();
//                    float screenRatio = vvShorts.getWidth() / (float) vvShorts.getHeight();
//
//                    float scale = videoRatio / screenRatio;
//                    if (scale > 1f) {
//                        vvShorts.setScaleX(scale);
//                    } else {
//                        vvShorts.setScaleY(1f / scale);
//                    }
//                }
//            });
//            vvShorts.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                @Override
//                public void onCompletion(MediaPlayer mp) {
//                    mp.start();
//                }
//            });
            tvDesc.setText(titleVideo);
            tvCmtCount.setText("cmtCount");
            tvTitleChannel.setText(titleChannel);
            tvLike.setText("likeCount");
            Picasso.get().load(urlLogoChannel).into(civLogoChannel);
        }
    }
}
