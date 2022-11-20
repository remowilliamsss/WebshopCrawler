package ru.egorov.StoreCrawler.crawlers;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

// TODO: 20.11.2022 Не очень понятно назначение класса в таком виде. Возможно, твои разъяснения
//  позволят посоветовать что-то дельное
public abstract class Crawler {
    private Boolean isStopped;
    private Timer timer;

    /*    Метод добавляет в базу данных все товары с сайта, которых еще нет в бд,
обновляет информацию у существующих, удаляет товары, которых нет на сайте.*/
    public abstract void scan() throws IOException;

    /*    Останавливает текущее сканирование.*/
    public void stopScan() {
        isStopped = true;
    }

    /*  Запускает автоматическое сканирование сайта с интервалом один раз в сутки.*/
    public void start() {
        stop();

        timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    scan();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                // TODO: 20.11.2022 https://www.baeldung.com/spring-scheduled-tasks
            }
        }, 0, 24*60*60*1000);
    }

    /*  Останавливает автоматическое сканирование сайта.*/
    public void stop() {
        if (timer != null)
            timer.cancel();

        stopScan();
    }

    public Boolean getStopped() {
        return isStopped;
    }

    public void setStopped(Boolean stopped) {
        isStopped = stopped;
    }

    public abstract String getStoreName();
}
