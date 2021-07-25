package by.natri.di.container.impl;

import by.natri.di.annotation.Inject;
import by.natri.di.container.Injector;
import by.natri.di.container.Provider;
import by.natri.di.container.Type;
import by.natri.di.exception.BindingNotFoundException;
import by.natri.di.exception.ConstructorNotFoundException;
import by.natri.di.exception.TooManyConstructorsException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class ProviderImpl<T> implements Provider<T> {

    private T t;
    private final Class<? extends T> tClass;
    private final Type type;
    private final Injector injector;

    public static <T> Provider<T> singletorOf(Class<? extends T> tClass, Injector injector){
        return new ProviderImpl<T>(tClass,Type.SINGLETON, injector);
    }

    public static <T> Provider<T> prototypeOf(Class<T> tClass, Injector injector){
        return new ProviderImpl<>(tClass,Type.PROTOTYPE, injector);
    }

    public ProviderImpl(Class<? extends T> tClass, Type type, Injector injector) {
        this.tClass = tClass;
        this.type = type;
        this.injector = injector;
    }

    @Override
    public T getInstance() {
        if (Type.SINGLETON == type && t != null){
            return t;
        }

        Constructor<?> constructor = getConstructor();

        Object[] params =
                getProvidersForConstructorFromContainer(constructor)
                        .stream()
                        .map(Provider::getInstance)
                        .toArray();

        t = (T) getObj(constructor, params);

        return t;
    }

    private Object getObj(Constructor<?> constructor, Object... objects){
        try {
            constructor.setAccessible(true);
            return constructor.newInstance(objects);
        } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private Constructor<?> getConstructor(){
        Constructor<?> result = null;

        Constructor<?>[] declaredConstructors = tClass.getDeclaredConstructors();

        for (Constructor<?> constructor : declaredConstructors) {
            Annotation[] annotations = constructor.getAnnotations();

            for (Annotation annotation : annotations) {
                if (annotation.annotationType().equals(Inject.class)){
                    if (result != null){
                        throw new TooManyConstructorsException("WOOOPS");
                    }
                    result = constructor;
                }
            }
        }

        if (result == null){
            try {
                result = tClass.getConstructor();
            } catch (NoSuchMethodException e) {
                throw new ConstructorNotFoundException(e);
            }
        }

        return result;
    }

    private List<Provider<?>> getProvidersForConstructorFromContainer(Constructor<?> constructor){
        List<Provider<?>> result = new ArrayList<>(constructor.getParameterCount());

        Class<?>[] parameterTypes = constructor.getParameterTypes();

        for (int i = 0; i < parameterTypes.length; i++) {
            Class<?> parameterType = parameterTypes[i];
            Provider<?> provider = injector.getProvider(parameterType);
            if (provider == null){
                throw new BindingNotFoundException(parameterType + " not found");
            }
            result.add(provider);
        }

        return result;
    }
}
