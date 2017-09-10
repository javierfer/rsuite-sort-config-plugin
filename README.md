# rsuite-sort-config-plugin

Sort managed objects and/or content assemblies by configuration.

## configuration file sample

See samples in config-samples

```
<!DOCTYPE sort-configuration
  PUBLIC 'urn:pubid:harpercollins.com:doctypes:sort:configuration'
  'sort-config.dtd'>
<!-- 
Elements/Attributes configuration:
	
	sort-configuration: root element
		@title: (optional) title of the sort configuration.
	
	order: Defines the relative order of the object. Supports either managed objects or content assemblies.
			The value is the object name pattern to match.
		@alias: (true|false, optional, default false) true if using alias, false if using display name.
		@startsWith: (true|false, optional, default false) true if matching object name starts with this value.
		@endsWith: (true|false, optional, default false) true if matching object name ends with this value.
		@sort: (asc|desc, optional, default asc) only applies to content assemblies.
		@sortMethod: (num|string, optional, default string) only applies to content assemblies. If "num", the
			numeric part of the object name is retained to use for the sorting operation.
-->
<sort-configuration title="Bible Chapter Sort">
	<order alias="false" startsWith="false" endsWith="true">Ge.dita</order>
	<order alias="false" startsWith="false" endsWith="false">KJV_BIBLE_BOOKS_FULL_before_Ex.dita</order>
	<order alias="false" startsWith="false" endsWith="true">Lev.dita</order>
	<order alias="false" startsWith="false" endsWith="true">Nu.dita</order>
	<order alias="false" startsWith="false" endsWith="true">Dt.dita</order>
</sort-configuration>

```