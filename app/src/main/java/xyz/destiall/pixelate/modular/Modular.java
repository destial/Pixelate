package xyz.destiall.pixelate.modular;

public interface Modular {
    <N extends Module> N getModule(Class<N> clazz);
    <N extends Module> void addModule(N module);
    <N extends Module> boolean hasModule(Class<N> clazz);
    <N extends Module> N removeModule(Class<N> clazz);
}
