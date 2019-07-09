package com.hisalari.util.obj;

import com.hisalari.util.date.DateUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ObjectUtil {

	/**
	 * 判断对象是否为空
	 * 
	 * @param o
	 * @return
	 */
	public static boolean isEmpty(Object o) {
		if (o == null) {
			return true;
		}
		if (String.class.isAssignableFrom(o.getClass())) {
			String result = (String) o;
			return result.trim().length() == 0;
		} else if (List.class.isAssignableFrom(o.getClass())) {
			List<?> result = (List<?>) o;
			return result.size() == 0;
		} else if (Set.class.isAssignableFrom(o.getClass())) {
			Set<?> result = (Set<?>) o;
			return result.size() == 0;
		} else if (Map.class.isAssignableFrom(o.getClass())) {
			Map<?, ?> result = (Map<?, ?>) o;
			return result.size() == 0;
		} else if (o.getClass().isArray()) {
			return Array.getLength(o) == 0;
		}
		return false;
	}

    public static void main(String[] args) {
	    BigDecimal bigDecimal = new BigDecimal(10.0);
        System.out.println(((String) getValue(bigDecimal,null)));
    }

	@SuppressWarnings("unchecked")
	public static <T> T getValue(Object obj, T defaultValue) {
		if (defaultValue == null && obj == null) {
			return null;
		}
		if (defaultValue == null) {
            if (obj instanceof BigDecimal){
                obj = obj.toString();
            }
			if (obj instanceof Double){
				obj = obj.toString();
			}
			if (obj instanceof Integer){
				obj = obj.toString();
			}
			return (T) obj;
		}
		try {
			if (defaultValue.getClass().isArray() || List.class.isAssignableFrom(defaultValue.getClass())
					|| Map.class.isAssignableFrom(defaultValue.getClass())) {
				return obj == null ? defaultValue : (T) obj;
			} else if (String.class.isAssignableFrom(defaultValue.getClass())) {
				return obj == null ? defaultValue : (T) String.valueOf(obj);
			} else {
			    if (obj instanceof BigDecimal){
			        obj = obj.toString();
                }
				if (obj instanceof Double){
					obj = obj.toString();
				}
				String value = String.valueOf(obj);
				if (Integer.class.isAssignableFrom(defaultValue.getClass())
						|| int.class.isAssignableFrom(defaultValue.getClass())) {
					return obj == null ? defaultValue : (T) Integer.valueOf(value.split("\\.")[0]);
				} else if (Long.class.isAssignableFrom(defaultValue.getClass())
						|| long.class.isAssignableFrom(defaultValue.getClass())) {
					return obj == null ? defaultValue : (T) Long.valueOf(value.split("\\.")[0]);
				} else if (Float.class.isAssignableFrom(defaultValue.getClass())
						|| float.class.isAssignableFrom(defaultValue.getClass())) {
					return obj == null ? defaultValue : (T) Float.valueOf(value);
				} else if (Double.class.isAssignableFrom(defaultValue.getClass())
						|| double.class.isAssignableFrom(defaultValue.getClass())) {
					return obj == null ? defaultValue : (T) Double.valueOf(value);
				} else if (Boolean.class.isAssignableFrom(defaultValue.getClass())
						|| boolean.class.isAssignableFrom(defaultValue.getClass())) {
					return obj == null ? defaultValue : (T) Boolean.valueOf(value);
				} else if (Short.class.isAssignableFrom(defaultValue.getClass())
						|| short.class.isAssignableFrom(defaultValue.getClass())) {
					return obj == null ? defaultValue : (T) Short.valueOf(value.split("\\.")[0]);
				} else if (Byte.class.isAssignableFrom(defaultValue.getClass())
						|| byte.class.isAssignableFrom(defaultValue.getClass())) {
					return obj == null ? defaultValue : (T) Byte.valueOf(value.split("\\.")[0]);
				} else if (Character.class.isAssignableFrom(defaultValue.getClass())
						|| char.class.isAssignableFrom(defaultValue.getClass())) {
					return obj == null ? defaultValue : (T) obj;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return defaultValue;
	}

	public static <T> Collection<T> removeSameData(Collection<T> collection) {
		if (collection == null || collection.size() == 0)
			return collection;
		if (List.class.isAssignableFrom(collection.getClass())) {
			List<T> list = (List<T>) collection;
			List<T> newList = new ArrayList<T>();
			for (T str : list) {
				if (!newList.contains(str)) {
					newList.add(str);
				}
			}
			return newList;
		} else if (Set.class.isAssignableFrom(collection.getClass())) {
			Set<T> set = (Set<T>) collection;
			Set<T> newSet = new HashSet<T>();
			for (T str : set) {
				if (!newSet.contains(str)) {
					newSet.add(str);
				}
			}
			return newSet;
		}
		return collection;
	}

	public static List<Field> getField(Class<?> clazz, List<Field> list) {
		if (list == null)
			list = new ArrayList<Field>();
		Field[] fields = clazz.getDeclaredFields();
		if (fields != null)
			list.addAll(Arrays.asList(fields));
		Class<?> superClass = clazz.getSuperclass();
		if (superClass != null)
			getField(superClass, list);
		return list;
	}

	public static <T> T replaceOldObejctValue(T oldObject, T newObject) {
		if (oldObject == null || newObject == null)
			return oldObject;
		List<Field> newFields = getField(newObject.getClass(), null);
		List<Field> oldFields = getField(oldObject.getClass(), null);
		for (Field field : newFields) {
			field.setAccessible(true);
			try {
				Object obj = field.get(newObject);
				if (obj != null) {
					String name = field.getName();
					for (Field _field : oldFields) {
						String _name = _field.getName();
						if (name.equals(_name)) {
							_field.setAccessible(true);
							Object _obj = _field.get(oldObject);
							if (!obj.equals(_obj)) {
								_field.set(oldObject, obj);
							}
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return oldObject;
	}
	
	public static <T> T copyObejctValue(T srcObject, T destObject) {
		if (srcObject == null || destObject == null)
			return destObject;
		List<Field> newFields = getField(destObject.getClass(), null);
		List<Field> oldFields = getField(srcObject.getClass(), null);
		for (Field field : newFields) {
			int mod = field.getModifiers();
			if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
				continue;
			}
			field.setAccessible(true);
			try {
				String name = field.getName();
				for (Field _field : oldFields) {
					String _name = _field.getName();
					if (name.equals(_name)) {
						_field.setAccessible(true);
						Object obj = _field.get(srcObject);
						field.set(destObject, obj);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return destObject;
	}

	/**
	 * 对象转map
	 * 
	 * @param obj
	 * @return
	 */
	public static Map<String, Object> objectToMap(Object obj) {
		if (obj == null) {
			return null;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		List<Field> fields = getField(obj.getClass(), null);
		for (Field field : fields) {
			int mod = field.getModifiers();
			if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
				continue;
			}
			field.setAccessible(true);
			try {
				Class<?> clazz = field.getType();
				String name = clazz.getName();
				if (List.class.isAssignableFrom(clazz)) {
					@SuppressWarnings("rawtypes")
                    List list = (List) field.get(obj);
					if (!isEmpty(list)) {
						Object o = list.get(0);
						if (o.getClass().getName().startsWith("java.lang")) {
							map.put(field.getName(), list);
						} else {
							List<Map<String, Object>> tempList = new ArrayList<Map<String, Object>>();
							for (int i = 0; list != null && i < list.size(); i++) {
								tempList.add(objectToMap(list.get(i)));
							}
							map.put(field.getName(), tempList);
						}
					} else {
						map.put(field.getName(), list);
					}
				} else if (!name.startsWith("java")) {
					map.put(field.getName(), objectToMap(field.get(obj)));
				} else {
					map.put(field.getName(), field.get(obj));
				}
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return map;
	}
	
	/**
	 * 对象转map
	 * 
	 * @param obj
	 * @return
	 */
	public static Map<String, Object> objectToMap(Object obj, String...noMapper) {
		if (obj == null) {
			return null;
		}
		List<String> noMapperList = Arrays.asList(noMapper);
		if (noMapperList == null) {
			noMapperList = new ArrayList<String>();
		}
		Map<String, Object> map = new HashMap<String, Object>();
		List<Field> fields = getField(obj.getClass(), null);
		for (Field field : fields) {
			int mod = field.getModifiers();
			if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
				continue;
			}
			if (noMapperList.contains(field.getName())) {
				continue;
			}
			field.setAccessible(true);
			try {
				Class<?> clazz = field.getType();
				String name = clazz.getName();
				if (List.class.isAssignableFrom(clazz)) {
					@SuppressWarnings("rawtypes")
                    List list = (List) field.get(obj);
					if (!isEmpty(list)) {
						Object o = list.get(0);
						if (o.getClass().getName().startsWith("java.lang")) {
							map.put(field.getName(), list);
						} else {
							List<Map<String, Object>> tempList = new ArrayList<Map<String, Object>>();
							for (int i = 0; list != null && i < list.size(); i++) {
								tempList.add(objectToMap(list.get(i)));
							}
							map.put(field.getName(), tempList);
						}
					} else {
						map.put(field.getName(), list);
					}
				} else if (!name.startsWith("java")) {
					map.put(field.getName(), objectToMap(field.get(obj)));
				} else {
					map.put(field.getName(), field.get(obj));
				}
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return map;
	}

	public static void objectToMap(Map<String, Object> map, Object obj) {
		if (obj == null) {
			return;
		}
		List<Field> fields = getField(obj.getClass(), null);
		for (Field field : fields) {
			int mod = field.getModifiers();
			if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
				continue;
			}
			field.setAccessible(true);
			try {
				Class<?> clazz = field.getType();
				String name = clazz.getName();
				if (List.class.isAssignableFrom(clazz)) {
					@SuppressWarnings("rawtypes")
                    List list = (List) field.get(obj);
					List<Map<String, Object>> tempList = new ArrayList<Map<String, Object>>();
					for (int i = 0; list != null && i < list.size(); i++) {
						tempList.add(objectToMap(list.get(i)));
					}
					map.put(field.getName(), tempList);
				} else if (!name.startsWith("java")) {
					objectToMap(map, field.get(obj));
					map.put(field.getName(), objectToMap(field.get(obj)));
				} else {
					map.put(field.getName(), field.get(obj));
				}
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static Object getObjectValue(Object obj, String key) {
		return getObjectValue(obj, obj.getClass(), key);
	}
	
	public static Object getObjectValue(Object obj, Class<?> classType, String key) {
		if (isEmpty(obj)) return null;
		try {
			Class<?> clazzType = obj.getClass();
			if (clazzType == classType) {
				return getFieldValue(obj, classType, key);
			} else {
				Object o = getTypeClass(clazzType, classType, obj, key);
				if (o != null) {
					return o;
				} else {
					return getSuperClass(clazzType, classType, obj, key);
				}
			}
		} catch (Exception e) {
			return null;
		}
		
	}
	
	private static Object getSuperClass(Class<?> clazzType, Class<?> classType, Object obj, String key) throws Exception {
		if (clazzType == null) return null;
		if (clazzType == classType) {
			return getFieldValue(obj, classType, key);
		} else {
			Object o = getTypeClass(clazzType, classType, obj, key);
			if (o == null) {
				return getSuperClass(clazzType, classType, obj, key);
			} else {
				return o;
			}
		} 
	}
	
	private static Object getTypeClass(Class<?> clazzType, Class<?> classType, Object obj, String key) throws Exception {
		Field[] fields = clazzType.getDeclaredFields();
		if (ObjectUtil.isEmpty(fields)) {
			return null;
		} else {
			for (Field field : fields) {
				Class<?> clazz = field.getType();
				if (clazz == classType) {
					field.setAccessible(true);
					return getFieldValue(field.get(obj), clazz, key);
				}
			}
			return null;
		}
	}
	
	public static Object getFieldValue(Object obj, Class<?> classType, String key) {
		try {
			Field field = classType.getDeclaredField(key);
			if (field != null) {
				field.setAccessible(true);
				return field.get(obj);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * map转对象
	 * 
	 * @param map
	 * @param beanClass
	 * @return
	 */
	public static Object mapToObject(Map<String, Object> map, Class<?> beanClass) {
		if (map == null)
			return null;
		Object obj = null;
		try {
			obj = beanClass.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (obj == null)
			return null;
		List<Field> fields = getField(obj.getClass(), null);
		for (Field field : fields) {
			int mod = field.getModifiers();
			if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
				continue;
			}
			field.setAccessible(true);
			try {
				field.set(obj, map.get(field.getName()));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return obj;
	}
	
	/**
	 * 
	* @Title: setValue
	* @Description: 为对象赋值
	* @author kevin
	* @date 2018年8月14日 下午5:34:58
	* @param obj
	* @param value
	* @param fieldName
	* @return Object
	* @throws
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Object setValue(Object obj, Object value, String fieldName) {
		if (value == null) {
			return obj;
		}
		List<Field> fields = getField(obj.getClass(), null);
		for (Field field : fields) {
			String name = field.getName();
			if (name.equals(fieldName)) {
				try {
					field.setAccessible(true);
					if (List.class.isAssignableFrom(field.getType())) {
						if (field.get(obj) == null) {
							field.set(obj, Collections.emptyList());
						}
						List list = (List) field.get(obj);
						if (List.class.isAssignableFrom(value.getClass())) {
							list.addAll((List)value);
						} else {
							list.add(value);
						}
					} else if (Set.class.isAssignableFrom(field.getType())) {
						if (field.get(obj) == null) {
							field.set(obj, Collections.emptySet());
						}
						Set set = (Set) field.get(obj);
						if (Set.class.isAssignableFrom(value.getClass())) {
							set.addAll((Set)value);
						} else {
							set.add(value);
						}
					} else if (Map.class.isAssignableFrom(field.getType())) {
						if (field.get(obj) == null) {
							field.set(obj, Collections.emptyMap());
						}
						Map map = (Map) field.get(obj);
						if (Map.class.isAssignableFrom(value.getClass())) {
							map.putAll((Map)value);
						} 
					} else {
						if (List.class.isAssignableFrom(value.getClass()) || Set.class.isAssignableFrom(value.getClass()) || Map.class.isAssignableFrom(value.getClass())) {
							System.out.println("参数类型不匹配：" + value + "不能转换成：" + field.getType().getName());
							return obj;
						}
						String tempValue = String.valueOf(value);
						if (Integer.class.isAssignableFrom(field.getType())
								|| int.class.isAssignableFrom(field.getType())) {
							field.set(obj, Integer.valueOf(tempValue.split("\\.")[0]));
						} else if (Long.class.isAssignableFrom(field.getType())
								|| long.class.isAssignableFrom(field.getType())) {
							field.set(obj, Long.valueOf(tempValue.split("\\.")[0]));
						} else if (Float.class.isAssignableFrom(field.getType())
								|| float.class.isAssignableFrom(field.getType())) {
							field.set(obj, Float.valueOf(tempValue));
						} else if (Double.class.isAssignableFrom(field.getType())
								|| double.class.isAssignableFrom(field.getType())) {
							field.set(obj, Double.valueOf(tempValue));
						} else if (Boolean.class.isAssignableFrom(field.getType())
								|| boolean.class.isAssignableFrom(field.getType())) {
							field.set(obj, Boolean.valueOf(tempValue));
						} else if (Short.class.isAssignableFrom(field.getType())
								|| short.class.isAssignableFrom(field.getType())) {
							field.set(obj, Short.valueOf(tempValue.split("\\.")[0]));
						} else if (Byte.class.isAssignableFrom(field.getType())
								|| byte.class.isAssignableFrom(field.getType())) {
							field.set(obj, Byte.valueOf(tempValue.split("\\.")[0]));
						} else if (Character.class.isAssignableFrom(field.getType())
								|| char.class.isAssignableFrom(field.getType())) {
							field.set(obj, Character.valueOf(tempValue.charAt(0)));
						} else {
							field.set(obj, tempValue);
						}
					}
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
		return obj;
	}
	
	/**
	 * 
	* @Title: sortString
	* @Description: 字符串排序
	* @author kevin
	* @date 2017年11月20日 下午3:21:43
	* @param string
	* @return String
	* @throws
	 */
	public static String sortString(String string, String defaultValue) {
		char[] chs = string.toCharArray();
		Arrays.sort(chs);
		if (chs == null)
			return defaultValue;
		int iMax = chs.length - 1;
		if (iMax == -1)
			return defaultValue;
		StringBuilder b = new StringBuilder();
		for (int i = 0;; i++) {
			b.append(chs[i]);
			if (i == iMax)
				return b.toString();
		}
	}
	
	public static String sortString(String string) {
		return sortString(string, null);
	}
	
	public static <T> List<T> deepCopy(List<T> src) {
		if (isEmpty(src)) {
			return null;
		}
		try {
			 ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
			    ObjectOutputStream out = new ObjectOutputStream(byteOut);
			    out.writeObject(src);  

			    ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
			    ObjectInputStream in = new ObjectInputStream(byteIn);
			    @SuppressWarnings("unchecked")
                List<T> dest = (List<T>) in.readObject();
			    return dest;  
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static <T> T deepCopy(T src) {
		if (isEmpty(src)) {
			return null;
		}
		try {
			 ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
			    ObjectOutputStream out = new ObjectOutputStream(byteOut);
			    out.writeObject(src);  

			    ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
			    ObjectInputStream in = new ObjectInputStream(byteIn);
			    @SuppressWarnings("unchecked")
			    T dest = (T) in.readObject();  
			    return dest;  
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static boolean checkValue(String regrex, String value) {
		Pattern pattern = Pattern.compile(regrex);
		Matcher matcher = pattern.matcher(value);
		return matcher.matches();
	}
	
	public static boolean checkDate(String date, String...dateFormats) {
		String[] dateFormat = dateFormats;
		if (dateFormats == null || dateFormats.length == 0) {
			dateFormat = new String[]{ "yyyy-MM-dd", "yyyy/MM/dd", "yyyy年MM月dd日", "yyyy-MM", "yyyy/MM", "yyyy年MM月", "yyyyMMdd",
			"yyyyMM" };	
		}
		for (int i = 0; i < dateFormat.length; i++) {
			boolean success = false;
			try {
				SimpleDateFormat sdf = new SimpleDateFormat(dateFormat[i]);
				sdf.setLenient(false);
				Date d = sdf.parse(date);
				System.out.println("日期转化后：" + DateUtil.format(d, dateFormat[i]));
				success = true;
			} catch (Exception e) {
				System.out.println(dateFormat[i] + "====>" + e.getMessage());
				success = false;
			}
			if (success) {
				System.out.println("时间格式：" + dateFormat[i] + ", 日期：" + date);
				return true;
			}
		}
		return false;
	}
	
}
