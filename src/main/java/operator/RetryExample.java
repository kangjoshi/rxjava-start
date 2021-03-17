package operator;

import io.reactivex.Observable;

import java.util.concurrent.TimeUnit;

public class RetryExample {

    public static void main(String[] args) throws Exception {
        Observable.just(5)
                .flatMap(
                        num -> Observable.interval(200L, TimeUnit.MILLISECONDS)
                                .take(5)
                                .map(i -> {
                                    long result;
                                    try {
                                        result = num / i;
                                    } catch (ArithmeticException exception) {
                                        System.err.println(exception.getMessage());
                                        throw  exception;
                                    }
                                    return result;
                                })
                                .retry((retryCount, ex) -> {
                                    System.err.println("재시도 횟수 : " + retryCount);
                                    Thread.sleep(1000L);
                                    return retryCount < 5 ? true : false;
                                })
                                .onErrorReturn(throwable -> -1L)

                ).subscribe(System.out::println, System.err::println, () -> System.out.println("complete"));


        Thread.sleep(5000L);
    }



}
