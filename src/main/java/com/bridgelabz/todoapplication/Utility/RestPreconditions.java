package com.bridgelabz.todoapplication.Utility;

import java.lang.reflect.Field;
import java.util.Objects;
import java.util.stream.Stream;

import javax.validation.constraints.Null;

import org.springframework.lang.Nullable;

public class RestPreconditions {
	// public static <T> T checkNotNull(Object obj, @Nullable Object errorMessage) {
		 
		 /*public  boolean checkIfNull() {
			    for (Field f : getClass().getDeclaredFields())
			        if (f.get(this) != null)
			            return false;
			    return true;            
			}*/
		 /* if (reference == null) {
		      throw new NullPointerException(String.valueOf(errorMessage));
		    }
		    return reference;
*/		  
	 
	
//}
	 static Boolean m(String id,String name) {
		 Stream.of(id, name).equals("");
		 return Stream.of(id, name)
			        .allMatch(Objects::isNull);
	 }
public static void main() {
	m(null,"2");
}
}