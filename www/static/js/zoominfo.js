
$(document).ready(function()
{
	$('.galleryImage').hover(
		function()
		{
		
		$(this).find('img').animate({width:115, marginTop:10, marginLeft:10}, 500);
		$(this).next('.fwimcont').hide();
		$(this).nextAll('.fwimbg').hide();
		   
		 },
		 function()
		 {
			 
			 $(this).find('img').animate({width:226, marginTop:0, marginLeft:0},300);
			 $(this).next('.fwimcont').show();
		     $(this).nextAll('.fwimbg').show();
			 
		 });
});

                       
                   