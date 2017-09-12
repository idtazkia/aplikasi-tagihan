package id.ac.tazkia.payment.virtualaccount;

import org.junit.Test;

public class StringFormattingTests {
    @Test
    public void testFormatVa(){
        Long runningNumber = 10L;
        System.out.println(String.format("%06d", runningNumber));

        String nim = "123";
        System.out.println(String.format("%10s", nim).replace(' ', '0'));
    }
}
