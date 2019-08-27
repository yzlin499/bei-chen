package top.yzlin.beichen;

import org.json.JSONObject;
import org.junit.Test;

import java.text.FieldPosition;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class WSTest {

    @Test
    public void test(){
        Stream.of(12,45,78,56,45,23,21).sorted(Comparator.comparingInt(Integer::intValue).reversed()).forEach(System.out::println);
    }
}
