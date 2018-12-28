Private i, x

On Error Resume Next

MM_FlashControlInstalled = False

For i = 6 To 1 Step -1
   Set x = CreateObject("ShockwaveFlash.ShockwaveFlash." & i)

   MM_FlashControlInstalled = IsObject(x)

   If MM_FlashControlInstalled Then
       MM_FlashControlVersion = CStr(i)
       Exit For
   End If
Next
