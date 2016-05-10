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
            <li id="prof">
              <a href="/profile">
                <i class="glyphicon glyphicon-home"></i> Overview </a>
            </li>
            <li id="settings">
              <a href="/profile?tab=settings">
                <i class="glyphicon glyphicon-user"></i> Account Settings </a>
            </li>
            <li id="history">
              <a href="/profile?tab=history">
                <i class="glyphicon glyphicon-book"></i> History </a>
            </li>
          </ul>
        </div>
        <!-- END MENU -->
      </div>
    </div>
    <div class="col-md-9">
      <div class="profile-content">
         ${profileContent}
      </div>
    </div>
  </div>
</div>
<br>

</#assign>
<#include "main.ftl">