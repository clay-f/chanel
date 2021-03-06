package com.f.java.concurrent;

import com.f.java.base.util.Generator;

import java.util.concurrent.*;
import java.util.*;

/**
 * ExchangeProducer 生产一个创建很消费时间的 list, 然后将这个列表交换为 exchangeConsumer
 *
 * @param <T>
 */
class ExchangerProducer<T> implements Runnable {
    private Generator<T> generator;
    private Exchanger<List<T>> exchanger;
    private List<T> holder;

    ExchangerProducer(Exchanger<List<T>> exch, Generator<T> gen, List<T> holder) {
        exchanger = exch;
        generator = gen;
        this.holder = holder;
    }

    public void run() {
        try {
            while (!Thread.interrupted()) {
                for (int i = 0; i < ExchangerDemo.size; i++)
                    holder.add(generator.next());
                // Exchange full for empty:
                holder = exchanger.exchange(holder);
            }
        } catch (InterruptedException e) {
            // OK to terminate this way.
        }
    }
}

class ExchangerConsumer<T> implements Runnable {
    private Exchanger<List<T>> exchanger;
    private List<T> holder;
    private volatile T value;

    ExchangerConsumer(Exchanger<List<T>> ex, List<T> holder) {
        exchanger = ex;
        this.holder = holder;
    }

    public void run() {
        try {
            while (!Thread.interrupted()) {
                System.out.println("开始等待获取的值...\n当前的数量为: " + holder.size());
                holder = exchanger.exchange(holder);
                System.out.println("交换值完毕，当前的数量为: " + holder.size());
                for (T x : holder) {
                    value = x; // Fetch out value
                    System.out.println(value);
                    holder.remove(x); // OK for CopyOnWriteArrayList
                }
            }
        } catch (InterruptedException e) {
            // OK to terminate this way.
        }
        System.out.println("Final value: " + value);
    }
}

public class ExchangerDemo {
    static int size = 10;
    static int delay = 5; // Seconds

    public static void main(String[] args) throws Exception {
        ExecutorService exec = Executors.newCachedThreadPool();
        Exchanger<List<Fat>> xc = new Exchanger<>();
        List<Fat> producerList = new CopyOnWriteArrayList<>(), consumerList = new CopyOnWriteArrayList<>();
        exec.execute(new ExchangerProducer<>(xc, BasicGenerator.create(Fat.class), producerList));
        exec.execute(new ExchangerConsumer<>(xc, consumerList));
        TimeUnit.SECONDS.sleep(1);
        exec.shutdownNow();
    }
} /* Output: (Sample)
Final value: Fat id: 29999
*///:~
