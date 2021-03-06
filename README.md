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