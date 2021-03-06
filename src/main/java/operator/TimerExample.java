package operator;

import io.reactivex.Flowable;

import java.util.concurrent.TimeUnit;

public class TimerExample {

    public static void main(String[] args) throws Exception {
        Flowable.timer(1000, TimeUnit.MILLISECONDS)
                .map(count -> "hello")
                .subscribe(System.out::println);

        Thread.sleep(3000L);    // timer 별도의 Thread에서 생성되므로 main 메서드가 종료되지 않도록 sleep
    }

}
