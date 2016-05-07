<#assign content>
	<div class="container">
		<div class="row">
			<div class="Absolute-Center is-Responsive signup">
				<div id="logo-container"></div>
				<div class="col-sm-12 col-md-10 col-md-offset-1">
					<form action="">
						<div class="form-group input-group">
							<span class="input-group-addon"><i class="glyphicon glyphicon-user"></i></span>
							<input id="first" class="form-control" type="text" name='first' placeholder="full name"/>
						</div>
						<span id="name-err" style="color: red; display: none">Required field.</span>
						<div class="form-group input-group">
							<span class="input-group-addon"><i class="glyphicon glyphicon-user"></i></span>
							<input id="username-signup" class="form-control" type="text" name='username' placeholder="username"/>
						</div>
						<span id="username-err" style="color: red; display: none">Required field.</span>

						<div class="form-group input-group">
							<span class="input-group-addon"><i class="glyphicon glyphicon-lock"></i></span>
							<input id="pwd-signup" class="form-control" type="password" name='password' placeholder="password"/>
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
						<span id="sub-err" style="color: red; display: none">Username already exists</span>
<!--						<div id = "success" class="alert alert-success" role="alert" style="display: none">Your account has been created</div>-->
						<form method="GET" action="/signup">
							<div class="form-group text-center">
								<span>Already have an account? <a href="/signin">Sign in</a>
							</div>
						</form>	
					</form>        
				</div>  
			</div>    
		</div>
	</div>
</#assign>
<#include "main.ftl">