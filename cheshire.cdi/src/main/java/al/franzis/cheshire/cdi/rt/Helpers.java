package al.franzis.cheshire.cdi.rt;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class Helpers {
	
	public static Method getAnnotatedMethod( Class<?> annotatedClass, Class<? extends Annotation> annotation ) {
		for(Method m : annotatedClass.getDeclaredMethods()) {
			if(m.isAnnotationPresent(annotation))
				return m;
		}
		return null;
	}
	
}
