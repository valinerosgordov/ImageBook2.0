/**
 * IMAGEBOOK.pickPoint module
 */
var IMAGEBOOK = (function (mainModule, $, PickPoint) {
	var module = mainModule.pickPoint = mainModule.pickPoint || {};

	module.showPickpointWizard = function () {
		PickPoint.open(pickpointCallback, {fromcity: 'Москва'});
	};

	var pickpointCallback = function (result) {
		var pickpointAddressField = $('.pickpointAddress');
		pickpointAddressField.val(result.address);
		$('.pickpointPostamateId').val(result.id);
		$('.pickpointRateZone').val(result.zone);
		$('.pickpointCoeff').val(result.coeff);
		pickpointAddressField.change();
		return false;
	};

	return mainModule;
}(IMAGEBOOK || {}, jQuery, PickPoint));
