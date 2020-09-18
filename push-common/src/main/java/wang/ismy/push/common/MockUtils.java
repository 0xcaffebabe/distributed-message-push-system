package wang.ismy.push.common;


import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author MY
 * @date 2020/5/9 21:03
 */
public class MockUtils {

    private static Map<Class<?>, Generator<?>> generatorMap = new ConcurrentHashMap<>();

    static {
        generatorMap.put(String.class, new StringGenerator());
        generatorMap.put(Long.class, new LongGenerator());
        generatorMap.put(Boolean.class, new BooleanGenerator());
        generatorMap.put(LocalDateTime.class, new LocalDateTimeGenerator());
    }

    public static <T> T create(Class<T> klass) {
        Constructor<?>[] constructors = klass.getConstructors();
        if (constructors.length == 0) {
            throw new IllegalStateException("没有构造器，无法创建");
        }
        try {
            Object o = constructors[0].newInstance();
            Method[] methods = klass.getDeclaredMethods();
            for (Method method : methods) {
                if (method.getName().startsWith("set")) {
                    Parameter[] parameters = method.getParameters();
                    if (parameters.length != 1) {
                        continue;
                    }
                    Parameter methodParam = parameters[0];
                    Class<?> valType = methodParam.getType();
                    var generator = generatorMap.get(valType);
                    if (generator != null) {
                        method.invoke(o, generator.generate(methodParam));
                    }

                }
            }
            return klass.cast(o);
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    public static <T> List<T> create(Class<T> klass, int number) {
        List<T> list = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            list.add(create(klass));
        }
        return list;
    }

    private static String randomStr(int bound) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        String uuid = UUID.randomUUID().toString();
        for (int i = 0; i < bound; i++) {
            sb.append(random.nextInt(uuid.length()));
        }
        return sb.toString();
    }

    private static Long randomLong() {
        Random random = new Random();
        return random.nextLong();
    }

    private static Boolean randomBoolean() {
        Random random = new Random();
        return random.nextBoolean();
    }

    private static BigDecimal randomBigDecimal() {
        Random random = new Random();
        return BigDecimal.valueOf(random.nextDouble());
    }

    /**
     * 数据生成器
     */
    interface Generator<R> {
        R generate(Parameter parameter);
    }

    static class StringGenerator implements Generator<String> {
        @Override
        public String generate(Parameter parameter) {
            return parameter.getName() + randomStr(3);
        }
    }

    static class LongGenerator implements Generator<Long> {
        @Override
        public Long generate(Parameter parameter) {
            return randomLong();
        }
    }

    static class BooleanGenerator implements Generator<Boolean> {
        @Override
        public Boolean generate(Parameter parameter) {
            return randomBoolean();
        }
    }

    static class LocalDateTimeGenerator implements Generator<LocalDateTime> {
        @Override
        public LocalDateTime generate(Parameter parameter) {
            return LocalDateTime.now();
        }
    }
}
