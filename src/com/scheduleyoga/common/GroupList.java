package com.scheduleyoga.common;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.StringUtils;

public class GroupList<K, V> {

	/**
	 * Groups objects using field name provided
	 * 
	 * @param list
	 *            the list of objects to group
	 * @param field
	 *            the field to group the objects by
	 * @return new Map containing list of objects. The Map's key is the property
	 *         specified in criteria object
	 */
	public static <K, V> Map<K, List<V>> group(List<V> list, String field) {


		
		// used for indexing to avoid too much looping
		HashMap<String, K> index = new HashMap<String, K>();
		// the container for grouped data. K is the data type for the field's
		// value.
		HashMap<K, List<V>> groupedData = new HashMap<K, List<V>>();

		if(list.size()<=0){
			return groupedData;
		}
		
		// the method to invoke to get the value of the 'field' prop from
		// objects
		Method m = null;

		try {
			V obj = list.get(0);
			// Used a handy Util Class's method for getting the getter method
			// for 'field'
			m = obj.getClass().getMethod("get" + StringUtils.capitalize(field));

			// m = ClassUtil.getMethod(obj.getClass(),
			// "get" + StringUtils.capitalize(field));
		} catch (NoSuchMethodException ex) {
			Logger.getLogger(ListUtils.class.getName()).log(Level.SEVERE, null,
					ex);

			return null;
		}

		// iterate through the list of objects passed as parameter
		for (V object : list) {

			// the 'field' value of object
			K columnVal = null;

			try {
				columnVal = (K) m.invoke(object, null);

				// group of objects under columnVal group
				List<V> group = groupedData
						.get(index.get(columnVal.toString()));

				if (group == null) {
					group = new ArrayList<V>();
					groupedData.put(columnVal, group);
					index.put(columnVal.toString(), columnVal);
				}
				// add the object to the group
				group.add(object);

			} catch (Exception ex) {
				Logger.getLogger(ListUtils.class.getName()).log(Level.SEVERE,
						null, ex);
			}
		}

		return groupedData;
	}
}
