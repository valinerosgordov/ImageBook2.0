<object width="${width?c}" height="${height?c}">
  <param name="movie" value="http://${flashUrl}/static/flash/publish/album.swf">
  <param name="FlashVars" value="xmlFile=http://${flashUrl}/publish/xml/${orderCode}/${width?c}">
  <embed src="http://${flashUrl}/static/flash/publish/album.swf"
    width="${width?c}" height="${height?c}"
    FlashVars="xmlFile=http://${flashUrl}/publish/xml/${orderCode}/${width?c}">
  </embed>
</object>