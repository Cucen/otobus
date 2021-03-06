package com.msi.android.home;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import com.androidquery.AQuery;

public class MusicAnaActivity extends Activity implements OnSeekBarChangeListener{

	private AQuery aq;
	private ViewHolder holder;
	private Drawable mpPlay;
	private Drawable mpPause;
	
	public String TAG = "MusicAnaActivity";

	// private String path = "/mnt/sdcard/DCIM/Camera/sample.mp4";
	// private String path = "rtsp://dcdn.motiwe.com:80/dogantv/startv.stream";

	private String lookingpath;
	protected String lookingpathparam;
	private String lookingimgpath;
	protected String lookingimgpathparam;
	private String lookingtitle;
	protected String lookingtitleparam;
	
//	private final TweenManager _tweenManager = new TweenManager();
	
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.musicview);
		
		Bundle bundle = this.getIntent().getExtras();
		lookingpath = bundle.getString(lookingpathparam);
		lookingimgpath = bundle.getString(lookingimgpathparam);
		lookingtitle = bundle.getString(lookingtitleparam);
		
		initAttr();
		initViews();
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		hidePlayer();
	}
	
	private void initAttr(){
		if(aq == null){
			aq = new AQuery(this);
			holder = new ViewHolder();
			mpPause = getResources().getDrawable(R.drawable.mp_pause);
			mpPlay = getResources().getDrawable(R.drawable.mp_play);
		}
	}
	
	private void initViews(){
		if(holder.rlMediaplayer == null){
			holder.rlMediaplayer = (RelativeLayout) aq.id(R.id.rlMediaplayer).getView();
			holder.rlHeader = (RelativeLayout) aq.id(R.id.rlHeader).getView();
			holder.bPrev = (ImageButton) aq.id(R.id.bPrev).getView();
			holder.bPlayPause = (ImageButton) aq.id(R.id.bPlayPause).getView();
			holder.bNext = (ImageButton) aq.id(R.id.bNext).getView();
			holder.tvMusicTitle = aq.id(R.id.tvMusicTitle).getTextView();
			holder.ivMusicImg = aq.id(R.id.ivMusicImg).getImageView();
			holder.pb = aq.id(R.id.progress).getProgressBar();
			holder.sb = (SeekBar) aq.id(R.id.sb).getView();
			holder.mp = new MediaPlayer();
			holder.tvPosAndDuration = aq.id(R.id.tvPosAndDuration).getTextView();
			
			holder.sb.setOnSeekBarChangeListener(this);
			holder.mp.setOnPreparedListener(new OnPreparedListener() {
				
				public void onPrepared(MediaPlayer mp) {
//					holder.pb.setVisibility(View.GONE);
					aq.id(holder.ivMusicImg)
						.progress(holder.pb)
						.image(lookingimgpath, true, true, 0, 0, null,AQuery.FADE_IN);
					aq.id(holder.tvPosAndDuration).visible();
					holder.tvMusicTitle.setText(lookingtitle);
					holder.bPlayPause.setImageDrawable(mpPause);
					holder.sb.setMax(mp.getDuration());
					mp.start();
					showPlayer();
					checkSBar();
				}
			});
			
			holder.mp.setOnCompletionListener(new OnCompletionListener() {
				
				public void onCompletion(MediaPlayer mp) {
//					mp.stop();
					holder.bPlayPause.setImageDrawable(mpPlay);
					holder.sb.setProgress(0);
					holder.tvPosAndDuration.setText("00:00 / 00:00");
				}
			});
		}
		holder.pb.setVisibility(View.VISIBLE);
		holder.tvMusicTitle.setText("lutfen bekleyin");
		try {
			//_holder.mp.setDataSource(_clickedMusic.getMusicpath());
			
			holder.mp.setDataSource(lookingpath);
			holder.mp.prepareAsync();
//			holder.mp.start();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void showPlayer(){
		aq.id(holder.rlMediaplayer).animate(R.anim.fade_in);
//		aq.id(holder.rlHeader).animate(R.anim.flyin);
		aq.id(holder.rlMediaplayer).visible();
	}
	
	private void hidePlayer(){
		aq.id(holder.rlMediaplayer).animate(android.R.anim.fade_out);
		holder.mp.stop();
//		_aq.id(_holder.flHeader).animate(R.anim.grow_from_bottom);
	}
	
	public void mpButtonClick(View v){
		switch(v.getId()){
		case R.id.bShowList:
			finish();
			break;
		case R.id.bPrev:
			break;
		case R.id.bPlayPause:
			if(holder.mp.isPlaying()){
				holder.mp.pause();
				holder.bPlayPause.setImageDrawable(mpPlay);
			}
			else{
				holder.mp.start();
				 holder.bPlayPause.setImageDrawable(mpPause);
				 if(holder.mp.getDuration() <= 0){
					 holder.mp.prepareAsync();
					 return;
				 }
				 checkSBar();
			}
			break;
		case R.id.bNext:
			break;
		}
	}
	
	public void checkSBar(){
		
		Thread t = new Thread(new Runnable() {
			
			public void run() {
				while(holder.mp.isPlaying()){
					runOnUiThread(new Runnable() {
						
						public void run() {
							holder.sb.setProgress(holder.mp.getCurrentPosition());
							displayCurrentPosInTime();
						}
						
					});
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		t.start();
		
	}
	
	@SuppressLint("NewApi")
	protected void displayCurrentPosInTime(){
		int itotal = holder.mp.getDuration();
		int icurrent = holder.mp.getCurrentPosition();
		
		String time = String.format("%02d:%02d / %02d:%02d", 
				TimeUnit.MILLISECONDS.toMinutes(icurrent),
				TimeUnit.MILLISECONDS.toSeconds(icurrent) % 60,
				TimeUnit.MILLISECONDS.toMinutes(itotal),
				TimeUnit.MILLISECONDS.toSeconds(itotal) % 60);
		
		holder.tvPosAndDuration.setText(time);
	}
	
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		if(fromUser){
			holder.mp.seekTo(progress);
		}
		
	}

	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		
	}

	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		
	}
	
	final static private class ViewHolder{
		RelativeLayout rlMediaplayer;
		ImageView ivMusicImg;
		ImageButton bPlayPause; 
		TextView tvMusicTitle;
		ImageButton bShowList; 
		ImageButton bPrev; 
		RelativeLayout rlHeader;
		ImageButton bNext; 
		MediaPlayer mp;
		ProgressBar pb;
		SeekBar sb;
		TextView tvPosAndDuration;
	}

}
