package al.franzis.cheshire.service

import java.lang.annotation.Target
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.annotation.ElementType

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
annotation ServiceActivationMethod {
}

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
annotation ServiceBindMethod {
}

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
annotation ServiceUnbindMethod {
}

class ServiceProcessor {
	
}