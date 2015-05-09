/*Dev roadmap:
 * This class should invoke a method and store the result.
 * The results will be stored with the method parameters to facilitate searching.
 * 
 * Problems:
 *  There is definately a better way to store and search for result/param objects. Could I hash the params?
 *  
 * 
 * 
 * 
 */

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * A class to easily memoize one-to-one methods. 
 * @author ewertj
 *
 * @param <Type>
 */
public class Memoizer<Type> {
	ArrayList<Result> storage = new ArrayList<Result>(); 

	/**
	 * Tests to ensure proper function
	 * @param args
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 */
	public static void main(String[] args) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		int count = 10000;
		Memoizer<Integer> m = new Memoizer<Integer>();
		Integer res = (Integer) m.memoize(m, "countDown", new Integer[]{count});
		System.out.println(res);
	}

	/**
	 * Invokes the method and stores the result and parameters in a list for memoization
	 * @param callOnObject
	 * @param method
	 * @param parameters
	 * @return
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	@SuppressWarnings("unchecked")
	public Type memoize(Object callOnObject, String methodName, Object[] parameters) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		
		//Get the Method object
		Method m = getMethod(callOnObject.getClass(), methodName);
		
		//Check for stored solution to the call
		for(Result a: storage){
			if(a.params.equals(parameters)){
				return (Type)a.r;
			}
		}
		Type result = (Type) m.invoke(callOnObject, parameters);
		Result<Type> r = new Result<Type>(result, parameters);
		storage.add(r);
		return result;
	}
	
	private Method getMethod(Class callOnClass, String methodName) {
		callOnClass.getClass().getDeclaredMethods();
		Method[] allMethods = callOnClass.getDeclaredMethods();
	    for (Method m : allMethods) {
			String mname = m.getName();
			if (!mname.equals(methodName)){
				return m;
			}
	    }
	    throw new IllegalArgumentException();
			
	}

	/**
	 * A simple method to memoize
	 * @param startNum
	 * @return
	 */
	public int countDown(int startNum){
		int total = 0;
		for(int i = startNum; i>0; i--){
			total++;
		}
		return total;
	}
}

class Result<Type>{
	Type r;
	Object[] params;
	Result(Type r, Object[] params){
		this.r = r;
		this.params = params;
	}
}
class MethodNameHelper {
	  public Method getMethod() {
	    return this.getClass().getEnclosingMethod();
	  }
}
