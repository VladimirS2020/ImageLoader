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
            } // т.к. будет загружено 2 картинки, то мы ожидаем, что 1-ая картинка загрузится сначала на сервер main, а потом сервер additional
        } // а 2-ая картинка наоборот сначала должна загрузиться на сервер additional, а потом на сервер main
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
