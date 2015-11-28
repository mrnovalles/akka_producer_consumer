### Intention
What's best in akka?
 - Spawning N actors to process N messages?
 - Spawning M actors using the akka routers and send them N messages?

Results on running java version 1.7.0_79 an i5 Macbook Pro with 2 cores.

### Actor producer (N actors N messages)

```
Actor producer
Terminated after 1000 messages. 1115 ms
```

### Messages based (N actors M messages, where M > N)

```
Smallest inbox (5)
Terminated after 1000 messages. 571 ms

Smallest inbox (10)
Terminated after 1000 messages. 722 ms

Smallest inbox(2)
Terminated after 1000 messages. 561 ms

Round Robin(2)
Terminated after 1000 messages. 519 ms
```
