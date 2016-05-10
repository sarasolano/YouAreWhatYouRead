<#assign content>
<nav class="navbar navbar-default navbar-fixed-top">
  <div class="container">
    <a class="navbar-brand" href="/"><img src="../img/redient2.gif"></a>
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

<div class="container">
  <div class="row profile">
    <div class="col-md-3">
      <div class="profile-sidebar">
        <!-- SIDEBAR USER TITLE -->
        <div class="profile-usertitle">
          <div class="profile-usertitle-name">
            ${username}
          </div>
          <div class="profile-usertitle-job">
            <p id="totalwords"></p>
            <p id="numArticles"></p>
          </div>
        </div>
        <!-- END SIDEBAR USER TITLE -->
        <!-- SIDEBAR MENU -->
        <div class="profile-usermenu">
          <ul class="nav">
            <li class="active">
              <a href="#">
                <i class="glyphicon glyphicon-home"></i> Overview </a>
            </li>
            <li>
              <a href="#">
                <i class="glyphicon glyphicon-user"></i> Account Settings </a>
            </li>
            <li>
              <a href="#">
                <i class="glyphicon glyphicon-book"></i> History </a>
            </li>
          </ul>
        </div>
        <!-- END MENU -->
      </div>
    </div>
    <div class="col-md-9">
      <div class="profile-content">
        <div class="row">
          <div class="col-md-9">
            <div class="chartLabel">Topics</div>
            <div id="topics"></div>
          </div>
        </div>
        <div class="row">
          <div class="col-md-4">
            <div class="chartLabel">Mood</div>
            <div id="avgMoods"></div>
          </div>
          <div class="col-md-5">
          	<div class="chartLabel">Readability</div>
            <div id="avg-rl"></div>
          </div>
        </div>
        <div class="row">
          <div class="col-md-9">
          <div class="chartLabel">Calendar</div>
            <div id="cal-heatmap">
            </div>
            <ul class="list-group" id = "articlelist"> 
		    </ul>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>
<br>

</#assign>
<#include "main.ftl">