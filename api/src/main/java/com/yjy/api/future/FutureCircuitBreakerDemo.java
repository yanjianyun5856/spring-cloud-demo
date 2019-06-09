package com.yjy.api.future;

import java.util.Random;
import java.util.concurrent.*;

/**
 * 通过Future 实现 服务熔断
 */
public class FutureCircuitBreakerDemo {

    public static void main(String[] args) throws InterruptedException, ExecutionException {

        ExecutorService executorService = Executors.newSingleThreadExecutor();

        RandomCommand command = new RandomCommand();

       /* Future<String> future = executorService.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return command.run();
            }
        });
*/
        Future<String> future = executorService.submit(command::run);
        String result = null;
        // 100 毫秒超时时间
        try {
            result = future.get(100, TimeUnit.MILLISECONDS);
        } catch (TimeoutException e) {
            //调用 fallback 方法
            result = command.fallback();
        }

        System.out.println(result);
        executorService.shutdown();
    }

    private static final Random random = new Random();

    public static class RandomCommand implements Command<String> {

        @Override
        public String run() throws InterruptedException {
            int executeTime = random.nextInt(200);

            System.out.println("execute time :"+ executeTime);

            Thread.sleep(executeTime);
            return "hello world";
        }

        @Override
        public String fallback() {
            return "fallback";
        }
    }

    public interface Command<T> {

        /**
         * 正常执行并返回结果
         * @return
         */
        T run() throws Exception;

        /**
         * 错误时，返回错误结果
         * @return
         */
        T fallback();
    }

}
