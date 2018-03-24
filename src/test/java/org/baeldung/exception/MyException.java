package org.baeldung.exception;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MyException {

    private Map<String, List<String>> headers = new HashMap<>();

    public void addHeaders(String param, List<String> value) {
        headers.put(param, value);
    }

    public List<String> getHeader(String param) {
        return headers.get(param);
    }


    public static void main(String[] args) {
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

        System.out.println( "param1: " + exception.getHeader("param1").get(0) );
//        System.out.println( "param6: " + exception.getHeader("param6").get(0) );
//        System.out.println( "param10: " + exception.getHeader("param10").get(0) );
        System.out.println( "param6: " + getMessage(exception.getHeader("param6")) );
        System.out.println( "param10: " + getMessage(exception.getHeader("param10")) );
        System.out.println();

        System.out.println( getMessage(exception, "param1") );
        System.out.println( getMessage(exception, "param10") );
        System.out.println();

        getMessage(exception, new String[]{"param1","param2","param6","param10"});
        System.out.println();

        System.out.println( getMessage(exception, new String[]{"param1","param2","param6","param10"}) );
    }

    private static String getMessage(List<String> param) {
//        return Optional.ofNullable(param)
//                .map(p -> p.get(0))
//                .orElse("");
        return Optional.ofNullable(param)
                .map(p -> p.stream()
                        .findFirst()
                        .orElse(""))
                .orElse("");
    }

//    https://stackoverflow.com/questions/20363719/java-8-listv-into-mapk-v
//    http://www.techiedelight.com/convert-stream-to-map-java-8/
//    http://www.technicalkeeda.com/java-8-tutorials/java-8-convert-list-to-map
//    http://javarevisited.blogspot.com/2016/04/10-examples-of-converting-list-to-map.html
    private static Map<String, Object> getMessage(final MyException ex,
                                                  final String param) {
        return Stream.of(ex.headers).collect(
                Collectors.toMap(k -> param, v -> getMessage(v.get(param))));
    }

    private static Map<String, Object> getMessage(final MyException ex,
                                                  final String... params) {
//        Stream.of(params).map(param ->
//                Stream.of(ex.headers).collect(
//                        Collectors.toMap(k -> param, v -> getMessage(v.get(param)))))
//                .forEach(System.out::println);

        Map<String, Object> messages = new HashMap<>();
        Stream.of(params).map(param ->
                Stream.of(ex.headers).collect(
                        Collectors.toMap(k -> param, v -> getMessage(v.get(param)))))
                .forEach(m -> messages.putAll(m));
        return messages;

        ////////////////////////////
//        Stream<Map<String, String>> mapStream = Stream.of(params).map(param ->
//                Stream.of(ex.headers).collect(
//                        Collectors.toMap(k -> param, v -> getMessage(v.get(param)))))
////                .findFirst().get();
//                .distinct();
//        return mapStream.findFirst().get();
    }



    class MyExceptionRead {

        private MyException ex;
        private String[] params;

        private MyExceptionRead() {}

        public MyExceptionRead(final MyException ex) {
            this.ex = ex;
            this.params = params;
        }

        public Map<String, List<String>> getMessage(final String... params) {
            return null;
        }

    }

}





