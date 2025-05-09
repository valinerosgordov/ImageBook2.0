<div id="${id}" class="pageFlip-imagesPanel">
</div>
<script type="text/javascript">
	pageFlip("${id}", ${albumWidth}, ${albumHeight}, ${nPages}, ${nInitialPages}, ${hard?string}, function(page) {
		return "${imageUrlPattern}".replace("${"$"}{page}", page); 
	});
</script>