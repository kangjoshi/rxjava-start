package operator;

import io.reactivex.Flowable;

import java.util.concurrent.TimeUnit;

public class RangeExample {

    public static void main(String[] args) throws Exception {
        Flowable.range(0, 100)
                .map(num -> num + " count")
                .subscribe(System.out::println);

        //Thread.sleep(3000L);    // interval은 별도의 Thread에서 생성되므로 main 메서드가 종료되지 않도록 sleep
    }

}
