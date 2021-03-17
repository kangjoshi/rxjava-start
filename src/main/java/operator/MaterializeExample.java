package operator;

import io.reactivex.Observable;

import java.util.concurrent.TimeUnit;

public class MaterializeExample {

    public static void main(String[] args) throws Exception {
        Observable.just(1, 2, 3, 4, 5, 6)
                .materialize()
                .subscribe(
                        notification -> {
                            String notificationType = notification.isOnNext() ? "OnNext : " : notification.isOnError() ? "onError" : "onComplte";
                            System.out.println(notificationType + " : " + notification.getValue());
                        }
                );

        Thread.sleep(5000L);
    }



}
