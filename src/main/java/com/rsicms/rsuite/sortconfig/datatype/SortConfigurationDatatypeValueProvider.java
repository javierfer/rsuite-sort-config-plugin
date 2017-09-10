package com.rsicms.rsuite.sortconfig.datatype;

import static com.rsicms.rsuite.sortconfig.ConfigSortConstants.*;

import java.util.*;

import org.apache.commons.lang.*;

import com.reallysi.rsuite.api.*;
import com.reallysi.rsuite.api.forms.*;
import com.rsicms.rsuite.sortconfig.utils.*;

public class SortConfigurationDatatypeValueProvider extends DefaultDataTypeOptionValuesProviderHandler {

	@Override
	public void provideOptionValues(DataTypeProviderOptionValuesContext context, List<DataTypeOptionValue> optionValues)
			throws RSuiteException {

		User user = context.getUser();

		List<DataTypeOptionValue> templateList = new ArrayList<DataTypeOptionValue>();

		String configRSuitePath = context.getConfigurationProperties().getProperty(SORT_CONFIG_RSUITE_PATH,
				SORT_CONFIG_RSUITE_PATH_DEFAULT);

		String[] path = StringUtils.split(configRSuitePath, "/");

		List<ManagedObject> transformMos = MOUtils.getManagedObjectsFromPath(context, user, path);

		for (ManagedObject mo : transformMos) {
			DataTypeOptionValue dtoValue = new DataTypeOptionValue(mo.getId(), mo.getDisplayName());
			templateList.add(dtoValue);
		}

		optionValues.addAll(templateList);
	}
}