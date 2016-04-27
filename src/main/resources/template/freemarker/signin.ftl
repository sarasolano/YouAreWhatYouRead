<#assign content>

<form id="sign-in">
		<label for="username">Username</label><br>
        <input type="text" id="username" name="username">
        <span id="username-err" style="color: red; display: none">Required field.</span><br>
        <label for="pwd">Password</label><br>
        <input type="password" id="pwd" name="pwd">
        <span id="pwd-err" style="color: red; display: none">Password must be 8-36 characters long.</span><br>
</form> 
        

</#assign>
<#include "main.ftl">