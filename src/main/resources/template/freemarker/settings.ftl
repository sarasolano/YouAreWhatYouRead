<#assign content>
	<nav class="navbar navbar-default navbar-fixed-top">
		<div class="container">
			<a class="navbar-brand" href="#">Readient</a>
			<ul class="nav navbar-nav navbar-right">
				<li class="dropdown">
					<a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">${username}<span class="caret"></span></a>
					<ul class="dropdown-menu">
						<li><a href="#">View Profile</a></li>
						<li><a id="logout" href="#">Log Out</a></li>
						<li><a id="settings" href="#">Settings</a></li>
					</ul>
				</li>
			</ul>
		</div>
		</div>
	</nav>
	<div class="jumbotron">
		<div class="container">
			<div class="jumbotron">
				<div id="border">
					<h1>Readient</h1>
					<p>You are what you read.</p>
				</div>
			</div>
		</div>
		<div class="container input-group">
			<input id ="url-input"type="text" class="form-control" placeholder="Add an article!">
			<span class="input-group-btn">
			<button id="plus" class="btn btn-default" type="submit"><span class="glyphicon glyphicon-thumbs-up"></span></button>
			<button id = "add-article" class="btn btn-default" type="submit"><span class="glyphicon glyphicon-plus"></span></button>
			<button id ="minus" class="btn btn-default" type="submit"><span class="glyphicon glyphicon-thumbs-down"></span></button>
			</span> 
		</div>
	</div>

	<div class="container">
		
	</div>
	

</#assign>
<#include "main.ftl">