package operator;

import io.reactivex.Observable;

import java.util.concurrent.TimeUnit;

public class ZipExample {

    public static void main(String[] args) throws Exception{
        Observable<Long> observable1 = Observable.interval(200L, TimeUnit.MILLISECONDS)
                .take(4);

        Observable<Long> observable2 = Observable.interval(400L, TimeUnit.MILLISECONDS)
                .take(6);

        Observable.zip(observable1, observable2, (data1, data2) -> data1 + data2)
                .subscribe(System.out::println);

        Thread.sleep(3000L);
    }
}
