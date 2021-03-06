package operator;

import io.reactivex.Observable;

import java.util.Arrays;
import java.util.List;

public class MapExample {

    public static void main(String[] args) {
        List<Integer> oddList = Arrays.asList(1, 3, 5, 7);

        Observable.fromIterable(oddList)
                .map(num -> "1을 더한 결과:" + (num + 1))
                .subscribe(data -> System.out.println(data));


    }

}
