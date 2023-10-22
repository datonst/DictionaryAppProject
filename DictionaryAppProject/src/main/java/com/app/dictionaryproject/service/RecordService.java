package com.example.jademo;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class RecordService {
//    public static RecordService INSTANCE;
    public TargetDataLine targetDataLine;
    private Thread thread;
    private boolean isRecording;

    private String path;
    public RecordService(String path) {
        this.path = path;
        // Specify the audio format
        AudioFormat audioFormat = new AudioFormat(
                AudioFormat.Encoding.PCM_SIGNED,
                44100,
                16,
                2,
                4,
                44100,
                false
        );
        DataLine.Info dataInfo = new DataLine.Info(TargetDataLine.class,audioFormat);
        if (!AudioSystem.isLineSupported(dataInfo)) {
            System.out.println("Not supported");
        }


        try {
            targetDataLine = (TargetDataLine) AudioSystem.getLine(dataInfo);
            targetDataLine.open();
//            targetDataLine = AudioSystem.getTargetDataLine(audioFormat);
//            targetDataLine.open(audioFormat);
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
        isRecording = false;
    }

//    public static RecordService getInstance() {
//        if (INSTANCE == null) {
//            INSTANCE = new RecordService();
//        }
//        return INSTANCE;
//    }

    public void startRecording() {
        if (!isRecording && targetDataLine != null) {
            isRecording = true;
            targetDataLine.start();
            this.thread = new Thread(() -> {
                AudioInputStream audioInputStream = new AudioInputStream(targetDataLine);
                //    System.out.println(path);
                File outputFile = new File(path);
                try {
                    AudioSystem.write(audioInputStream,AudioFileFormat.Type.WAVE,outputFile);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("STOP record");
            });
            this.thread.start();
            this.thread.interrupt();
        }
    }

    public void stopRecording() {

        if( thread!=null) {
            targetDataLine.stop();
            targetDataLine.close();
            this.thread.interrupt();
        }
        isRecording = false;
    }
}
