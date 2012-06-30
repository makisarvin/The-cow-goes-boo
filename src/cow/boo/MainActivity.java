package cow.boo;

import java.util.HashMap;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ViewSwitcher.ViewFactory;

public class MainActivity extends Activity implements OnTouchListener, AnimationListener {

	private static int[] pictureIds = { R.drawable.bear, R.drawable.cat,
			R.drawable.chicken, R.drawable.chimpansee, R.drawable.cow,
			R.drawable.dog, R.drawable.donkey, R.drawable.elephant,
			R.drawable.frog, R.drawable.goat, R.drawable.goose,
			R.drawable.horse, R.drawable.kitten, R.drawable.lion,
			R.drawable.monkey, R.drawable.pig, R.drawable.rooster,
			R.drawable.seal, R.drawable.sheep, R.drawable.tiger,
			R.drawable.turkey, R.drawable.whale, R.drawable.wolf };

	private static int[] soundIds = { R.raw.bear, R.raw.cat, R.raw.chicken,
			R.raw.chimpansee, R.raw.cow, R.raw.dog, R.raw.donkey,
			R.raw.elephant, R.raw.frog, R.raw.goat, R.raw.goose, R.raw.horse,
			R.raw.kitten, R.raw.lion, R.raw.monkey, R.raw.pig, R.raw.rooster,
			R.raw.seal, R.raw.sheep, R.raw.tiger, R.raw.turkey, R.raw.whale,
			R.raw.wolf };
	
	private static final String CURRENT_POSITION = "currentPosition";
	private static final int STARTING_POSITION = 0;
	private static final int ZERO_POINT = 50;
	private static final int DIRECTION_LEFT = 1;
	private static final int DIRECTION_RIGHT = 2;

	private MediaPlayer mp;
	private SharedPreferences preferences;
	private ImageSwitcher imageSwitcher;
	private int currentPosition = 0;
	private double startSwipeX;
	private int direction;
	
	//custom animations
	private HashMap<String, Animation> animationMap;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		
		preferences = getPreferences(MODE_WORLD_READABLE);
		Editor editor = preferences.edit();
		editor.putInt(CURRENT_POSITION, STARTING_POSITION);
		
		currentPosition = preferences.getInt(CURRENT_POSITION, 0);
		
		imageSwitcher = (ImageSwitcher)findViewById(R.id.image_switcher);
		imageSwitcher.setFactory(new MyImageSwitcherFactory());
		imageSwitcher.setImageDrawable(getDrawable(currentPosition));
		imageSwitcher.setOnTouchListener(this);
		
		//load animation for later
		animationMap = new HashMap<String, Animation>();
		animationMap.put("slide_in_left", AnimationUtils.loadAnimation(this, R.anim.slide_in_left));
		animationMap.put("slide_out_left", AnimationUtils.loadAnimation(this, R.anim.slide_out_left));
		animationMap.put("slide_in_right", AnimationUtils.loadAnimation(this, R.anim.slide_in_right));
		animationMap.put("slide_out_right", AnimationUtils.loadAnimation(this, R.anim.slide_out_right));
		
		for (Animation value: animationMap.values()) {
			value.setAnimationListener(this);
		}
			
	}
	
	public boolean onTouch(View v, MotionEvent event) {
		
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
				startSwipeX = event.getX();
		}
		
		if (event.getAction() == MotionEvent.ACTION_UP) {
			double difference = startSwipeX - event.getX();
			if (difference > ZERO_POINT) { //swipe right
				direction = DIRECTION_RIGHT;
				imageSwitcher.startAnimation(animationMap.get("slide_out_left"));
			} else if (difference < -ZERO_POINT) { //swipe left
				direction = DIRECTION_LEFT;
				imageSwitcher.startAnimation(animationMap.get("slide_out_right"));
			} else { // click
				if (mp != null) {
					mp.release();
				}
				mp = MediaPlayer.create(this, soundIds[currentPosition]);
				mp.start();
			}
			return true;
		}
		
		return false;
	}
	
	private void setNextImage() {
		if (++currentPosition > pictureIds.length-1) {
			currentPosition = 0;
		} 
		if (mp != null) mp.stop();
		Log.d("COW APP", "next current position: " + currentPosition);
		imageSwitcher.setImageDrawable(getDrawable(currentPosition));
		imageSwitcher.startAnimation(animationMap.get("slide_in_right"));
		
	}
	
	private void setPreviousImage() {
		if (--currentPosition < 0) {
			currentPosition = pictureIds.length-1;
		} 
		if (mp != null) mp.stop();
		Log.d("COW APP", "previous current position: " + currentPosition);
		imageSwitcher.setImageDrawable(getDrawable(currentPosition));
		imageSwitcher.startAnimation(animationMap.get("slide_in_left"));
	}
	
	private Drawable getDrawable(int index) {
	    return getResources().getDrawable(pictureIds[index]);
	}


	/** animation listener methods */
	
	public void onAnimationEnd(Animation animation) {
		//depending on the direction we need to replace the animation with a new one and replace the image also.
		//we care only for the slide out animations
		int direc = direction;
		direction = 0;
		switch (direc) {
		case DIRECTION_LEFT:
			Log.d("COW APP", "changing image previous");
			setPreviousImage();
			break;
		case DIRECTION_RIGHT:
			Log.d("COW APP", "changing image previous");
			setNextImage();
			break;
		default:
		}
	}

	public void onAnimationRepeat(Animation animation) {
		// TODO Auto-generated method stub
		
	}

	public void onAnimationStart(Animation animation) {
		// TODO Auto-generated method stub
	}
	
	private class MyImageSwitcherFactory implements ViewFactory {
	    public View makeView() {
	        ImageView imageView = new ImageView(MainActivity.this);
	        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
	        imageView.setLayoutParams(new ImageSwitcher.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
	        return imageView;
	    }
	}

}
