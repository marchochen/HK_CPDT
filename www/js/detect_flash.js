function getIEPlayerVersions()
    {
   
     if      ( TestActiveX('ShockwaveFlash.ShockwaveFlash.4'))  { document.fversion = 4 }
       else if ( TestActiveX('ShockwaveFlash.ShockwaveFlash.3'))  { document.fversion = 3 }
       else if ( TestActiveX('ShockwaveFlash.ShockwaveFlash.1'))  { document.fversion = 2 }
       else { document.fversion = 0 }
    }
	
function getNSPlayerVersions()
    {
	 document.fversion = 0 

            thearray = navigator.plugins
            arraylen = thearray.length
    
            for (i=0;i<arraylen;i++)
            {
                    theplugin = thearray[i]
                    thename   = theplugin.name
                    thedesc   = theplugin.description
     
                     if (thedesc.indexOf('Shockwave Flash 4.') != -1) { document.fversion = 4 }
                     else if (thedesc.indexOf('Shockwave Flash 3.') != -1) { document.fversion = 3 }
                     else if (thename.indexOf('Shockwave Flash 2.') != -1) { document.fversion = 2 }
            }

    }
	
function checkFlash() { 

   if (navigator.appName.indexOf('Netscape') != -1) {
	getNSPlayerVersions();
  } 
  
  else if (navigator.appName.indexOf('Microsoft') != -1) {
  	getIEPlayerVersions();
  }

	return document.fversion
}
