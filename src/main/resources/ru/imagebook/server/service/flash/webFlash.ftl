<div style="width: ${(width * 2 + 2)?c}px; height: ${(height + 2)?c}px; background-color: #f0f0f0; display: table-cell; vertical-align: middle;">
	<div style="width: ${(width * 2)?c}px; height: ${height?c}px; margin: auto;">
		<object width="${(width * 2)?c}" height="${height?c}">
			<param name="movie" value="/static/web/flash/book15.swf">
			<param name="FlashVars" value="xmlFile=${webPrefix}/webXml?a=${sessionId}">
			<embed
				src="/static/web/flash/book15.swf"
				width="${(width * 2)?c}"
				height="${height?c}"
				FlashVars="xmlFile=${webPrefix}/webXml?a=${sessionId}">
			</embed>
		</object>
	</div>
</div>