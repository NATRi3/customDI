package by.natri.di.example;

public class NoConstructServiceImpl implements NoConstructService{

    private Integer i;

    private NoConstructServiceImpl(Integer i){
        this.i = i;
    }
}
