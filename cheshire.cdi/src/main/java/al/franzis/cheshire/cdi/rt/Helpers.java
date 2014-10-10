package al.franzis.cheshire.cdi.rt;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class Helpers {
	
	public static Method getAnnotatedMethod( Class<?> annotatedClass, Class<? extends Annotation> annotation ) {
		for(Method m : annotatedClass.getDeclaredMethods()) {
			if(m.isAnnotationPresent(annotation))
				return m;
		}
		return null;
	}
	
	public static List<Method> getAnnotatedMethods( Class<?> annotatedClass, Class<? extends Annotation> annotation ) {
		List<Method> annotatedMethods = new ArrayList<Method>();
		for(Method m : annotatedClass.getDeclaredMethods()) {
			if(m.isAnnotationPresent(annotation))
				annotatedMethods.add(m);
		}
		return annotatedMethods;
	}
	
}
