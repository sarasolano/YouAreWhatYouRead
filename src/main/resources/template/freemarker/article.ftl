<#assign page>
<div class="container">
	<div id="article-container" class="col-md-10 col-md-offset-1">
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
<#include "navbar.ftl">