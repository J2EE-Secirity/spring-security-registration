package org.baeldung.exception;

import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @see https://planetcalc.ru/2134
 *      **************************
 *      Конвертер URL-encoded строки
 */

public class MyCycleByJava8Test {

    private static final String PARAM_TEST = "param";
    private static int NUM_TEST = 0;

    @Test
    public void testToParamMap1() {
        System.out.println(
                toParamMap1(new String[]{"one", "two", "three"}));
    }

    @Test
    public void testToParamMap2() {
//        String val = null;
//        System.out.println(
//                "val: " + Optional.ofNullable(val).orElse("NONE"));
//        System.out.println(
//                "val: " + Optional.ofNullable(val).map(s -> s + "!").orElse("NONE?"));
//        System.out.println(
//                "val: " + Optional.ofNullable(val).map(s -> s + "!").orElseGet(() -> "NONE??"));

        System.out.println(
                toParamMap2(new String[]{"one", "two", "three"}));
    }

    @Test
    public void test3() {
        List<String> param1 = new ArrayList<>(),
                param2 = new ArrayList<>(),
                param3 = new ArrayList<>(),
                param4 = new ArrayList<>(),
                param5 = new ArrayList<>(),
                param6 = new ArrayList<>();
        param1.add("Aaaaaaaaaaaaa");
        param2.add("Bbbbbbbbbbbbb");
        param3.add("Ccccccccccccc");
        param4.add("Ddddddddddddd");
        param5.add("Eeeeeeeeeeeee");
//        param6.add(null);

        MyException exception = new MyException();
        exception.addHeaders("param1", param1);
        exception.addHeaders("param2", param2);
        exception.addHeaders("param3", param3);
        exception.addHeaders("param4", param4);
        exception.addHeaders("param5", param5);
        exception.addHeaders("param6", param6);

        System.out.println(
                getMessage(exception, "param1"));
        System.out.println(
                getMessage(exception, new String[]{"param1", "param2", "param3", "param10"}));
    }

    private static String PARAM;
    private static Map<String, Object> getMessage(final MyException ex,
                                                  final String... params) {
//        return Stream.of(params).collect(
//          Collectors.toMap(k -> { PARAM = k; return k; }, v -> getMessage(ex.getHeader(PARAM))));

        final String none = "NONE";
        return Stream.of(params).collect(
                Collectors.toMap(k -> { PARAM = k; return k; },
                        v -> Optional.ofNullable(ex.getHeader(PARAM)) // TODO: я принимаю какой-то список... но он может бть пустым!
                                .map(p -> p.stream().findFirst().orElse(none)) // TODO: если список НЕпустой - я перебираю его по элементам и ??? возвращаю первый найденный элемент ИННАЧЕ по умолчанию...
                                .orElse(none))); // TODO: если список пустой - возвращаю элемент по умолчнию
    }

    private static Map<String, Object> getMessage(final MyException ex,
                                                  final String param) {
        return Stream.of(ex.getHeader(param)).collect(
                Collectors.toMap(k -> param, v -> getMessage(v)));
    }

    private static String getMessage(final List<String> param) {
        final String none = "NONE";
        return Optional.ofNullable(param)
                .map(p -> p.stream()
                        .findFirst()
                        .orElse(none))
                .orElse(none);
    }

    /**
     * @see http://info.javarush.ru/KapChook/2015/03/09/Избавляемся-от-циклов-в-Java-8-.html
     *      http://info.javarush.ru/translation/2014/10/09/Особенности-Java-8-максимальное-руководство-часть-2-.html
     * @see https://habrahabr.ru/post/224593
     */
    private Map<String, Object> toParamMap2(final String... params) {
//        return Stream.of(params)
//                .collect(Collectors.toMap(k -> k, v -> v));
        return Stream.of(params)
                .collect(Collectors.toMap(k -> PARAM_TEST + String.valueOf(NUM_TEST++), v -> v));
    }

    private Map<String, Object> toParamMap1(String... params) {
        Map<String, Object> paramMap = new HashMap<>();
        if (params != null && params.length > 0) {
            for (int i = 0; i < params.length; i++) {
                paramMap.put(PARAM_TEST + i, params[i]);
            }
        }
        return paramMap;
    }

}
