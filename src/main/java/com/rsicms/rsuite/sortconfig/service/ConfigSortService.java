package com.rsicms.rsuite.sortconfig.service;

import static com.reallysi.rsuite.api.ObjectType.*;
import static com.reallysi.rsuite.api.RSuiteException.*;
import static com.rsicms.rsuite.sortconfig.ConfigSortConstants.*;
import static java.lang.String.*;
import static org.apache.commons.lang.StringUtils.*;

import java.util.*;

import org.apache.commons.logging.*;
import org.xmlbeam.*;

import com.reallysi.rsuite.api.*;
import com.reallysi.rsuite.api.control.*;
import com.reallysi.rsuite.api.extensions.*;
import com.reallysi.rsuite.api.tools.*;
import com.reallysi.rsuite.service.*;
import com.rsicms.rsuite.sortconfig.document.*;
import com.rsicms.rsuite.sortconfig.mapper.*;
import com.rsicms.rsuite.sortconfig.utils.*;

public class ConfigSortService {

	protected static Log log = LogFactory.getLog(ConfigSortService.class);

	private ExecutionContext context;
	private User user;
	private ContentAssemblyService caService;
	private ManagedObjectService moService;
	private AliasHelper aliasHelper;

	public ConfigSortService(ExecutionContext context, User user) {
		this.context = context;
		this.caService = context.getContentAssemblyService();
		this.moService = context.getManagedObjectService();
		this.aliasHelper = context.getManagedObjectService().getAliasHelper();
		this.user = user;
	}

	/**
	 * Sorts the content assembly by a sort configuration
	 * @param contentAssemblyNodeContainer
	 * @param configMoId 
	 * @return results
	 * @throws RSuiteException
	 */
	public String sort(ContentAssemblyNodeContainer contentAssemblyNodeContainer, String configMoId) throws RSuiteException {

		ManagedObject configMo = moService.getManagedObject(user, configMoId);

		String configRSuitePath = context.getConfigurationProperties().getProperty(SORT_CONFIG_RSUITE_PATH,
				SORT_CONFIG_RSUITE_PATH_DEFAULT);

		if (configMo == null) {
			throw new RSuiteException(ERROR_CONFIGURATION_PROBLEM,
					String.format("No configurations found in '%s'", configRSuitePath));
		}

		// Get the sort mapper
		ConfigSortMapper configSortMapper = buildConfigSortMapper(contentAssemblyNodeContainer);

		List<SortConfiguration> orders = getAllSortConfigurationOrders(configMo);

		int position = 0;
		StringBuilder matchMsgs = new StringBuilder("<br><b>Matches for:</b><br>");
		StringBuilder noMatchMsgs = new StringBuilder("<br><b>No matches for:</b><br>");

		for (SortConfiguration order : orders) {

			ManagedObject moToSort;

			if (order.isAlias()) {
				if (order.isStartsWith()) {
					moToSort = configSortMapper.getByAliasStartsWith(order.getOrderValue());
				} else if (order.isEndsWith()) {
					moToSort = configSortMapper.getByAliasEndsWith(order.getOrderValue());
				} else {
					moToSort = configSortMapper.getByAlias(order.getOrderValue());
				}
			} else {
				if (order.isStartsWith()) {
					moToSort = configSortMapper.getByDisplayNameStartsWith(order.getOrderValue());
				} else if (order.isEndsWith()) {
					moToSort = configSortMapper.getByDisplayNameEndsWith(order.getOrderValue());
				} else {
					moToSort = configSortMapper.getByDisplayName(order.getOrderValue());
				}
			}

			if (moToSort != null) {
				matchMsgs.append(format("%s<br>", order.getOrderValue()));

				ObjectReferenceMoveOptions opts = new ObjectReferenceMoveOptions();
				opts.setPosition(position++);
				caService.moveReference(user, moToSort.getId(), contentAssemblyNodeContainer.getId(), opts);
				
				if (isNotBlank(order.getSort()) && moToSort.getObjectType().equals(CONTENT_ASSEMBLY_REF)) {
					sortChildren(order, moToSort);
				}
			} else {
				noMatchMsgs.append(format("%s<br>", order.getOrderValue()));
			}
		}

		return matchMsgs.append(noMatchMsgs).append("<br><br>Sort finished.").toString();

	}

	/**
	 * Sorts children of the content assembly by configuration
	 * 
	 * @param order
	 * @param mo
	 * @throws RSuiteException
	 */
	public void sortChildren(SortConfiguration order, ManagedObject mo) throws RSuiteException {

		ContentAssemblyItem ca = caService.getContentAssemblyNodeContainer(user, mo.getTargetId());

		List<? extends ContentAssemblyItem> kids = ca.getChildrenObjects();
		List<ManagedObject> referencedKids = new ArrayList<ManagedObject>();
		
		// Add kids to list
		for (ContentAssemblyItem kid : kids) {
			ManagedObject kidMo = moService.getManagedObject(user, kid.getId());
			referencedKids.add(kidMo);
		}
		
		// Sort the kid's list
		MOUtils.sortMoList(order.getSort(), order.getSortMethod(), referencedKids);
		
		// Reorder kids
		int position = 0;
		for (ManagedObject moKid : referencedKids) {
			ObjectReferenceMoveOptions options = new ObjectReferenceMoveOptions();
			options.setPosition(position++);
			caService.moveReference(user, moKid.getId(), ca.getId(), options);
		}

	}

	/**
	 * Gets all <order> configuration elements.
	 * 
	 * @param configMo
	 * @return SortConfiguration
	 * @throws RSuiteException
	 */
	public List<SortConfiguration> getAllSortConfigurationOrders(ManagedObject configMo) throws RSuiteException {
		XBProjector projector = new XBProjector();

		SortConfiguration sortConfigs = projector.projectDOMNode(configMo.getElement(), SortConfiguration.class);

		return sortConfigs.getOrders();
	}

	/**
	 * Maps managed object display names and aliases
	 * 
	 * @param ca
	 * @return configuration sort mapper object
	 * @throws RSuiteException
	 */
	public ConfigSortMapper buildConfigSortMapper(ContentAssemblyNodeContainer ca) throws RSuiteException {
		ConfigSortMapper configSortMapper = new ConfigSortMapper();

		List<? extends ContentAssemblyItem> kids = ca.getChildrenObjects();

		for (ContentAssemblyItem kid : kids) {
			ManagedObject refMo = moService.getManagedObject(user, kid.getId());

			configSortMapper.add(refMo.getDisplayName(), aliasHelper.getFilename(user, refMo), refMo);
		}

		return configSortMapper;
	}
	
}
