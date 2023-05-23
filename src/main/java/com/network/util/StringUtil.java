package com.network.util;

import java.util.Random;
import java.util.function.IntPredicate;
import java.util.stream.IntStream;

public class StringUtil {

    public StringUtil() {
    }

    /**
     * Tạo ra một chuỗi ngẫu nhiên có độ dài được chỉ định bằng cách sử dụng các ký tự ASCII từ 48 đến 122,
     * loại bỏ các ký tự không phải chữ cái hoặc số và trả về chuỗi đã tạo.
     *
     * @param length Độ dài của chuỗi được tạo ra
     * @return Chuỗi ngẫu nhiên
     */
    public static String randomText(int length) {
        final int ASCII_LOWER_BOUND = 48;
        final int ASCII_UPPER_BOUND = 122;
        Random random = new Random();
        // Lọc các ký tự không phải chữ cái hoặc số
        IntPredicate isAlphanumeric = (i) -> (i <= '9' || i >= 'A') && (i <= 'Z' || i >= 'a');
        // Tạo ra chuỗi ngẫu nhiên
        IntStream randomInts = random.ints(ASCII_LOWER_BOUND, ASCII_UPPER_BOUND + 1);
        String generatedString = randomInts.filter(isAlphanumeric).limit(length).collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();
        return generatedString;
    }

}
