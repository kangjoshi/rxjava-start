import io.reactivex.BackpressureOverflowStrategy;
import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;

import java.util.concurrent.TimeUnit;

public class BackpressureBufferExample02 {

    public static void main(String[] args) throws Exception {
        System.out.println("### start : " + System.currentTimeMillis());

        Flowable.interval(300L, TimeUnit.MILLISECONDS)
                .doOnNext(data -> System.out.println("#interval doOnNext()" + data))
                .onBackpressureBuffer(
                        2,
                        () -> System.out.println("over flow"),
                        BackpressureOverflowStrategy.DROP_OLDEST)
                .doOnNext(data -> System.out.println("onBackpressureBuffer doOnNext()" + data))
                .observeOn(Schedulers.computation(), false, 1) // 소비자에서 처리하는 스레드를 별도로 줌
                .subscribe(
                        data -> {
                            Thread.sleep(1000L);
                        },
                        error -> System.err.println(error)
                );

        Thread.sleep(2800L);

    }

}
