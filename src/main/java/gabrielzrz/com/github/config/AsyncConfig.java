package gabrielzrz.com.github.config;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author Zorzi
 */
@Configuration
@EnableAsync
public class AsyncConfig implements AsyncConfigurer {

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2); // mantém pelo menos 2 threads vivas, mesmo que estejam ociosas.
        executor.setMaxPoolSize(4); // no máximo 4 threads serão criadas.
        executor.setQueueCapacity(40); // até 40 tarefas podem aguardar na fila se as threads estiverem ocupadas.
        executor.setThreadNamePrefix("app-async-"); // ajuda no debug/log porque nomeia as threads.
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy()); // se o pool estiver cheio e a fila lotada, tarefas excedentes são descartadas silenciosamente (sem exceção ou log).
        executor.initialize();
        return executor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new SimpleAsyncUncaughtExceptionHandler();
    }
}
