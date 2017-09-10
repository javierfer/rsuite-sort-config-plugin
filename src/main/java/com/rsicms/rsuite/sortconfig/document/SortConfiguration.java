package com.rsicms.rsuite.sortconfig.document;

import java.util.*;

import org.xmlbeam.annotation.*;

/**
 * Sort Configuration document projection.<br>
 * 
 * @see <a href="https://xmlbeam.org">https://xmlbeam.org</a>
 */
public interface SortConfiguration {

	@XBRead("/sort-configuration/*")
	List<SortConfiguration> getOrders();

	@XBRead(".")
	String getOrderValue();

	@XBRead("@alias")
	boolean isAlias();

	@XBRead("@startsWith")
	boolean isStartsWith();

	@XBRead("@endsWith")
	boolean isEndsWith();

	@XBRead("@sort")
	String getSort();

	@XBRead("@sort")
	String getSortMethod();
}
