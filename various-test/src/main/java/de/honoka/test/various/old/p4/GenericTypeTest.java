package de.honoka.test.various.old.p4;

import java.lang.reflect.ParameterizedType;

public class GenericTypeTest {
	
	static class ClassA<T> {
		
		Class<?> getGenericTypeClass() {
			return (Class<?>) ((ParameterizedType) getClass()
					.getGenericSuperclass()).getActualTypeArguments()[0];
		}
	}
	
	public static void main(String[] args) {
		ClassA<String> obj1 = new ClassA<>();
		System.out.println(obj1.getGenericTypeClass());
		System.out.println(((ParameterizedType) obj1.getClass()
				.getGenericSuperclass()).getActualTypeArguments()[0].getTypeName());
		//ClassA<String> obj2 = getObj();
		//System.out.println(obj2.getClass().getGenericSuperclass()
		//		instanceof ParameterizedType);
		//System.out.println(((ParameterizedType) obj2.getClass()
		//		.getGenericSuperclass()).getActualTypeArguments()[0]);
		//System.out.println(obj2.getGenericTypeClass());
	}
}
