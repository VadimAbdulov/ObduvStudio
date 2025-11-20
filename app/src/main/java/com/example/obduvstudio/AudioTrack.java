package com.example.obduvstudio;

public class AudioTrack {
    private String name;
    private String filePath;
    private String duration;

    public AudioTrack(String name, String filePath, String duration) {
        this.name = name;
        this.filePath = filePath;
        this.duration = duration;
    }

    public String getName() { return name; }
    public String getFilePath() { return filePath; }
    public String getDuration() { return duration; }
}