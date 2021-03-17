package operator;

import io.reactivex.Observable;
import io.reactivex.Single;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ToMapExample {

    public static void main(String[] args) {
        String[] alphabets = {"a-Alpha", "b-Beta", "c-Charlie", "d-Delta"};

        Single<Map<String, String>> single = Observable.fromArray(alphabets)
                .toMap(data -> data.split("-")[0], data -> data.split("-")[1]); // 반환값은 Map의 key가 되고 data는 value가 된다

        single.subscribe(System.out::println);
    }
}
