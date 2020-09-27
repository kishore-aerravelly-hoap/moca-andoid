var currentPage = 0;
var maxPage = 1;
var pageSelector = "page";
var fontLevel = 4;
var fontMaxLevels = 7;
var fontMinLevels = 4;
var fontIncreaseTolerance =  1.2;
var fontDecreaseTolerance =  .80;
var level1 = 4;
var level2 = 5;
var level3 = 6;
var level4 = 7;
var currentSize = 4;

$(document).ready(function() {
	
	maxPage = $('.' + pageSelector).length;
	$('.' + pageSelector).hide();
	$('h6').hide();
	//showPage(1);
	
});

function n() {
	if (!goToPage((currentPage + 1))) {
		eReader.nextResource();
	}
}

function p() {
	if (!goToPage((currentPage - 1))) {
		eReader.previousResource(); 
	}
}

function l() {
	goToPage(maxPage);	
}


function goToPage(pageNo) {
	
	var status = true;
	hidePage(currentPage);
	// Ensure that we don't jump out of boundary
	if (pageNo > maxPage) {
		pageNo = maxPage;
		status = false;
	}
	if (pageNo <= 0) {                                                      
		pageNo = 1;
		status = false;
	}

	currentPage = pageNo;
	showPage(currentPage);
	
	return status;
}

function showPage(pageNo) {
	
	$("." + pageSelector + ":eq(" + (pageNo - 1) + ")").show();
	$("h6:eq("+(pageNo-1)+")").show();
	setCurrentPage();
}

function hidePage(pageNo) {
	$("." + pageSelector + ":eq(" + (pageNo - 1) + ")").hide();
	$("h6:eq("+(pageNo-1)+")").hide();   
}

function setCurrentPage(){
	eReader.setCurrentPage(currentPage);	
}

function differenceCount(currentSize,selectedSize){
	var count = 1;
	if(selectedSize >= currentSize){
		count = parseInt(selectedSize) - parseInt(currentSize);
	}else if(selectedSize <= currentSize){
		count = parseInt(currentSize) - parseInt(selectedSize);
	}
	//alert('count is: '+count);
	return count;
}

//BEGIN - Font increase / decrease functionality
function setInitialFont(currentSize1,selectedSize){
	//alert('current size iss: ' +currentSize1 +'selectedSize is: '+selectedSize);
	currentSize = currentSize1;
	var flag = true;
	if(currentSize > selectedSize){
		flag = false;
	}else if(currentSize < selectedSize){
		flag = true;
	}
	if(!flag){
		var count = differenceCount(currentSize, selectedSize);
		changeFontSize(count,'decrease',selectedSize);
		
	}else{
		var count = differenceCount(currentSize,selectedSize);
		changeFontSize(count,'increase',selectedSize);
	}	
	currentSize = parseInt(selectedSize);
}

function changeFontSize(deltaValue, type,selectedSize){
	var elements = new Array ("h1","h2","h3","h5","h6","div","ul","td","th","li","p","small");
	for (var i=0;i<elements.length;i++){
		var fontSize = $(elements[i]).css("font-size");
		if(fontSize != undefined){
			var newSize = 1;
			
			if(type == 'increase'){
				newSize = parseInt(fontSize.replace(/px/, "")) + deltaValue;
			}else if(type == 'decrease'){
				newSize = parseInt(fontSize.replace(/px/, "")) - deltaValue;
			}
		 $(elements[i]).css("font-size", newSize + "px");		
		}
		var lineHeight = $(elements[i]).css("line-height");
		if(fontSize != undefined){
			var newSize1 = 1;
			if(type == 'increase'){
				newSize1 = parseInt(lineHeight.replace(/px/, "")) + deltaValue;
			}else if(type == 'decrease'){
				newSize1 = parseInt(lineHeight.replace(/px/, "")) - deltaValue;
			}
			$(elements[i]).css("line-height", newSize1 + "px");
		}
	}
	
	setFontLevel(selectedSize);
}
function setFontLevel(selectedSize){
	eReader.setFontLevel(selectedSize);
}


//END - Font increase / decrease functionality