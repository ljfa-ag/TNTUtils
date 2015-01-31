package ljfa.tntutils.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class ReflectionHelper {
    /** Gets the value of a field */
    public static Object getField(Class<?> cl, String name, Object obj) throws ReflectiveOperationException {
        Field field = cl.getDeclaredField(name);
        field.setAccessible(true);
        return field.get(obj);
    }
    
    /** Gets the value of a static field */
    public static Object getStaticField(Class<?> cl, String name) throws ReflectiveOperationException {
        return getField(cl, name, null);
    }
    
    /** Sets the value of a field */
    public static void setField(Class<?> cl, String name, Object obj, Object value) throws ReflectiveOperationException {
        Field field = cl.getDeclaredField(name);
        field.setAccessible(true);
        field.set(obj, value);
    }
    
    /** Sets the value of a static field */
    public static void setStaticField(Class<?> cl, String name, Object value) throws ReflectiveOperationException {
        setField(cl, name, null, value);
    }
    
    /** Changes the value of a final field */
    public static void setFinalField(Class<?> cl, String name, Object obj, Object value) throws ReflectiveOperationException {
        Field field = cl.getDeclaredField(name);
        field.setAccessible(true);
        
        Field modifiers = Field.class.getDeclaredField("modifiers");
        modifiers.setAccessible(true);
        modifiers.setInt(field, field.getModifiers() & ~Modifier.FINAL);
        
        field.set(obj, value);
    }
}
