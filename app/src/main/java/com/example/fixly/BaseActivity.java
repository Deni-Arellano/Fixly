package com.example.fixly;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public abstract class BaseActivity extends AppCompatActivity {

    protected void setupEdgeToEdge(int layoutId) {
        EdgeToEdge.enable(this);
        setContentView(layoutId);


        getWindow().setStatusBarColor(getResources().getColor(R.color.azul_fixly));
        ViewCompat.getWindowInsetsController(getWindow().getDecorView())
                .setAppearanceLightStatusBars(false);

        View main = findViewById(R.id.main);
        View header = findViewById(R.id.header_container);
        View bottom = findViewById(R.id.bottom_container);

        ViewCompat.setOnApplyWindowInsetsListener(main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());

            if (header != null) {
                header.setPadding(
                        header.getPaddingLeft(),
                        systemBars.top,
                        header.getPaddingRight(),
                        header.getPaddingBottom()
                );


                ViewGroup.LayoutParams params = header.getLayoutParams();
                int baseHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 64, getResources().getDisplayMetrics());
                params.height = baseHeight + systemBars.top;
                header.setLayoutParams(params);
            }

            if (bottom != null) {

                bottom.setPadding(
                        bottom.getPaddingLeft(),
                        bottom.getPaddingTop(),
                        bottom.getPaddingRight(),
                        systemBars.bottom
                );

                ViewGroup.LayoutParams params = bottom.getLayoutParams();
                int baseHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 70, getResources().getDisplayMetrics());
                params.height = baseHeight + systemBars.bottom;
                bottom.setLayoutParams(params);
            }

            return insets;
        });
    }
}