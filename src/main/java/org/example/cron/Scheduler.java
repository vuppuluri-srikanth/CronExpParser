package org.example.cron;

import org.example.cron.dtos.CronSchedule;
import org.example.cron.dtos.Task;
import org.example.cron.exceptions.InvalidCronExpressionException;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Scheduler implements AutoCloseable {
    private final CronParser cronParser;
    private final ScheduledExecutorService scheduledThreadPool;
    private final PriorityQueue<Task> minHeap;
    private final ExecutorService workerPool;

    //TODO: Handle stale time
    public Scheduler(CronParser cronParser) {
        this.cronParser = cronParser;
        minHeap = new PriorityQueue<>();
        workerPool = Executors.newWorkStealingPool(Runtime.getRuntime().availableProcessors());
        scheduledThreadPool = Executors.newScheduledThreadPool(1);
        scheduledThreadPool.schedule(CronTask::new, 1, TimeUnit.SECONDS);
    }

    public void scheduleJob(Runnable runnable, String cronExpression) throws InvalidCronExpressionException {
        CronSchedule cronSchedule = cronParser.parseExpression(cronExpression);
        Optional<LocalDateTime> nextScheduleTime = cronSchedule.getNextScheduleTime(LocalDateTime.now());
        if (nextScheduleTime.isEmpty())
            return;
        Task task = new Task(nextScheduleTime.get(), runnable, cronSchedule);
        minHeap.add(task);
    }

    @Override
    public void close() throws Exception {
        scheduledThreadPool.close();
        workerPool.close();
    }

    private class CronTask implements Runnable {
        @Override
        public void run() {
            while (!minHeap.isEmpty() && !minHeap.peek().getTime().isAfter(LocalDateTime.now())) {
                Task task = minHeap.poll();
                assert task != null;
                workerPool.execute(task.getFunc());

                var nextScheduleTime = task.getCronSchedule().getNextScheduleTime(task.getTime());
                if (nextScheduleTime.isEmpty())
                    continue;
                Task newTask = new Task(nextScheduleTime.get(), task.getFunc(), task.getCronSchedule());
                minHeap.add(newTask);
            }
        }
    }
}



