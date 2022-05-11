package ru.Audio;

import javazoom.spi.mpeg.sampled.file.MpegAudioFileReader;
import org.apache.commons.io.IOUtils;

import javax.sound.sampled.*;
import java.io.*;
import java.util.Map;

public class AudioPlayer {

    private Clip clip;
    private FloatControl control;
    public float previousVolume = 0;
    public float currentVolume = 0;

    public static final float MAX_VOLUME = 6.0f;
    public static final float MIN_VOLUME = -80.0f;

    public boolean mute = false;

    public AudioPlayer(String s){

        try{
            //InputStream audioSrc = getClass().getResourceAsStream(s);
            //add buffer for mark/reset support
            //InputStream bufferedIn = new BufferedInputStream(audioSrc);
            //AudioInputStream ais = AudioSystem.getAudioInputStream(bufferedIn);
            AudioInputStream ais =
                    AudioSystem.getAudioInputStream(
                            getClass().getResourceAsStream(s)
                    );
            AudioFormat baseFormat = ais.getFormat();
            AudioFormat decodeFormat = new AudioFormat(
                    AudioFormat.Encoding.PCM_SIGNED,
                    baseFormat.getSampleRate(),
                    16,
                    baseFormat.getChannels(),
                    baseFormat.getChannels() * 2,
                    baseFormat.getSampleRate(),
                    false
            );
            AudioInputStream dais =
                    AudioSystem.getAudioInputStream(
                            decodeFormat,
                            ais
                    );
            clip = AudioSystem.getClip();
            clip.open(dais);

            control = (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    public void setVolume(float volume){
        currentVolume = volume;
        control.setValue(currentVolume);
    }
    public void volumeUp(){
        currentVolume += 1.0f;
        if (currentVolume > MAX_VOLUME){
            currentVolume = MAX_VOLUME;
        }
        control.setValue(currentVolume);
    }

    public void volumeDown(){
        currentVolume -= 1.0f;
        if(currentVolume < MIN_VOLUME){
            currentVolume = MIN_VOLUME;
        }
        control.setValue(currentVolume);
    }

    public void muteVolume(){
        if(!mute){
            previousVolume = currentVolume;
            currentVolume = MIN_VOLUME;
            control.setValue(currentVolume);
            mute = true;
        }
        else{
            currentVolume = previousVolume;
            control.setValue(currentVolume);
            mute = false;
        }
    }

    public void loop(){
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void play(){
        if(clip == null) return;
        stop();
        clip.setFramePosition(0);
        clip.start();
        //clip.loop(Clip.LOOP_CONTINUOUSLY);
    }
    public void stop(){
        if(clip.isRunning()) clip.stop();
    }

    public void close(){
        stop();
        clip.close();
    }

    // returns mp3's duration
    public long getDuration(String s){
        long duration = 0;
        try{
            File file = new File("temp");
            InputStream is = getClass().getResourceAsStream(s);
            OutputStream outputStream = new FileOutputStream(file);
            IOUtils.copy(is, outputStream);
            AudioFileFormat baseFileFormat = new MpegAudioFileReader().getAudioFileFormat(file);
            Map properties = baseFileFormat.properties();
            duration = (long) properties.get("duration");
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return duration;
    }
}
