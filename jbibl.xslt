<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="3.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:fn="http://www.w3.org/2005/xpath-functions">
	<xsl:template match="/">
		<html>
			<head>
				<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></meta>
				<link rel="stylesheet" href="main.css" type="text/css"></link>
				<title></title>
			</head>
			<body>
				<table width="100%">
					<thead>
						<tr class="caption">
							<th>File Name</th>
							<th>Title</th>
							<th>Author</th>
							<th>Tags</th>
							<th>File Size</th>
						</tr>
					</thead>
					<tbody>
						<xsl:apply-templates select="/jbibl/files/file"></xsl:apply-templates>
					</tbody>
					<xsl:apply-templates select="/jbibl/settings/setting"></xsl:apply-templates>
				</table>
			</body>
		</html>
	</xsl:template>
	<xsl:template match="/jbibl/settings/setting">
		<tfoot>
			<tr class="footer">
				<th>Settings used - root dir: <xsl:value-of select="root_dir"></xsl:value-of>, file types: <xsl:value-of select="harvest_filetype"></xsl:value-of>, log level: <xsl:value-of select="log_level"></xsl:value-of></th>
			</tr>
		</tfoot>
	</xsl:template>
	<xsl:template match="/jbibl/files/file">
					<tr class="data">
						<th class="fullpath">
							<xsl:element name="a">
								<xsl:attribute name="href">
									file:///<xsl:value-of select="@full_path"></xsl:value-of>
								</xsl:attribute>
								<xsl:attribute name="title">
									<xsl:value-of select="@full_path"></xsl:value-of>
								</xsl:attribute>
								<xsl:attribute name="target">
									_blank
								</xsl:attribute>
								<xsl:value-of select="file_name"></xsl:value-of>
							</xsl:element>
						</th>
						<th class="title"><xsl:value-of select="title"></xsl:value-of></th>
						<th class="author"><xsl:value-of select="author"></xsl:value-of></th>
						<th class="tags"><xsl:value-of select="tags"></xsl:value-of></th>
						<th class="filesize"><xsl:value-of select="file_size"></xsl:value-of></th>
					</tr>
	</xsl:template>
</xsl:stylesheet>
