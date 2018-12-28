<%
Dim lmsURL
Dim urlFailure
Dim lrnRole
Dim siteId
Dim lmsQdbServletURL
Dim usrID
Dim refer
Dim module
Dim encoding

lmsURL = "yourhost:yourport"
urlFailure = "yourhost1:yourport1"
'the user will login system with follow role.must be the learner role of the system
lrnRole = "NLRN_1"
siteId = "1"
refer = request.ServerVariables("HTTP_REFERER")

lmsQdbServletURL = lmsURL + "servlet/Dispatcher"
usrID = GetUserID(Request.ServerVariables("Logon_User"))
module = "login.LoginModule"
encoding = "UTF-8"

function GetUserID(username)
    DIM pos
    pos = instr(1, username, "\")
    if pos = 0 then
        GetUserID = trim(username)
    else
        GetUserID = GetUserID(mid(username, pos + 1))
    end if
end function
%>