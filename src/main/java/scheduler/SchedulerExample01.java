package scheduler;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

import java.util.logging.Logger;

public class SchedulerExample01 {

    public static void main(String[] args) throws Exception {
        Observable<String> observable = Observable.just("1", "2", "3", "4", "5");

        observable
                .subscribeOn(Schedulers.io())
                .map(data -> "$$" + data + "$$")
                .subscribe(data -> System.out.println(Thread.currentThread().getName() +" : "+ data));

        observable
                .subscribeOn(Schedulers.computation())
                .map(data -> "##" + data + "##")
                .subscribe(data -> System.out.println(Thread.currentThread().getName() +" : "+ data));

        Thread.sleep(5000);
    }


}
