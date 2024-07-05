import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ClientMain {

    public static void main(String[] args) throws InterruptedException {
        ReentrantLock lock = new ReentrantLock(true);
        Condition condition = lock.newCondition();
        int serverPortNumber = 8095;
        int requestedTicket = 0;
        /*Client 'ın ilk önce reader işlemini yapması için proccessOrder tipini 0 eğer ilk writer işlemini
        yapmasını istiyorsam 1 gönderiyorum bileti önce alıp sonra iptal etmesini ve bilet listesini okumasını istiyorsam
         processOrder olarak 2 ' yi gönderiyorum
        */
        Client client1 = new Client(lock, condition, 1, 7500, serverPortNumber, requestedTicket, 1);
        Client client2 = new Client(lock, condition, 2, 7600, serverPortNumber, requestedTicket, 0);
        Client client3 = new Client(lock, condition, 3, 7700, serverPortNumber, requestedTicket, 0);

        Thread thread1 = new Thread(client1);
        Thread thread2 = new Thread(client2);
        Thread thread3 = new Thread(client3);
        thread1.start();
        thread2.start();
        thread3.start();

        thread1.join();
        thread2.join();
        thread3.join();

    }
}