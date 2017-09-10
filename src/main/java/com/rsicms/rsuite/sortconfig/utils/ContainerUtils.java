package com.rsicms.rsuite.sortconfig.utils;

import static com.reallysi.rsuite.api.RSuiteException.*;

import org.apache.commons.lang.*;

import com.reallysi.rsuite.api.*;
import com.reallysi.rsuite.api.extensions.*;
import com.rsicms.rsuite.helpers.utils.*;

/**
 * A collection of static CA utility methods.
 */
public class ContainerUtils {
	
	private ContainerUtils() {
	}

	/**
	 * Get content assembly node container from path.
	 * 
	 * @param context
	 * @param user
	 * @param pathParts
	 * @return content assembly node container
	 * @throws RSuiteException if no content assembly node container found
	 */
	public static ContentAssemblyNodeContainer getContainerFromPath(ExecutionContext context, User user,
			String[] pathParts) throws RSuiteException {
		ContentAssemblyNodeContainer parentContainer = context.getContentAssemblyService().getRootFolder(user);

		return getContainerFromPath(context, user, pathParts, parentContainer);
	}

	/**
	 * Get content assembly node container from path.
	 * 
	 * @param context
	 * @param user
	 * @param pathParts
	 * @param parentContainer
	 * @return
	 * @throws RSuiteException
	 */
	public static ContentAssemblyNodeContainer getContainerFromPath(ExecutionContext context, User user,
			String[] pathParts, ContentAssemblyNodeContainer parentContainer) throws RSuiteException {

		ContentAssemblyNodeContainer ca = RSuiteUtils.getCaContainerForPath(context, user, parentContainer, pathParts);

		if (ca == null) {
			// Report the problem to the user
			throw new RSuiteException(ERROR_URI_NOT_EXIST,
					"No content assembly node container found: " + StringUtils.join(pathParts, ","));
		}
		return ca;
	}

}
