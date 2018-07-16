package codepath.kaughlinpractice.fridgefone;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class LoadingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        Thread mythread = new Thread(){
            @Override
            public void run() {
                try {
                    sleep(3000);
                    Intent i = new Intent(getApplicationContext(), FridgeActivity.class);
                    startActivity(i);
                    finish();// destroys activity after it is used
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        mythread.start();
    }
}
