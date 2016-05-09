<#assign content>
	<nav class="navbar navbar-default navbar-fixed-top">
		<div class="container">
			<a class="navbar-brand" href="#"><img
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
	<div class="jumbotron" id="big">
		<div class="container">
			<div class="jumbotron">
				<div id="border">
					<h1>Readient</h1>
					<p id="des">You are what you read.</p>
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
		<div class="container input-group">
		<div id="alert"class="alert alert-danger hide" role="alert">Must give a valid url</div>
		</div>
	</div>
	<div class="article-stats">
		<div class="container">
			<div class="jumbotron hide" id="article">
				<h2> Article: <a id="title" href ='#'></a> </h2>
				<p id ="topic"> topic</p>
				<p id ="word-count">  </p>
				<p id ="pages"> </p>
				<p id ="grade"></p>
				</div>
				<h3>Sentiment Timeline</h3>
				<div class="progress hide" id ="bar">
  						</div>
  				
				<div class="row" id="row2" >
				
				<div class="col-md-8">
						<div id="cloud"></div>
				</div>
				<div class="col-md-4">
					<div id="mood"></div>
				</div>
				</div>
				<div class="row">
					<div class="col-md-4 col-md-offset-4">
						<div id="level"></div>
					</div>
					</div>
				</div>
		</div>
	</div>

</#assign>
<#include "main.ftl">