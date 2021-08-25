package unsw.music;

import unsw.LoopManiaApplication;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Objects;
import javax.sound.sampled.*;

/**
 * <p>
 * Class {@code MusicPlayer}
 * </p>
 * <p>
 * Reference to online materials
 * </p>
 * <p>
 * https://github.com/Mpmart08/MusicPlayer
 * </p>
 *
 * @see unsw.music.MusicPlayer
 * @since 1.0
 **/
public class MusicPlayer {

    private final File musicFile;
    private volatile boolean isRunning = true;

    private AudioInputStream audioStream;
    private SourceDataLine sourceDataLine;
    private FloatControl volumeControl;

    /**
     * Method that can comtrol volume
     *
     * @return //
     */
    public FloatControl getVolumeControl() {
        return this.volumeControl;
    }

    /**
     * Method that get all the music
     *
     * @param fileName //
     * @return //
     */
    public static MusicPlayer getMusic(String fileName) {
        File file = null;
        try {
            file = new File(Objects.requireNonNull(LoopManiaApplication.class.getResource(fileName)).toURI().getPath());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return new MusicPlayer(file);
    }

    /**
     * Method that fetch music
     *
     * @param musicFile //
     */
    public MusicPlayer(File musicFile) {
        this.musicFile = musicFile;
        prefetch();
    }

    /**
     * Method that fetch all the music in MusicList
     */
    private void prefetch() {
        try {
            this.audioStream = AudioSystem.getAudioInputStream(musicFile);
            AudioFormat audioFormat = audioStream.getFormat();
            DataLine.Info dataLineInfo =
                new DataLine.Info(SourceDataLine.class, audioFormat, AudioSystem.NOT_SPECIFIED);
            this.sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
            this.sourceDataLine.open(audioFormat);
            this.volumeControl = (FloatControl) sourceDataLine.getControl(FloatControl.Type.MASTER_GAIN);
            this.sourceDataLine.start();
        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException ex) {
            ex.printStackTrace();
        }

    }

    /**
     * Method that play music
     *
     * @param loop //
     * @throws InterruptedException //
     */
    private void playMusic(boolean loop) throws InterruptedException {
        try {
            if (loop) {
                while (true) { playMusic(); }
            } else {
                playMusic();
            }
            sourceDataLine.drain();
            sourceDataLine.close();
            audioStream.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    /**
     * Method that play music
     */
    private void playMusic() {
        try {
            synchronized (this) { isRunning = true; }
            // AudioInputStream -> SourceDataLine;
            audioStream = AudioSystem.getAudioInputStream(musicFile);
            int count;
            byte[] tempBuff = new byte[1024];
            while ((count = audioStream.read(tempBuff, 0, tempBuff.length)) != -1) {
                synchronized (this) {
                    while (!isRunning) { wait(); }
                }
                sourceDataLine.write(tempBuff, 0, count);
            }
        } catch (UnsupportedAudioFileException | IOException | InterruptedException ex) {
            ex.printStackTrace();
        }

    }

    /**
     * Method that stop music
     */
    private void stopMusic() {
        synchronized (this) {
            isRunning = false;
            notifyAll();
        }
    }

    /**
     * Method that continue music
     */
    private void continueMusic() {
        synchronized (this) {
            isRunning = true;
            notifyAll();
        }
    }

    /**
     * Method that start music
     *
     * @param loop //
     */
    public void start(boolean loop) {
        Thread mainThread = new Thread(() -> {
            try {
                playMusic(loop);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        mainThread.start();
    }

    public void stop() {
        new Thread(this::stopMusic).start();
    }

    public void continues() {
        new Thread(this::continueMusic).start();
    }

}
