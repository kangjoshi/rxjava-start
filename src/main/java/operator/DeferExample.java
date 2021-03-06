package operator;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Observer;

import java.time.LocalTime;
import java.util.concurrent.TimeUnit;

public class DeferExample {

    public static void main(String[] args) throws Exception {
        Observable<LocalTime> observableDefer = Observable.defer(() -> Observable.just(LocalTime.now()));
        Observable<LocalTime> observableJust = Observable.just(LocalTime.now());

        observableDefer.subscribe(time -> System.out.println("defer() 구독1의 구독 시간 : " + time));
        observableJust.subscribe(time -> System.out.println("defer() 구독1의 구독 시간 : " + time));

        Thread.sleep(3000);

        observableDefer.subscribe(time -> System.out.println("defer() 구독2의 구독 시간 : " + time));
        observableJust.subscribe(time -> System.out.println("defer() 구독2의 구독 시간 : " + time));
    }

}
