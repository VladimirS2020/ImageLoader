import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class ImageLoader implements Runnable {
    private String url;
    private String filename;
    private Storage storage;
    private Server main;
    private Server additional;
    private static int i = 0;

    public ImageLoader(String url, Storage storage, Server main, Server additional) {
        this.storage = storage;
        this.main = main;
        this.additional = additional;
        this.url = url;
        filename = url.substring(url.lastIndexOf('/') + 1);
    }

    @Override
    public void run() {
        URL urlConnection = null;
        try {
            urlConnection = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try (InputStream is = urlConnection.openStream();
             OutputStream os = new FileOutputStream(filename)) {
            int bt;
            while ((bt = is.read()) != -1
                    && !Thread.currentThread().isInterrupted()) {
                storage.addByte();
                os.write(bt);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        storage.addImage();
        if (i++ % 2 == 0) {
            storage.uploadToServer(main, additional);
        } else {
            storage.uploadToServer(additional, main);
        }
    }
}