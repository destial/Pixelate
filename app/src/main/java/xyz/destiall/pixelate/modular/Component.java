package xyz.destiall.pixelate.modular;

/**
 * Written by Rance
 * @param <E> The class this component belongs to
 */
public interface Component<E> extends Module {
    E getParent();
    Component<E> setParent(E e);
}
