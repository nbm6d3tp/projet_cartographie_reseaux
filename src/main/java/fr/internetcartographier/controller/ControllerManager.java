package fr.internetcartographier.controller;

import fr.internetcartographier.Main;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class ControllerManager {

    private static ControllerManager instance;
    private final Map<String, Controller> controllers;

    private ControllerManager() {
        controllers = new HashMap<>();
    }

    public static synchronized ControllerManager getInstance() {
        if (instance == null) {
            instance = new ControllerManager();
        }

        return instance;
    }

    private Main main;

    public void registreMain(Main main) {
        this.main = main;
    }

    public Main getMain() {
        return main;
    }

    public void registerController(String name, Controller controller) {
        controllers.put(name, controller);
    }

    public Controller getController(String name) {
        return controllers.get(name);
    }

    public void callControllerMethod(String controllerName, String methodName, Object... args) {
        Controller controller = controllers.get(controllerName);

        if (controller != null) {
            try {
                Method method = controller.getClass().getMethod(methodName, getParameterTypes(args));
                method.invoke(controller, args);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException("Error: While invoking " + methodName + " method for controller " + controllerName + "!");
            }
        }
    }

    private Class<?>[] getParameterTypes(Object[] args) {
        Class<?>[] parameterTypes = new Class<?>[args.length];

        for (int i = 0; i < args.length; i++) {
            parameterTypes[i] = args[i].getClass();
        }

        return parameterTypes;
    }

}
