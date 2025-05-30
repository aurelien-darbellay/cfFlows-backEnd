package s05t02.interactiveCV.exception;

public class IllegalInterfaceImplementation extends RuntimeException {
    public IllegalInterfaceImplementation(Class<?> c1, Class<?> c2) {
        super(String.format("Interface %s is only supposed to be implement by class %s", c1.getSimpleName(), c2.getSimpleName()));
    }
}
