/*
 * Copyright (C) 2018 CyberAgent, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package jp.co.cyberagent.android.gpuimage.sample.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import jp.co.cyberagent.android.gpuimage.GPUImageView;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.sample.GPUImageFilterTools;
import jp.co.cyberagent.android.gpuimage.sample.R;

public class GalleryActivity extends AppCompatActivity {

    private GPUImageFilterTools.FilterAdjuster filterAdjuster;
    private GPUImageView gpuImageView;
    private SeekBar seekBar;
    
    private static final int REQUEST_PICK_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        
        gpuImageView = findViewById(R.id.gpuimage);
        seekBar = findViewById(R.id.seekBar);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (filterAdjuster != null) {
                    filterAdjuster.adjust(progress);
                }
                gpuImageView.requestRender();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        findViewById(R.id.button_choose_filter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GPUImageFilterTools.showDialog(GalleryActivity.this, new GPUImageFilterTools.OnFilterSelectedListener() {
                    @Override
                    public void onFilterSelected(GPUImageFilter filter) {
                        switchFilterTo(filter);
                        gpuImageView.requestRender();
                    }
                });
            }
        });
        
        findViewById(R.id.button_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveImage();
            }
        });

        startPhotoPicker();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_PICK_IMAGE) {
            if (resultCode == RESULT_OK) {
                gpuImageView.setImage(data.getData());
            } else {
                finish();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void startPhotoPicker() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, REQUEST_PICK_IMAGE);
    }

    private void saveImage() {
        String fileName = System.currentTimeMillis() + ".jpg";
        gpuImageView.saveToPictures("GPUImage", fileName, new GPUImageView.OnPictureSavedListener() {
            @Override
            public void onPictureSaved(Uri uri) {
                Toast.makeText(GalleryActivity.this, "Saved: " + uri.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void switchFilterTo(GPUImageFilter filter) {
        if (gpuImageView.getFilter() == null || gpuImageView.getFilter().getClass() != filter.getClass()) {
            gpuImageView.setFilter(filter);
            filterAdjuster = new GPUImageFilterTools.FilterAdjuster(filter);
            if (filterAdjuster.canAdjust()) {
                seekBar.setVisibility(View.VISIBLE);
                filterAdjuster.adjust(seekBar.getProgress());
            } else {
                seekBar.setVisibility(View.GONE);
            }
        }
    }
}