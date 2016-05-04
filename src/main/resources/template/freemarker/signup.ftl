<#assign content>

<form id="sign-up">
		<label for="username">Username</label><br>
		<input type="text" id="username" name="username">
		<span id="username-err" style="color: red; display: none">Required field.</span><br>
		<span id="repeat-err" style="color: red; display: none">Username already exists</span><br>
		
		<label for="pwd">Create Password</label><br>
		<input type="password" id="pwd" name="pwd">
		<span id="pwd-err" style="color: red; display: none">Password must be 8-36 characters long.</span><br>
		<label for="pwd2">Repeat Password</label><br>
		<input type="password" id="pwd2" name="pwd2">
		<span id="pwd2-err" style="color: red; display: none">Passwords don't match</span><br>
		
		<label for="pwd:
		<button id="submit" type="submit" form="form1" value="Submit">Submit</button>
</form> 
        

</#assign>
<#include "main.ftl">