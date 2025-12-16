package me.readln.etc;

import lombok.Data;
import java.util.Date;

@Data
public class ProcessingTime {

    private long beginningTime;

    public ProcessingTime() {
        beginningTime = shootCurrentTime();
    }

    private long shootCurrentTime () {
        Date date = new Date();
        return date.getTime();
    }

    public long getCurrentProcessingTimeInSecs () {
        return (shootCurrentTime() - beginningTime) / 1000;
    }

}
