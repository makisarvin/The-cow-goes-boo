package cow.boo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashActivity extends Activity implements AnimationListener {

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        ImageView splashIcon = (ImageView)findViewById(R.id.splash_icon);
        
        Animation animation = AnimationUtils.loadAnimation(this, android.R.anim.fade_out);      
        Animation customAnimation = AnimationUtils.loadAnimation(this, R.anim.custom_anim);
        customAnimation.setAnimationListener(this);
      //splashIcon.startAnimation(animation);
        splashIcon.startAnimation(customAnimation);

   }

	public void onAnimationEnd(Animation animation) {
		this.startActivity(new Intent(this, MainActivity.class));
		
	}

	public void onAnimationRepeat(Animation animation) {
		// TODO Auto-generated method stub
		
	}

	public void onAnimationStart(Animation animation) {
		// TODO Auto-generated method stub
		
	}
}