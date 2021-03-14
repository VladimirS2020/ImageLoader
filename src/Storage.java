import java.util.concurrent.atomic.AtomicLong;

public class Storage {
    private int filesUploaded = 0;
    private AtomicLong totalSize = new AtomicLong(0);

    public void uploadToServer(Server mainServer, Server additionalServer) {
        synchronized (mainServer) {
            System.out.println(Thread.currentThread().getId() + ": направление данных на сервер " + mainServer.getName());
            mainServer.upload();
            synchronized (additionalServer) {
                System.out.println(Thread.currentThread().getId() + ": направление данных на сервер " + additionalServer.getName());
                additionalServer.upload();
            } // такие вложенные synchronized блоки лучше не делать, т.к. они могут привести к дэдлоку
        }
    }

    public synchronized void addImage() {
        System.out.println("1 изображение добавлено");
        filesUploaded++;
        notify();
    }

    public void addByte() {
        totalSize.incrementAndGet();
    }

    public synchronized void waitImagesUpload(int expectedFiles) {
        while (filesUploaded < expectedFiles) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Необходимое количество изображений загружено");
        System.out.println("Общий размер загруженных файлов: " + totalSize + " байт(-а)");
    }
}
