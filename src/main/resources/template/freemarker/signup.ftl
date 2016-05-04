<#assign content>

<!--
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
-->

	<div class="container">
		<div class="row">
			<div class="Absolute-Center is-Responsive">
				<div id="logo-container"></div>
				<div class="col-sm-12 col-md-10 col-md-offset-1">
					<form action="">
						<div class="form-group input-group">
							<span class="input-group-addon"><i class="glyphicon glyphicon-user"></i></span>
							<input id="first" class="form-control" type="text" name='first' placeholder="full name"/>
						</div>
						<div class="form-group input-group">
							<span class="input-group-addon"><i class="glyphicon glyphicon-user"></i></span>
							<input id="username" class="form-control" type="text" name='username' placeholder="username"/>
						</div>
						<span id="username-err" style="color: red; display: none">Required field.</span>

						<div class="form-group input-group">
							<span class="input-group-addon"><i class="glyphicon glyphicon-lock"></i></span>
							<input id="pwd" class="form-control" type="password" name='password' placeholder="password"/>
						</div>
						<span id="pwd-err" style="color: red; display: none">Password must be 8-36 characters long.</span>

						<div class="form-group input-group">
							<span class="input-group-addon"><i class="glyphicon glyphicon-lock"></i></span>
							<input id="pwd2" class="form-control" type="password" name='password' placeholder="repeat password"/>	
						</div>
						<span id="pwd2-err" style="color: red; display: none">Passwords don't match</span>

						<div class="form-group">
							<button id="submit" type="button" class="btn btn-def btn-block">Signup</button>
						</div>
						<span id="sub-err" style="color: red; display: none">Invalid username or passowrd</span>
					</form>        
				</div>  
			</div>    
		</div>
	</div>
</#assign>
<#include "main.ftl">