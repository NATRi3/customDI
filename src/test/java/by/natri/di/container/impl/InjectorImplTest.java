package by.natri.di.container.impl;

import by.natri.di.container.Injector;
import by.natri.di.container.Provider;
import by.natri.di.example.*;
import by.natri.di.exception.BindingNotFoundException;
import by.natri.di.exception.ConstructorNotFoundException;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class InjectorImplTest {

    @Test
    public void testExistingBinding() {
        Injector injector = new InjectorImpl();

        injector.bind(DBParam.class, DBParamImpl.class);

        Provider<DBParam> daoProvider = injector.getProvider(DBParam.class);

        assertNotNull(daoProvider);

        assertNotNull(daoProvider.getInstance());

        assertNotEquals(daoProvider.getInstance(), daoProvider.getInstance());

        assertSame(DBParamImpl.class, daoProvider.getInstance().getClass());
    }

    @Test
    public void test() {
        Injector injector = new InjectorImpl(); //создаем имплементацию инжектора

        injector.bindSingleton(DBParam.class, DBParamImpl.class);

        injector.bindSingleton(EventDAO.class,InMemoryEventDAOImpl.class);

        Provider<EventDAO> provider = injector.getProvider(EventDAO.class);

        DBParam actual = provider.getInstance().getDb();

        Provider<DBParam> expected = injector.getProvider(DBParam.class);

        assertEquals(actual,expected.getInstance());
    }

    @Test
    public void testBindNotFoundException() {
        Injector injector = new InjectorImpl(); //создаем имплементацию инжектора

        injector.bind(EventDAO.class, InMemoryEventDAOImpl.class); //добавляем в инжектор реализацию интерфейса

        Provider<EventDAO> daoProvider = injector.getProvider(EventDAO.class); //получаем инстанс класса из инжектора

        try{
            daoProvider.getInstance();
        } catch (BindingNotFoundException e){
            assertEquals(e.getClass(), BindingNotFoundException.class);
        }
    }

    @Test
    public void testNoConstructException() {
        Injector injector = new InjectorImpl(); //создаем имплементацию инжектора

        injector.bind(NoConstructService.class, NoConstructServiceImpl.class); //добавляем в инжектор реализацию интерфейса

        Provider<NoConstructService> daoProvider = injector.getProvider(NoConstructService.class); //получаем инстанс класса из инжектора

        try{
            daoProvider.getInstance();
        } catch (ConstructorNotFoundException e){
            assertEquals(e.getClass(), ConstructorNotFoundException.class);
        }
    }
}