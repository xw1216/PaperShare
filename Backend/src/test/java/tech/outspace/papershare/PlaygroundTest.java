package tech.outspace.papershare;

import org.junit.jupiter.api.Test;
import tech.outspace.papershare.utils.time.TimeUtil;

import java.util.Date;

public class PlaygroundTest {
    @Test
    public void printDate() {
        System.out.println(new Date());
        System.out.println(TimeUtil.getUTC());
    }
}
