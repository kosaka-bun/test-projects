# AbstractQueuedSynchronizer

## 1 简介
```
AQS 的全称为 `AbstractQueuedSynchronizer` ，翻译过来的意思就是抽象队列同步器。这个类在 `java.util.concurrent.locks` 包下面。

AQS 就是一个抽象类，主要用来构建锁和同步器。

AQS 为构建锁和同步器提供了一些通用功能的是实现，因此，使用 AQS 能简单且高效地构造出应用广泛的大量的同步器，
比如我们提到的 `ReentrantLock`，`Semaphore`，其他的诸如 `ReentrantReadWriteLock`，`SynchronousQueue`，
`FutureTask`(jdk1.7) 等等皆是基于 AQS 的。
```

AQS是一个提供了状态量和挂起排队算法的一个抽象类，java中预置的许多锁的实现都基于它。它本身不定义一个锁，只负责利用实现类提供的方法去尝试获取指定数量的“许可”（即[acquire](./AbstractQueuedSynchronizer.java#L1238)等方法的参数"arg"），若获取不成功，则将发起请求的线程包装到一个[Node](./AbstractQueuedSynchronizer.java#L394)节点中，加入由AQS维护的链表中，用一定的算法（不断判断、阻塞）排队尝试获取“许可”。

## 2 ReentrantLock
### 2.1 加锁
以ReentrantLock为例，了解AQS的基本工作原理。
```java
ReentrantLock reentrantLock = new ReentrantLock();
reentrantLock.lock();
```
[ReentrantLock.lock()](./ReentrantLock.java#L266)
```java
public ReentrantLock(boolean fair) {
    sync = fair ? new FairSync() : new NonfairSync();
}

public void lock() {
    sync.acquire(1);
}
```
此处的acquire即为AQS中的[acquire](./AbstractQueuedSynchronizer.java#L1238)方法。
```java
public final void acquire(int arg) {
    if (!tryAcquire(arg) &&
        acquireQueued(addWaiter(Node.EXCLUSIVE), arg))
        selfInterrupt();
}
```
首先调用了tryAcquire方法，ReentrantLock对其的实现是：
```java
//非公平锁
protected final boolean tryAcquire(int acquires) {
    return nonfairTryAcquire(acquires);
}

@ReservedStackAccess
final boolean nonfairTryAcquire(int acquires) {
    final Thread current = Thread.currentThread();
    int c = getState();
    if (c == 0) {
        if (compareAndSetState(0, acquires)) {
            //设置当前线程为这个对象的独占式持有线程
            setExclusiveOwnerThread(current);
            return true;
        }
    }
    else if (current == getExclusiveOwnerThread()) {
        int nextc = c + acquires;
        if (nextc < 0) // overflow
            throw new Error("Maximum lock count exceeded");
        setState(nextc);
        return true;
    }
    return false;
}

//公平锁
@ReservedStackAccess
protected final boolean tryAcquire(int acquires) {
    final Thread current = Thread.currentThread();
    int c = getState();
    if (c == 0) {
        if (!hasQueuedPredecessors() &&
            compareAndSetState(0, acquires)) {
            setExclusiveOwnerThread(current);
            return true;
        }
    }
    else if (current == getExclusiveOwnerThread()) {
        int nextc = c + acquires;
        if (nextc < 0)
            throw new Error("Maximum lock count exceeded");
        setState(nextc);
        return true;
    }
    return false;
}
```
可以看到，相对于非公平锁，公平锁仅仅只是多在获取锁之前执行了`!hasQueuedPredecessors()`，这个方法的文档说明为：
```
查询是否有任何线程等待获取的时间超过当前线程。
调用此方法等效于（但可能比其更高效）：
getFirstQueuedThread() != Thread.currentThread() && hasQueuedThreads()
返回值：
如果当前线程之前有一个排队线程，则为true ，如果当前线程位于队列的头部或队列为空，则为false。
```
即，在公平模式下，在尝试获取锁前，会先判断AQS队列中是否存在某些线程的等待时间比当前线程更长，如果有，则tryAcquire直接返回false。

回到[acquire](./AbstractQueuedSynchronizer.java#L1238)方法中。
```java
public final void acquire(int arg) {
    if (!tryAcquire(arg) &&
        acquireQueued(addWaiter(Node.EXCLUSIVE), arg))
        selfInterrupt();
}
```
如果tryAcqiure方法返回false，则下一步将调用[addWaiter](./AbstractQueuedSynchronizer.java#L650)方法。
```java
/**
 * mode有以下两个值可选，用于表示当前节点以什么模式在获取锁：
 * static final Node SHARED = new Node();
 * static final Node EXCLUSIVE = null;
 * 
 * 该方法将当前线程加到等待队列的尾部
 */
private Node addWaiter(Node mode) {
    //以当前线程创建节点
    Node node = new Node(mode);

    for (;;) {
        Node oldTail = tail;
        //若存在现有的等待队列
        if (oldTail != null) {
            //将当前线程节点的prev指针设置为oldTail
            node.setPrevRelaxed(oldTail);
            //将当前线程节点设置为tail
            if (compareAndSetTail(oldTail, node)) {
                //之前的tail节点的next设置为当前线程节点
                oldTail.next = node;
                return node;
            }
        } else {
            initializeSyncQueue();
        }
    }
}

//in class Node
Node(Node nextWaiter) {
    this.nextWaiter = nextWaiter;
    THREAD.set(this, Thread.currentThread());
}

//in class Node
final void setPrevRelaxed(Node p) {
    //PREV = l.findVarHandle(Node.class, "prev", Node.class);
    PREV.set(this, p);
}

//初始化等待队列，将head与tail均设置为一个新的空节点
private final void initializeSyncQueue() {
    Node h;
    //HEAD = l.findVarHandle(AbstractQueuedSynchronizer.class, "head", Node.class);
    if (HEAD.compareAndSet(this, null, (h = new Node())))
        tail = h;
}

//in class Node
Node() {}
```
[addWaiter](./AbstractQueuedSynchronizer.java#L650)方法调用完成后，当前线程节点已经被加入到等待队列的尾部了，随后调用[acquireQueued](./AbstractQueuedSynchronizer.java#L906)方法。
```java
//该方法用于循环判断和等待获取锁的条件
//返回值表示获取到锁后，线程是否处于打断状态
final boolean acquireQueued(final Node node, int arg) {
    boolean interrupted = false;
    try {
        for (;;) {
            //获取前驱结点
            final Node p = node.predecessor();
            //判断前驱结点是否是头结点
            if (p == head && tryAcquire(arg)) {
                //当前线程节点获取到了“许可”，成为头节点
                setHead(node);
                //原头节点移出队列
                p.next = null; // help GC
                return interrupted;
            }
            //当前节点不是第二个节点，无权尝试获取“许可”，或者获取失败
            if (shouldParkAfterFailedAcquire(p, node))
                //阻塞当前线程，并在阻塞结束后判断当前线程是否已被打断
                interrupted |= parkAndCheckInterrupt();
            //循环进行判断和尝试获取锁
            //除非tryAcquire方法返回true，或抛出异常，此循环都不会结束
        }
    } catch (Throwable t) {
        cancelAcquire(node);
        if (interrupted)
            selfInterrupt();
        throw t;
    }
}

private void setHead(Node node) {
    head = node;
    node.thread = null;
    node.prev = null;
}

/**
 * 判断当前线程节点（node）是否应当阻塞
 * pred == node.prev恒成立。
 * 返回值表示当前线程是否应当阻塞。
 */
private static boolean shouldParkAfterFailedAcquire(Node pred, Node node) {
    /*
     * waitStatus有数个取值：
     * static final int CANCELLED =  1;
     * static final int SIGNAL    = -1;
     * static final int CONDITION = -2;
     * static final int PROPAGATE = -3;
     * 以及默认的0值。
     * 
     * 通常会通过判断waitStatus是否大于0，来判断当前节点对应的线程是否已取消排队。
     * waitStatus为-1时，表示这个节点的后继节点已经阻塞或即将被阻塞。
     * waitStatus为0时，表示当前节点没有任何特殊状态。
     */
    int ws = pred.waitStatus;
    if (ws == Node.SIGNAL)
        //前驱节点的waitStatus为-1，但当前线程却未阻塞，则返回true。
        return true;
    //此后都返回false
    if (ws > 0) {
        //前驱节点是已被取消的节点
        //将当前线程节点的prev指针指向前驱节点的前驱节点，然后再次进行相同的判断。
        //直到前驱节点不再是无效节点
        do {
            node.prev = pred = pred.prev;
        } while (pred.waitStatus > 0);
        //维护正确的双链表结构
        pred.next = node;
        //返回false，使调用方（acquireQueued方法）立即再执行一次循环，判断当前线程节点是不是头节点
    } else {
        //当前节点应该被阻塞，但是前驱节点的waitStatus却不为-1，则先设为-1
        pred.compareAndSetWaitStatus(ws, Node.SIGNAL);
        //思考：此处为什么不返回true使当前线程直接进入阻塞状态？
    }
    return false;
}

//阻塞当前线程，并在阻塞后判断当前线程是否被打断
private final boolean parkAndCheckInterrupt() {
    //this参数表示blocker，用于保存在当前线程的thread对象中
    //供其他使用这个thread对象的对象了解当前线程是被谁挂起了
    LockSupport.park(this);
    return Thread.interrupted();
}

//取消排队
private void cancelAcquire(Node node) {
    if (node == null)
        return;
    node.thread = null;
    Node pred = node.prev;
    while (pred.waitStatus > 0)
        node.prev = pred = pred.prev;
    Node predNext = pred.next;
    node.waitStatus = Node.CANCELLED;
    if (node == tail && compareAndSetTail(node, pred)) {
        pred.compareAndSetNext(predNext, null);
    } else {
        int ws;
        if (pred != head &&
            ((ws = pred.waitStatus) == Node.SIGNAL ||
                (ws <= 0 && pred.compareAndSetWaitStatus(ws, Node.SIGNAL))) &&
            pred.thread != null) {
            Node next = node.next;
            if (next != null && next.waitStatus <= 0)
                pred.compareAndSetNext(predNext, next);
        } else {
            //唤醒节点的后继者（如果存在）
            unparkSuccessor(node);
        }
        node.next = node; // help GC
    }
}
```
可以看出，任何情况下，头节点都仅仅只是用于表示后继节点状态的节点，不参与线程等待。

回到[acquire](./AbstractQueuedSynchronizer.java#L1238)方法中。
```java
public final void acquire(int arg) {
    if (!tryAcquire(arg) &&
        acquireQueued(addWaiter(Node.EXCLUSIVE), arg))
        selfInterrupt();
}
```
当[acquireQueued](./AbstractQueuedSynchronizer.java#L906)方法执行完成后，acquire方法执行完成。如果正常返回，表示当前线程已正确获取到锁，若抛出异常，表示获取锁的过程中出现了问题。

在没有获取到锁的情况下，方法不会结束。

此处有一个存疑说法：
> [acquire](./AbstractQueuedSynchronizer.java#L1238)方法中的`selfInterrupt()`方法其实是多余的。
```java
static void selfInterrupt() {
    Thread.currentThread().interrupt();
}
```
> 因为执行它时，tryAcquire返回false，acquireQueued返回true，而仅在当前线程已经被打断的情况下，acquireQueued才会返回true，在已经被打断的情况下再执行一次打断是没有意义的。<br>
同理，acquire方法中catch块中的[`if (interrupted)`](./README.md#L188)判断也是无意义的。

思考：线程被打断后还存在被取消打断的可能吗？或者说开发规范允许这么做吗？

### 2.2 解锁
同样以ReentrantLock为例。
```java
public void unlock() {
    sync.release(1);
}
```
[AbstractQueuedSynchronizer.release()](./AbstractQueuedSynchronizer.java#L1301)

此处的arg参数同样是“许可”的含义。
```java
//返回值表示锁是否被彻底释放
public final boolean release(int arg) {
    //尝试释放，然后判断锁是否彻底释放
    if (tryRelease(arg)) {
        //在锁彻底释放后，判断是否有等待队列
        /*
         * 尝试唤醒头节点的后继者（如果存在）
         * 有两种情况，会导致锁在刚刚释放后，头节点的waitStatus就为0，此时不需要唤醒头节点的后继者。
         *
         * 第一种情况：
         * 当前线程节点是队列中唯一一个节点，waitStatus为0，锁被释放前，某线程A刚刚进入队列，
         * 随即锁被释放，线程A还没来得及被阻塞。
         * 此时，当前线程节点的waitStatus已没有再被修改的可能，而后继节点也并非处于阻塞状态，因此不需要唤醒。
         * 
         * 第二种情况：
         * 当前线程节点是等待列表中唯一一个节点，某线程A在tryRelease方法完成前极短的时间内，
         * 申请获取线程锁，申请失败，它通过addWaiter方法进入了排队队列，然后进入
         * acquireQueued方法，尝试获取锁。
         * 此时，它的前驱节点是头节点，可以执行tryAcquire方法，在它执行tryAcquire方法前，
         * 这里的tryRelease方法刚好完成执行，线程A执行tryAcquire方法就可以成功，
         * 并且其对应的节点可成为等待队列的头节点。
         * 由于指令重排，有极小的概率会使得在此处获取头节点时，线程A对应的节点就已经成为了头节点，
         * 它的waitStatus应当为0，并且应当不处于阻塞状态。
         * 故在头节点的waitStatus为0的情况下，即有可能出现了上述情况，此时不应当唤醒
         * 头节点的后继节点。
         */
        Node h = head;
        if (h != null && h.waitStatus != 0)
            unparkSuccessor(h);
        return true;
    }
    return false;
}
```
[tryRelease](./ReentrantLock.java#L146)在ReentrantLock中的实现：

由于是独占锁，所以在释放锁的时候并不需要担心修改state时的线程安全问题。

需要注意的是，tryRelease方法并不会将当前线程节点从等待队列中移出，它仍是队列的头节点。
```java
@ReservedStackAccess
protected final boolean tryRelease(int releases) {
    //减去许可数量
    int c = getState() - releases;
    if (Thread.currentThread() != getExclusiveOwnerThread())
        throw new IllegalMonitorStateException();
    //判断锁是否已被完全释放
    boolean free = false;
    if (c == 0) {
        free = true;
        //取消对独占线程的持有
        setExclusiveOwnerThread(null);
    }
    setState(c);
    return free;
}
```
参考ReentrantReadWriteLock中tryReleaseShared的实现：
其中对state的修改便使用了CAS。
```java
@ReservedStackAccess
protected final boolean tryReleaseShared(int unused) {
    Thread current = Thread.currentThread();
    if (firstReader == current) {
        // assert firstReaderHoldCount > 0;
        if (firstReaderHoldCount == 1)
            firstReader = null;
        else
            firstReaderHoldCount--;
    } else {
        HoldCounter rh = cachedHoldCounter;
        if (rh == null ||
            rh.tid != LockSupport.getThreadId(current))
            rh = readHolds.get();
        int count = rh.count;
        if (count <= 1) {
            readHolds.remove();
            if (count <= 0)
                throw unmatchedUnlockException();
        }
        --rh.count;
    }
    for (;;) {
        int c = getState();
        int nextc = c - SHARED_UNIT;
        if (compareAndSetState(c, nextc))
            // Releasing the read lock has no effect on readers,
            // but it may allow waiting writers to proceed if
            // both read and write locks are now free.
            return nextc == 0;
    }
}
```
release方法执行完成后，即完成了一次解锁操作，若锁已被完全释放，则唤醒队列中的下一个排队的有效节点（waitStatus <= 0的节点）。
