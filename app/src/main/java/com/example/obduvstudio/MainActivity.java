package com.example.obduvstudio;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private MediaRecorder mediaRecorder;
    private boolean isRecording = false;
    private String currentFilePath = "";
    private Button recordButton;
    private RecyclerView tracksRecyclerView;
    private TrackAdapter trackAdapter;
    private List<AudioTrack> tracks = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_main);

        String projectName = getIntent().getStringExtra("PROJECT_NAME");
        if (projectName == null) {
            projectName = "Без названия";
        }

        TextView projectTitle = findViewById(R.id.projectTitle);
        projectTitle.setText(projectName);

        recordButton = findViewById(R.id.recordButton);
        tracksRecyclerView = findViewById(R.id.tracksRecyclerView);

        trackAdapter = new TrackAdapter(tracks);
        tracksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        tracksRecyclerView.setAdapter(trackAdapter);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    REQUEST_RECORD_AUDIO_PERMISSION);
        }

        recordButton.setOnClickListener(v -> {
            if (isRecording) {
                stopRecording();
                recordButton.setText("НАЧАТЬ ЗАПИСЬ");
                recordButton.setBackgroundColor(ContextCompat.getColor(this, android.R.color.holo_red_dark));
            } else {
                if (startRecording()) {
                    recordButton.setText("ОСТАНОВИТЬ ЗАПИСЬ");
                    recordButton.setBackgroundColor(ContextCompat.getColor(this, android.R.color.holo_green_dark));
                }
            }
        });
    }

    private boolean startRecording() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Разрешение на запись аудио не предоставлено", Toast.LENGTH_SHORT).show();
            return false;
        }

        try {
            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);

            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
            String fileName = "AUDIO_" + timeStamp + ".m4a";
            File storageDir = getExternalFilesDir(Environment.DIRECTORY_MUSIC);
            File audioFile = new File(storageDir, fileName);
            currentFilePath = audioFile.getAbsolutePath();

            mediaRecorder.setOutputFile(currentFilePath);
            mediaRecorder.prepare();
            mediaRecorder.start();
            isRecording = true;

            Toast.makeText(this, "Запись начата", Toast.LENGTH_SHORT).show();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Ошибка записи", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private void stopRecording() {
        if (mediaRecorder != null) {
            try {
                mediaRecorder.stop();
                mediaRecorder.release();
                mediaRecorder = null;
                isRecording = false;

                String trackName = "Запись " + (tracks.size() + 1);
                AudioTrack newTrack = new AudioTrack(trackName, currentFilePath, "0:00");
                tracks.add(newTrack);
                trackAdapter.notifyItemInserted(tracks.size() - 1);

                Toast.makeText(this, "Запись сохранена", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Ошибка при остановке записи", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Разрешение предоставлено", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Разрешение отклонено", Toast.LENGTH_SHORT).show();

            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isRecording) {
            stopRecording();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaRecorder != null) {
            mediaRecorder.release();
            mediaRecorder = null;
        }
    }
}