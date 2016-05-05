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
					</ul>
				</li>
			</ul>
		</div>
		</div>
	</nav>
	<div class="jumbotron" id="big">
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
			<button class="btn btn-default" type="submit"><span class="glyphicon glyphicon-thumbs-up"></span></button>
			<button id = "add-article" onclick = "sendUrl()" class="btn btn-default" type="submit"><span class="glyphicon glyphicon-plus"></span></button>
			<button class="btn btn-default" type="submit"><span class="glyphicon glyphicon-thumbs-down"></span></button>
			</span>
		</div>
	</div>
	<div class="article-stats">
		<div class="container">
			<div class="jumbotron" id="article">
				<h2> Article Title </h2>
				<p> Word Count: 1,567 </p>
				<p> Pages: 6.3 </p>
				<div class="row">
					<div class="col-md-4">
						<div id="wc"></div>
					</div>
					<div class="col-md-4">
						<div id="topic"></div>
					</div>
					<div class="col-md-4">
						<div id="pages"></div>
					</div>
				</div>
				<div class="row">
					<div class="col-md-8">
						<div id="cloud"></div>
					</div>
					<div class="col-md-4">
						<div id="mood"></div>
					</div>
				</div>
				<div class="row">
					<div class="col-md-4">
						<div id="level"></div>
					</div>
					<div class="col-md-8">
						<div id="sentiment"></div>
					</div>
				</div>
			</div>
		</div>
	</div>

</#assign>
<#include "main.ftl">