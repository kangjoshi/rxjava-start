package operator;

import io.reactivex.Observable;

import java.util.Arrays;
import java.util.List;

public class TakeExample {

    public static void main(String[] args) {
        Observable.just("a", "b", "c")
                .take(1)
                .subscribe(
                        System.out::println,
                        System.err::println,
                        () -> System.out.println("complete")
                );
    }



}
