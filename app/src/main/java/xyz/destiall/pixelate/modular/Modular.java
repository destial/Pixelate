package xyz.destiall.pixelate.modular;

/**
 * Written by Rance
 */
public interface Modular {
    /**
     * Get a loaded module
     * @param clazz The module class
     * @return The loaded module, null if none
     */
    <N extends Module> N getModule(Class<N> clazz);

    /**
     * Add a module
     * @param module The module to add
     */
    void addModule(Module module);

    /**
     * Check if a module is loaded
     * @param clazz The module class
     * @return true if loaded, otherwise false
     */
    <N extends Module> boolean hasModule(Class<N> clazz);

    /**
     * Remove a loaded module
     * @param clazz The module class
     * @return The removed module, null if none
     */
    <N extends Module> N removeModule(Class<N> clazz);
}
