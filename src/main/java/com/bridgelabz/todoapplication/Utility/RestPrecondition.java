package com.bridgelabz.todoapplication.Utility;

import org.springframework.lang.Nullable;

public class RestPrecondition {
	public static <T> T checkNotNull(T reference, @Nullable Object errorMessage) {
	    if (reference == null) {
	      throw new NullPointerException(String.valueOf(errorMessage));
	    }
	    return reference;
	  }
	
	public static <T> T checkNotEmpty(T reference, @Nullable Object errorMessage) {
	    if (reference == "") {
	      throw new NullPointerException(String.valueOf(errorMessage));
	    }
	    return reference;
	  }
}
