
//Rhino doesn't provide console but has 'print'
var console;
if (!console){
	console={
		log:print, 
		error:print
	}
}

try {
	console.log("Finished executing engine-init.js");		
} catch (e){
	// turning off console because of silly old HtmlUnit
	console={
		log:function(){}, 
		error:function(){}
	}
	
}
