import java.time.ZonedDateTime;

public class ZoneDateTimeDemo {
    public static void main(String[] args) {
        ZonedDateTime time=ZonedDateTime.now();
        System.out.println(time);//2023-05-17T23:32:04.096+08:00[Asia/Shanghai]
    }
}
