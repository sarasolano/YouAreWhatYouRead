<#assign profileContent>
      <div id="content">
        <div class="row">
          <div class="col-md-4">
            <div class="chartLabel">Mood</div>
            <div id="avgMoods"></div>
          </div>
          <div class="col-md-4">
          	<div class="chartLabel">Readability</div>
            <div id="avg-rl"></div>
          </div>
          <div class="col-md-4">
          	<div class="chartLabel">Liked Websites</div>
            <div id="websites"></div>
          </div>
        </div>
        <div class="row">
          <div class="col-md-12">
          <div class="chartLabel">Calendar</div>
            <div id="cal-heatmap">
            </div>
            <ul class="list-group" id = "articlelist"> 
		    </ul>
          </div>
        </div>
      </div>
</#assign>
<#include "profileContainer.ftl">