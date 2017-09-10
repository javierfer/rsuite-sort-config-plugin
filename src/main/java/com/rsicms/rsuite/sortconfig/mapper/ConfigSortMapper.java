package com.rsicms.rsuite.sortconfig.mapper;

import static org.apache.commons.lang.StringUtils.*;

import java.util.*;

import com.reallysi.rsuite.api.*;

public class ConfigSortMapper {

	Map<String, ManagedObject> displayNameMap = new HashMap<String, ManagedObject>();
	Map<String, ManagedObject> aliasMap = new HashMap<String, ManagedObject>();

	public void add(String displayName, String alias, ManagedObject mo) {
		displayNameMap.put(displayName, mo);
		aliasMap.put(alias, mo);
	}

	public ManagedObject getByDisplayName(String displayName) {
		return displayNameMap.get(displayName);
	}

	public ManagedObject getByAlias(String alias) {
		return aliasMap.get(alias);
	}

	public ManagedObject getByDisplayNameStartsWith(String prefix) {
		for (String dn : displayNameMap.keySet()) {
			if (startsWith(dn, prefix)) {
				return displayNameMap.get(dn);
			}
		}
		return null;
	}

	public ManagedObject getByDisplayNameEndsWith(String suffix) {
		for (String dn : displayNameMap.keySet()) {
			if (endsWith(dn, suffix)) {
				return displayNameMap.get(dn);
			}
		}
		return null;
	}

	public ManagedObject getByAliasStartsWith(String prefix) {
		for (String alias : aliasMap.keySet()) {
			if (startsWith(alias, prefix)) {
				return aliasMap.get(alias);
			}
		}
		return null;
	}

	public ManagedObject getByAliasEndsWith(String suffix) {
		for (String alias : aliasMap.keySet()) {
			if (endsWith(alias, suffix)) {
				return aliasMap.get(alias);
			}
		}
		return null;
	}

}
