package mp3player;

import java.io.*;

import java.util.logging.Level;
import java.util.logging.Logger;

import javazoom.jl.player.Player;
import javazoom.jl.decoder.JavaLayerException;

public class MainClass
{
    ByteArrayInputStream FIS;
    BufferedInputStream BIS;

    public Player player;

    public long pauseLocation;
    public long songLength;

    public byte[] file;

    public int fl = 0;

    public void Stop()
    {
        if (player != null){
            player.close();

            pauseLocation = 0;
            songLength = 0;

            fl = 0;

        }
    }

    public void Play(byte[] fileToPlay)
    {
        if (fl == 0){
            try {
                file = fileToPlay;
                FIS = new ByteArrayInputStream(fileToPlay);
                BIS = new BufferedInputStream(FIS);

                player = new Player(BIS);

                songLength = FIS.available();

                fl = 1;

            } catch (JavaLayerException ex) {
            }

            new Thread()
            {
                @Override
                public void run()
                {
                    try {
                        player.play();
                    } catch (JavaLayerException ex) {
                        Logger.getLogger(MainClass.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }.start();
        }
    }


    public void Resume()
    {
        if (fl == 0)
        {
            try {
                FIS = new ByteArrayInputStream(file);
                BIS = new BufferedInputStream(FIS);

                player = new Player(BIS);

                FIS.skip(songLength - pauseLocation);

                fl = 1;

            } catch (JavaLayerException ex) {

            }

            new Thread()
            {
                @Override
                public void run()
                {
                    try {
                        player.play();
                    } catch (JavaLayerException ex) {
                        Logger.getLogger(MainClass.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }.start();
        }
    }

    public void Pause()
    {
        if (player != null)
        {
            pauseLocation = FIS.available();
            player.close();
            fl = 0;
        }
    }
}
