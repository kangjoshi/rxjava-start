### Flowable - Observable 비교
1. Flowable
    - Reactive Streams 인터페이스를 구현
    - Subscriber에서 데이터를 처리
    - 배압 기능 제공
    - Subscription으로 구독을 해지

2. Observable
    - Reactive Streams 인터페이스를 구현하지 않음
    - Observer에서 데이터 처리
    - 배압 기능 제공하지 않음
    - Disposable로 구독을 해지

###### 배압(Back Pressure)이란? 
- 데이터 통지량 제어
- `BackpressureStategy`를통해 배압 전략 제공 (배압 기능을 가지고 있는 Flowable만 가능)
    - MISSING : 배압을 적용하지 않는다.
    - ERROR : 통지된 데이터가 버퍼의 크기를 초과하면 `MissingBackpressureException`에러 발생
    - BUFFER_DROP_LATEST : 버퍼가 가득차면 버퍼내에서 가장 최근에 버퍼로 들어온 데이터(바로 이전 데이터)를 DROP
    - BUFFER_DROP_OLDEST : 버퍼가 가득차면 버퍼내에서 가장 먼저(오래전) 들어온 데이터를 DROP
    - DROP : 버퍼에 데이터가 모두 채워진 상태가 되면 이후 생성되는 데이터를 DROP
    
### Single, Maybe, Completable
1. Single
    - 데이터 1건만 통지하거나 에러를 통지한다
    - 데이터 통지 자체가 완료를 의미하므로 완료 통지는 하지 않는다
    - onNext(), onComplete()가 없으며 둘을 합한 onSuccess()를 제공한다
2. Maybe
    - 데이터를 1건만 통지하거나 1건도 통지하지 않고 완료 또는 에러를 통지한다
    - 데이터 통지 자체가 완료를 의미하므로 완료 통지는 하지 않는다
    - 단, 데이터를 1건도 통지하지 않고 처리가 종료될 경우에는 완료 통지를 한다
3. Completable
    - 데이터 생산자이지만 데이터를 통지하지 않고 완료 또는 에러를 통지한다
    - 통지의 역할 대신 완료만 통지할 때 사용    
    
### 연산자
- 연산자를 이용하여 데이터를 생성하고 통지하는 Flowable이나 Observable등의 생산자를 생성할 수 있다
- Flowable이나 Observable에서 통지한 데이터를 다양한 연산자를 사용하여 가공 처리하여 결과값을 만들어 낸다

###### Flowable / Observable 생성연산자
1. interval
    - 지정한 시간 간격마다 통지되고 완료 없이 계속 통지한다
    - `initialDelay` 파라미터를 이용해서 최초 통지에 대한 대기 시간을 지정할 수 있다
    - 호출한 스레드와는 별도의 스레드에서 실행된다
    - polling 용도의 작업을 수행할 때 활용할 수 있다
    - ```java
      Flowable.interval(0, 1000L, TimeUnit.MILLISECONDS) // initialDelay, period(대기 시간), 대기 시간 단위
          .map(num -> num + " count")
          .subscribe(System.out::println);

      Thread.sleep(3000L);    // interval은 별도의 Thread에서 생성되므로 main 메서드가 종료되지 않도록 sleep
       ```
2. range
    - 지정한 값부터 m개의 숫자를 통지한다
    - for, while 등의 반복문을 대체할 수 있다
    - ```java
      Flowable.range(0, 100)
          .map(num -> num + " count")
          .subscribe(System.out::println);
      ```
3. timer
    - 지정한 시간이 지나면 0을 통지하고 onComplete 이벤트가 발생하여 종료한다
    - 호출한 스레드와는 별도의 스레드에서 실행된다
    - 특정 시간을 대기한 후 어떤 처리를 하고자 할 때 활용
    - ```java
      Flowable.timer(1000, TimeUnit.MILLISECONDS)
          .map(count -> "hello")
          .subscribe(System.out::println);

      Thread.sleep(3000L);    // timer 별도의 Thread에서 생성되므로 main 메서드가 종료되지 않도록 sleep
      ```
4. defer
    - 구독이 발생할 때마다 새로운 Observable을 생성한다
    - 선언한 시점의 데이터를 통지하는것이 아니라 구독된 시점에 생성된 데이터를 통지한다
    - 데이터 생성을 미루는 효과가 있기때문에 최신 데이터를 얻고자할 때 활용
    - ```java
      Observable<LocalTime> observableDefer = Observable.defer(() -> Observable.just(LocalTime.now()));
      Observable<LocalTime> observableJust = Observable.just(LocalTime.now());
    
      observableDefer.subscribe(time -> System.out.println("defer() 구독1의 구독 시간 : " + time)); // defer() 구독1의 구독 시간 : 01:56:07.634982
      observableJust.subscribe(time -> System.out.println("defer() 구독1의 구독 시간 : " + time)); // defer() 구독1의 구독 시간 : 01:56:07.628274
    
      Thread.sleep(3000);
    
      observableDefer.subscribe(time -> System.out.println("defer() 구독2의 구독 시간 : " + time)); // defer() 구독2의 구독 시간 : 01:56:10.652844
      observableJust.subscribe(time -> System.out.println("defer() 구독2의 구독 시간 : " + time)); // defer() 구독2의 구독 시간 : 01:56:07.628274
      ```
5. fromIterable
    - Iterable 인터페이스를 구현한 클래스를 파라미터로 받아 데이터를 순서대로 통지한다
6. fromFuture
    - Future 인터페이스를 구현한 클래스를 파라미터로 받아 데이터를 통지한다
    
###### 데이터 필터링 연산자
1. filter
    - Predicate를 파라미터로 받아 결과가 ture인 데이터만 통지한다
2. distinct
    - 이미 통지된 동일한 데이터가 있다면 이후의 동일한 데이터는 통지하지 않는다
    - 통지되는 데이터 유형이 오브젝트이면 파라미터로 특정 필드만 중복 제거하도록 지정할 수 있다.
3. take
    - 파라미터로 지정한 개수나 시간이 될 때까지 통지한다
    - 지정한 개수가 모두 통지되면 완료가 통지된다
    - ```java
      Observable.just("a", "b", "c")
          .take(1)
          .subscribe(
              System.out::println,
              System.err::println,
              () -> System.out.println("complete")
      );
    ```
4. takeUntil
    - 파라미터로 지정한 조건이 true가 될 때까지 데이터를 통지한다
    - 파라미터가 Observable 유형이라면 지정한 Observable이 최초 데이터를 통지할 때까지 데이터를 계속 통지한다 (`Observable.timer()를 사용`)
5. skip
    - 파라미터로 지정한 숫자 또는 시간만큼 건너뛰고 통지된다
        
###### 데이터 변환 연산자
1. map
    - 통지되는 데이터를 원하는 값으로 변환 후 통지한다
    - 변환 전, 후 데이터 타입은 달라도 상관없지만 null을 반환해서는 안된다(NPE 발생)
2. flatMap
    - concatMap과 다르게 데이터 처리 순서는 보장하지 않는다.
    1. 첫번째 유형
        - 통지되는 데이터를 원하는 값으로 변환 후 통지한다
        - map은 1:1 변환(하나의 데이터를 받아서 하나를 통지)인 것과 달리 flatMap은 1:N(하나의 데이터를 받아서 여러개를 통지) 변환하므로 데이터 한개로 여러 데이터를 통지할 수 있다
        - map은 변환된 데이터를 반환하지만 flatMap은 변환 된 어러개의 데이터를 담고 있는 새로우 Observable을 반환
    2. 두번째 유형
        - 통지되는 데이터와 변환된 데이터를 조합하여 새로운 데이터를 통지 (원본데이터 + 변환된 데이터 = 최종데이터)
3. concatMap
    - flatMap과 마찬가지로 받은 데이터를 변환하여 새로운 Observable로 반환한다
    - concatMap은 반환된 새로우 Observable을 하나씩 순서대로 실행하여 데이터 처리 순서는 보장하지만 처리중인 Observable의 처리가 끝나야 다음 Observable이 생행되므로 처리 성능에는 영향을 줄 수 있다
4. switchMap
    - concatMap과 마찬가지로 받은 데이터를 변환하여 새로운 Observable로 반환한다
    - switchMap도 concatMap처럼 순서를 보장하지만 새로운 데이터가 통지되면 현재 처리중이던 작업을 바로 중단하고 새로운 작업을 시작한다
    - 키보드 입력에 따른 검색과 같이 이전 데이터의 처리중 새로운 데이터가 들어올 경우 이전 데이터 처리가 의미 없을때 사용하면 성능 향상 효과가 있다
5. groupBy
    - 하나의 Observable을 여러개의 새로운 `GroupedByObservable`로 만든다
    - 각각 그룹으로 묶이는 것이 아닌 각각의 데이터들이 그룹에 해당하는 Key를 가지게 된다 key는 `groupedObservable.getKey()`로 접근 가능
6. toList
    - 통지되는 데이터를 모두 List에 담다 통지한다 - 단건의 리스트이므로 `Single`로 반환된다
7. toMap
    - 통지되는 데이터를 Key & Value 쌍으로 변환되어 Map에 담긴다 - 단건의 Map이므로 `Single`로 반환된다
    - ```java
      String[] alphabets = {"a-Alpha", "b-Beta", "c-Charlie", "d-Delta"};
        
      Single<Map<String, String>> single = Observable.fromArray(alphabets)
                                            .toMap(data -> data.split("-")[0], data -> data.split("-")[1]); // 첫번째 파라미터는 key가 되고 두번째 파라미터가 value가 된다. 만약 두번째 파라미터를 넘기지 않는다면 data가 value가 된다
                
      single.subscribe(System.out::println);
      ```
      
###### 데이터 결합 연산자
1. merge
    - 다수의 Observable에서 통지된 데이터를 받아 하나의 Observable로 통지한다
    - 통지 시점이 빠른 Observable의 데이터부터 순차적으로 통지되고 통지 시점이 같을 경우 merge() 함수의 파라미터로 먼저 지정된 Observable의 데이터부터 통지된다
    - ```java
      Observable<Long> observable1 = Observable.interval(200L, TimeUnit.MILLISECONDS)
                .take(5);

      Observable<Long> observable2 = Observable.interval(400L, TimeUnit.MILLISECONDS)
                .take(5)
                .map(num -> num + 1000);

      Observable.merge(observable1, observable2)
                .subscribe(System.out::println);
      ```
2. concat
    - 다수의 Observable에서 통지된 데이터를 받아 하나의 Observable로 통지한다
    - 통지 시점과 상관 없이 파라미터로 전달된 순서대로 하나의 Observable에서 통지가 끝나면 다음 Observable에서 연이어 통지된다
2. zip
    - 다수의 Observable에서 통지된 데이터를 받아 하나의 Observable로 통지한다
    - 각 Observable에서 통지된 데이터가 모두 모이면 각 Observable에서 동일한 index(순서)의 데이터로 새로운 데이터를 생성한 후 통지한다
    - 통지하는 데이터 개수가 가장 적은 Observable의 통지 시점에 완료 통지 시점을 맞춘다
    - ```java
      Observable<Long> observable1 = Observable.interval(200L, TimeUnit.MILLISECONDS)
                    .take(4);
    
      Observable<Long> observable2 = Observable.interval(400L, TimeUnit.MILLISECONDS)
                    .take(6);
      
      // 가장 적은 통지 횟수를 가진 observable1이 4이므로 4개의 데이터 통지 후 완료된다
      Observable.zip(observable1, observable2, (data1, data2) -> data1 + data2)
                    .subscribe(System.out::println);
    
      Thread.sleep(3000L);
      ```
3. combineLatest
    - 다수의 Observable에서 통지된 데이터를 받아 하나의 Observable로 통지한다
    - 각 Observable에서 데이터를 통지할 때 마다 모든 Observable에서 마지막으로 통지한 각 데이터를 함수형 인터페이스에 전달하고, 새로운 데이터를 생성해 통지한다
    
###### 에러 처리 연산자
1. onErrorReturn
    - 에러가 발생했을  에러를 의미하는 데이터로 대체한다
    - onErrorReturn()을 호출하면 onError 이벤트는 발생하지 않는다
    - ```java
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
                ).subscribe(
                      System.out::println,                    // onNext로 통지 됨 
                      System.err::println,                    // onError는 처리되지 않음
                      () -> System.out.println("complete"));
                Thread.sleep(1000L);
      ```
2. onErrorResumeNext
    - 에러가 발생했을 때 에러를 의미하는 Observable로 대체한다
    - Observable로 대체되기 때문에 추가적인 처리가 가능하다
3. retry
    - 데이터 통지 중 에러가 발생 했을 때, 데이터 통지를 재시도 한다
    - 즉, onError 이벤트가 발생하면 subscribe()를 다시 호출하여 재구독한다
    - 에러가 발생한 시점에 통지에 실패한 데이터만 다시 시도 하는것이 아닌 전체 데이터가 재구독된다
    - ```java
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
                                      Thread.sleep(1000L);    // 재 시도시 지연시간 부여
                                      return retryCount < 5 ? true : false; // 최대 5번 시도 하도록
                                })
                                .onErrorReturn(throwable -> -1L) // 최대 시도 이후에도 계속 실패한다면 에러 처리
                        ).subscribe(System.out::println, System.err::println, () -> System.out.println("complete"));
     
      Thread.sleep(5000L);
      ```
      
###### 유틸리티 연산자
1. delay
    - 설정한 시간 만큼 소바자쪽으로의 데이터 전달(`onNext()`)을 지연한다
2. delaySubscription
    - 설정한 시간 만큼 구독을 미룬다. 즉 소비자가 구독을 해도 구독 시점 자체가 지연된다
3. timeout
    - 각각의 데이터 통지시 지정된 시간안에 통지가 되지 않으면 에러를 통지한다
    - 에러 통지 시 전달되는 에러 객체는 `TimeOutException`이다
4. imteInterval
    - 각각의 데이터가 통지되는데 걸린 시간을 통지한다
5. materialize
    - 통지된 데이터와 통지된 데이터의 통지 타입 자체(메타 데이터가 포함되어 있는)를 Notification 객체에 담고 이 객체를 통지한다
    - ```java
      Observable.just(1, 2, 3, 4, 5, 6)
                    .materialize()
                    .subscribe(
                        notification -> {
                            String notificationType = notification.isOnNext() ? "OnNext : " : notification.isOnError() ? "onError" : "onComplte";
                            System.out.println(notificationType + " : " + notification.getValue());
                        }
                    );
        
      Thread.sleep(5000L);
      ```
6. dematerialize
    - materialize의 반대, 통지 타입이 통지되면 데이터만 통지한다
    - materialize를 사용하여 에러가 발생할 경우 구체적인 처리를 하고 dematerialize를 이용하여 값을 다시 넘기는 방법으로 사용할 수 있다
    
###### 조건과 불린 연산자
1. all
    - 통지된 데이터가 모두 조건에 판단하여 true 또는 false가 통지된다
    - 결과 값을 한번만 통지하면 되기 때문에 Single로 반환된다
2. amb
    - 여러개의 Observable 중에서 최초 통지 시점이 가장 빠른 Observable의 데이터만 통지되고 나머지는 무시된다
3. contains
    - 통지된 데이터중 조건에 해당하는 데이터 여부를 판단하여 true 또는 false가 통지된다
    - 결과 값을 한번만 통지하면 되기 때문에 Single로 반환된다
4. defaultIfEmpty
    - 통지할 데이터가 없을 경우 파라미터로 입력된 값을 통지한다
5. sequenceEqual
    - 두 Observable이 동일한 순서로 동일한 갯수의 같은 데이터를 통지하는지 판단한다


###### 테스트
1. blocking 함수
    - 비동기 처리 결과를 테스트하기 위해 현재 쓰레드는 호출 대상 쓰레드의 실행 결과를 반환 받을 때 까지 대기할 수 있어야 한다.
    1. blockingFirst
        - 생산자가 통지한 첫번째 데이터를 반환
        - 통지된 데이터가 없을 경우 예외가 발생된다

    