<#assign content>
	<nav class="navbar navbar-default navbar-fixed-top">
		<div class="container">
			<a class="navbar-brand" href="/home"><img
             src="../img/redient2.gif"></a>
			<ul class="nav navbar-nav navbar-right">
				<li class="dropdown">
					<a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">${username}<span class="caret"></span></a>
					<ul class="dropdown-menu">
						<li><a id="profile" href="#">View Profile</a></li>
						<li><a id="logout" href="#">Log Out</a></li>
					</ul>
				</li>
			</ul>
		</div>
		</div>
	</nav>
	<div class="alert alert-warning hide" id="failure">
  		<strong>Warning!</strong> Article was already removed from database.
	</div>
	<div class="alert alert-success hide" id="s">
  		<strong>Success!</strong> Article was successfully deleted from the database!
	</div>
	${page}
	<nav class="navbar navbar-default navbar-fixed-bottom">
		<div class="container">
			<button type="button" id="trash" class="btn btn-default navbar-btn"><span class="glyphicon glyphicon-trash"></span></button>
			<p class="navbar-text navbar-right" id="added"></p>
		</div>
	</nav>
</#assign>
<#include "main.ftl">