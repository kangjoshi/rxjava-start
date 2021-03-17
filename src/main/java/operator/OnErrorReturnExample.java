package operator;

import io.reactivex.Observable;
import io.reactivex.Observer;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class OnErrorReturnExample {

    public static void main(String[] args) throws Exception {
        Observable.just(5)
                .flatMap(
                        num -> Observable.interval(200L, TimeUnit.MILLISECONDS)
                                .take(5)
                                .map(i -> num / i)
                                .onErrorReturn(exception -> {
                                    if (exception instanceof ArithmeticException)
                                        System.err.println("계산 처리 에러 발생");

                                    return -1L;
                                })
                ).subscribe(System.out::println, System.err::println, () -> System.out.println("complete"));


        Thread.sleep(1000L);
    }



}
