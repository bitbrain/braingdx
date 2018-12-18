package de.bitbrain.braingdx.android;

import android.os.Bundle;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import de.bitbrain.braingdx.apps.BrainGdxTest;

public class BrainGdxAppActivity extends AndroidApplication {

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
      initialize(new BrainGdxTest(), config);
   }
}