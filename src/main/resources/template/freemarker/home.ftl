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
		<div class="container">
			<div class="container input-group" id="small-stats">
			<input id ="url-input"type="text" class="form-control" placeholder="Add an article!">
			<span class="input-group-btn">
			<button id="plus" class="btn btn-default" type="submit"><span class="glyphicon glyphicon-thumbs-up"></span></button>
			<button id = "add-article" class="btn btn-default" type="submit"><span class="glyphicon glyphicon-plus"></span></button>
			<button id ="minus" class="btn btn-default" type="submit"><span class="glyphicon glyphicon-thumbs-down"></span></button>
			</span> 
		
		</div>
		<div class="container">
		<div id="alert"class="alert alert-danger hide" role="alert">Must give a valid url</div>
		</div>
		</div>
	</div>
	<div class="container">
	<div id="article-container" class="col-md-10 col-md-offset-1 hide">
		<div class="row">
			<div class="col-md-12">
				<div id="title-container">
				<h2><a id="title" href='#'></a></h2>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-md-3">
				<div class="chartLabel">Topic</div>
				<p id="topic" class="stat"></p>
			</div>
			<div class="col-md-3">
				<div class="chartLabel">Word Count</div>
				<p id="word-count" class="stat"></p>
			</div>
			<div class="col-md-3">
				<div class="chartLabel">Pages</div>
				<p id="pages" class="stat"></p>
			</div>
			<div class="col-md-3">
				<div class="chartLabel">Grade Level</div>
				<p id="grade" class="stat"></p>
			</div>
		</div>
		<div class="article-stats">
			<div class="row">
				<div class="col-md-12">
					<div id="cloud"></div>
				</div>
			</div>
			<div class="row">
				<div class="col-md-1"></div>
				<div class="col-md-10">
					<div class="chartLabel">Sentiment Timeline</div>
					<div class="progress" id="bar"></div>
				</div>
				<div class="col-md-1"></div>
			</div>
			<div class="row">
				<div class="col-md-6">
				<div class="chartLabel">Mood</div>
				<div id="mood"></div>
			</div>
			<div class="col-md-6">
				<div class="chartLabel">Readability</div>
				<div id="level"></div>
			</div>
			</div>
		</div>
	</div>
</div>

</#assign>
<#include "main.ftl">