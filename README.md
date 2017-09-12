# rsuite-sort-config-plugin

Sorts managed objects and/or content assemblies by configuration.

Sometimes users want to set a defined order for certain content, where the traditional ascending/descending alphabetical order does not fit.

This plugin allows the use of different configuration files to define orders. The sample below is used to sort Bible content, like the Pentateuch. 

(Compatible with v5.x.x)

## configuration file sample

See samples in config-samples

```
<!DOCTYPE sort-configuration
  PUBLIC 'urn:pubid:rsicms.com:doctypes:sort:configuration'
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
		@sort: (asc|desc, optional, default asc) only applies to content assemblies, to sort children.
		@sortMethod: (num|string, optional, default string) only applies to content assemblies. If "num", the
			numeric part of the object name is retained to use for the children sort.
-->
<sort-configuration title="Bible Chapter Sort">
	<order alias="false" startsWith="false" endsWith="true">Ge.dita</order>
	<order alias="false" startsWith="false" endsWith="false">KJV_BIBLE_BOOKS_FULL_before_Ex.dita</order>
	<order alias="false" startsWith="false" endsWith="true">Lev.dita</order>
	<order alias="false" startsWith="false" endsWith="true">Nu.dita</order>
	<order alias="false" startsWith="false" endsWith="true">Dt.dita</order>
</sort-configuration>

```

## RSuite configuration

By default, configuration files are looked up in "Support/Sort Configurations". To define a different place set the following property: 
* sort.config.rsuite.path

Use "/" as the folder hierarchy separator.

## RSuite action menu

The action menu is visible on any folder. It is available on "Organize/Sort by configuration..."
