package com.yjy.api.rxjava;

import rx.Observable;
import rx.Scheduler;
import rx.Single;
import rx.schedulers.Schedulers;

import java.util.Arrays;
import java.util.List;

/**
 * RxJava1.x
 */
public class RxJavaDemo {

    public static void main(String[] args) throws InterruptedException {

        //demoSingle();
        System.out.printf("线程： %s ;  \n",Thread.currentThread().getName());
        //demoObservable();
        demoStandardReactive();
    }

    public static void demoStandardReactive() throws InterruptedException {
        List<Integer> values = Arrays.asList(1,2,3);
        Observable.from(values) //发布多个数据
                .subscribeOn(Schedulers.newThread()) //在IO 线程执行
                .subscribe(value ->{
                   if (value > 2)
                        throw new IllegalStateException("数据不应该大于2");

                    //消费数据
                    println("消费数据"+value);

                }, e ->{
                    //异常情况
                    println("发生异常"+e.getMessage());
                }, () -> {
                    //整体流程完成时
                    println("流程完成");
                })
        ;

        Thread.sleep(100);
    }

    public static void demoObservable() throws InterruptedException {
        List<Integer> values = Arrays.asList(1,2,3,4,5,6,7,8,9);

        Observable.from(values) //发布多个数据
                .subscribeOn(Schedulers.io()) //在IO 线程执行
                .subscribe(RxJavaDemo::println)//订阅并消费
        ;

        Thread.sleep(100);
    }

    public static void demoSingle() {
        Single.just("hello world")//仅能发布单个数据
                .subscribeOn(Schedulers.io()) //在IO 线程执行
                .subscribe(RxJavaDemo::println)//订阅并消费
        ;

    }

    private static void println(Object value){
        System.out.printf("线程： %s;数据：%s \n",Thread.currentThread().getName(),value);
    }

}
