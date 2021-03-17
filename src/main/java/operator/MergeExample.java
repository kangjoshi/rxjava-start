package operator;

import io.reactivex.Observable;

import java.util.concurrent.TimeUnit;

public class MergeExample {

    public static void main(String[] args) throws Exception{
        Observable<Long> observable1 = Observable.interval(200L, TimeUnit.MILLISECONDS)
                .take(5);

        Observable<Long> observable2 = Observable.interval(400L, TimeUnit.MILLISECONDS)
                .take(5)
                .map(num -> num + 1000);

        Observable.merge(observable1, observable2)
                .subscribe(System.out::println);

        Thread.sleep(4000);
    }
}
