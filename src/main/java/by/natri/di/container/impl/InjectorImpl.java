package by.natri.di.container.impl;

import by.natri.di.container.Injector;
import by.natri.di.container.Provider;
import by.natri.di.exception.BindingNotFoundException;

import java.util.HashMap;
import java.util.Map;

public class InjectorImpl implements Injector {

    private final Map<Class<?>,Provider<?>> container;

    public InjectorImpl(Map<Class<?>, Provider<?>> container) {
        this.container = container;
    }

    public InjectorImpl(){
        container = new HashMap<>();
    }

    @Override
    public <T> Provider<T> getProvider(Class<T> type) {
        Provider<T> provider = (Provider<T>) container.get(type);
        return provider;
    }

    @Override
    public <T> void bind(Class<T> intf, Class<? extends T> impl) {
        Provider<? extends T> provider = ProviderImpl.prototypeOf(impl,this);
        container.put(intf,provider);
    }

    @Override
    public <T> void bindSingleton(Class<T> intf, Class<? extends T> impl) {
        Provider<? extends T> provider = ProviderImpl.singletorOf(impl,this);
        container.put(intf,provider);
    }
}
