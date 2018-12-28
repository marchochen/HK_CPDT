Function TestActiveX(objectID)
            on error resume next
            TestActiveX = IsObject(CreateObject(objectID))
End Function


