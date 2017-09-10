package com.rsicms.rsuite.sortconfig.webservice;

import static java.lang.String.*;

import org.apache.commons.logging.*;

import com.reallysi.rsuite.api.*;
import com.reallysi.rsuite.api.remoteapi.*;
import com.reallysi.rsuite.api.remoteapi.result.*;
import com.rsicms.rsuite.helpers.webservice.*;
import com.rsicms.rsuite.sortconfig.service.*;

public class ConfigSortWebService extends RemoteApiHandlerBase {

	protected static Log log = LogFactory.getLog(ConfigSortWebService.class);
	protected static final String PARAM_SORT_CONFIG = "sort-config";

	@Override
	public RemoteApiResult execute(RemoteApiExecutionContext context, CallArgumentList args) throws RSuiteException {

		String caId = args.getFirstString("rsuiteId");
		User user = context.getSession().getUser();
		String configMoId = args.getFirstString(PARAM_SORT_CONFIG);

		try {
			ConfigSortService configSortService = new ConfigSortService(context, user); 

			ContentAssemblyNodeContainer contentAssemblyNodeContainer = 
					context.getContentAssemblyService().getContentAssemblyNodeContainer(user, caId);

			String messages = configSortService.sort(contentAssemblyNodeContainer, configMoId);

			MessageDialogResult result = new MessageDialogResult(format("Sort - '%s'", 
					contentAssemblyNodeContainer.getDisplayName()), 
					messages);

			UserInterfaceAction refreshAction = new UserInterfaceAction("rsuite:refreshManagedObjects");
			refreshAction.addProperty("objects", caId);
			refreshAction.addProperty("children", true);
			result.addAction(refreshAction);

			return result;
		} catch (Exception e) {
			log.error(e.getLocalizedMessage(), e);
			return new MessageDialogResult("Error", e.getLocalizedMessage());
		}
	}
}
