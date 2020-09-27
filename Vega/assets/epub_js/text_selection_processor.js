/*
function showSelection() {
    if ((undefined != document.selection)&&(document.selection)) {
        //alert(document.selection.createRange().text);
        
        console.log("text-selection-if:"+document.selection.createRange().text);
        
        event.cancelBubble = true;
    }else if((undefined != document.getSelection())&&(document.getSelection())){
    	//alert(document.getSelection().toString());
    	
        console.log("text-selection-else if:"+document.getSelection().toString());
    }else{
    	//alert("else");
        console.log("text-selection-else");
    }
}

$(document).ready(function(){
	//document.onmouseup = showSelection;
	
	document.addEventListener("touchend", function(event) { 
		showSelection();
	}, false);
	
	document.addEventListener("mouseup", function(event) { 
		showSelection();
	}, false);
});
*/