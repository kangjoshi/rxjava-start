package operator;

import io.reactivex.Observable;

import java.util.Arrays;
import java.util.List;

public class ConcatMapExample {

    public static void main(String[] args) {
        List<Integer> oddList = Arrays.asList(1, 3, 5, 7);

        ConcatMapExample flatMapExample = new ConcatMapExample();

        flatMapExample.type01().subscribe(data -> System.out.println(data));
        System.out.println("==========");
        flatMapExample.type02().subscribe(data -> System.out.println(data));

    }

    private Observable<String> type01() {
       return Observable.range(2, 1)
                .flatMap(data -> Observable.range(1, 9)
                        .map(range -> data + " * " + range + " = " + (data*range)));
    }

    private Observable<String> type02() {

        return Observable.range(2, 1)
                .flatMap(
                        data -> Observable.range(1, 9),
                        (sourceData, transformedData) ->
                                sourceData + " * " + transformedData + " = " + (sourceData*transformedData)
                );
    }

}
