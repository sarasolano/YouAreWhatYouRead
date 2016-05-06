<#assign content>
	<nav class="navbar navbar-default navbar-fixed-top">
		<div class="container">
			<a class="navbar-brand" href="/"><img
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
	
	<div class="prof-stats">
		<div class="container">
				<ul id = "articlelist"> article list!
				</ul>
				</div>
				<div class="row">
					<div class="col-md-4">
						<div id="avg-rl"></div>
					</div>
					<div class="col-md-4">
						<div id="totalwords-"></div>
					</div>
			
				
			
		</div>
	</div>
	

</#assign>
<#include "main.ftl">