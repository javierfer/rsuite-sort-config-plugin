package com.rsicms.rsuite.sortconfig.utils;

import static com.google.common.base.CharMatcher.*;
import static com.reallysi.rsuite.api.ObjectType.*;
import static java.lang.String.*;

import java.util.*;

import org.apache.commons.logging.*;

import com.reallysi.rsuite.api.*;
import com.reallysi.rsuite.api.extensions.*;
import com.reallysi.rsuite.service.*;
import com.rsicms.rsuite.helpers.utils.*;

/**
 * A collection of static MO utility methods.
 */
public class MOUtils {

	protected static Log log = LogFactory.getLog(MOUtils.class);

	private MOUtils() {
	}

	/**
	 * Get all the managed objects from a given path
	 * 
	 * @param context
	 * @param user
	 * @param path
	 * @return list of managed objects
	 * @throws RSuiteException
	 */
	public static List<ManagedObject> getManagedObjectsFromPath(ExecutionContext context, User user, String[] path)
			throws RSuiteException {
		List<ManagedObject> mos = new ArrayList<ManagedObject>();
		ContentAssemblyNodeContainer container = ContainerUtils.getContainerFromPath(context, user, path);

		if (container != null) {
			ManagedObjectService moService = context.getManagedObjectService();

			for (ContentAssemblyItem item : container.getChildrenObjects()) {
				ObjectType objectType = item.getObjectType();
				if (objectType == MANAGED_OBJECT_REF) {
					ManagedObjectReference moRef = (ManagedObjectReference) item;
					ManagedObject mo = moService.getManagedObject(user, moRef.getTargetId());
					mos.add(mo);
				}
			}
		}
		return mos;
	}

	/**
	 * Sorts managed objects in memory.
	 * @param sortType
	 * @param sortMethod
	 * @param objectList
	 * @throws RSuiteException
	 */
	public static void sortMoList(final String sortType, final String sortMethod, List<ManagedObject> objectList)
			throws RSuiteException {
		if (objectList != null) {
			Collections.sort(objectList, new Comparator<ManagedObject>() {
				public int compare(ManagedObject mo1, ManagedObject mo2) {
					String mo1Name = "";
					String mo2Name = "";
					try {
						mo1Name = mo1.getDisplayName() != null ? mo1.getDisplayName() : "";
						mo2Name = mo2.getDisplayName() != null ? mo2.getDisplayName() : "";
					} catch (RSuiteException e) {
						log.error(format("Unexpected error getting display name: %s - %s", RSuiteUtils.formatMoId(mo1),
								RSuiteUtils.formatMoId(mo2)));
					}

					if ("num".equals(sortMethod)) {
						Long mo1NameValue = Long.valueOf(JAVA_DIGIT.retainFrom(mo1Name));
						Long mo2NameValue = Long.valueOf(JAVA_DIGIT.retainFrom(mo2Name));
						if (sortType.contains("desc")) {
							return mo2NameValue.compareTo(mo1NameValue);
						} else {
							return mo1NameValue.compareTo(mo2NameValue);
						}
					}
					if (sortType.contains("desc")) {
						return mo2Name.compareToIgnoreCase(mo1Name);
					} else {
						return mo1Name.compareToIgnoreCase(mo2Name);
					}
				}
			});
		}
	}
}
