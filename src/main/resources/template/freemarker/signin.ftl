<#assign content>
<div class="container">
	<div class="row">
		<div class="Absolute-Center is-Responsive">
			<div id="logo-container"></div>
			<div class="col-sm-12 col-md-10 col-md-offset-1">
				<form action="">
					<div class="form-group input-group">
						<span class="input-group-addon"><i class="glyphicon glyphicon-user"></i></span>
						<input id="uname" class="form-control" type="text" name='username' placeholder="username"/>
					</div>

					<div class="form-group input-group">
						<span class="input-group-addon"><i class="glyphicon glyphicon-lock"></i></span>
						<input id="passoword" class="form-control" type="password" name='password' placeholder="password"/>
					</div>

					<div class="form-group">
						<button id="login" type="button" class="btn btn-def btn-block">Login</button>
						<span id="sub-err" style="color: red; display: none">Invalid username or password</span><br>
					</div>
					<form method="GET" action="/signup">
						<div class="form-group text-center">
							<span>New to Readient? <a href="/signup">Create an account</a>
						</div>
					</form>	
				</form>        
			</div>  
		</div>    
	</div>
</div>
</#assign>
<#include "main.ftl">