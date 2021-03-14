import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;

import java.util.ArrayList;
import java.util.List;

public class Controller {

    @FXML
    Button loadButton;
    @FXML
    TextField imageRef;
    @FXML
    ToggleButton loadingProgress;
    @FXML
    Button cancelLoad;

    @FXML
    public void stopLoading() {
        /*        loadImageInThread.interrupt();*/
        System.out.println("Мы прервали поток");
    }

    @FXML
    public void loadImage() {
        Storage storage = new Storage();
        List<String> files = new ArrayList<>();
        files.add("https://million-wallpapers.ru/wallpapers/0/83/493776229567513/udivitelnyj-zamok.jpg");
        files.add("https://en.wikipedia.org/wiki/File:N%26SAmerica-pol.jpg");
        Server server1 = new Server();
        server1.setName("Main");
        Server server2 = new Server();
        server2.setName("Additional");
        for (String file : files) {
            new Thread(new ImageLoader(file, storage, server1, server2)).start();
        }

        new Thread() {
            @Override
            public void run() {
                loadingProgress.setVisible(true);
                storage.waitImagesUpload(files.size());
                loadingProgress.setVisible(false);
            }
        }.start();
    }
}
