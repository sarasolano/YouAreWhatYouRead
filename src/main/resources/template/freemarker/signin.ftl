<#assign content>

<form id="login">
		<label for="username">Username</label><br>
        <input type="text" id="username" name="username">
        <span id="username-err" style="color: red;">Required field.</span><br>
        <label for="pwd">Password</label><br>
        <input type="password" id="pwd" name="pwd">
        <span id="pwd-err" style="color: red;">Password must be 8-36 characters long.</span><br>
				<button type="submit" form="form1" value="Submit">Submit</button>
				<spam id="sub-err" style="color: red;">Invalid username or passowrd</span><br>
</form> 
        

</#assign>
<#include "main.ftl">