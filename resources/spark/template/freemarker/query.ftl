<#assign content>

<h1> Query ${db} </h1>
<div id="page">
<div class="forms" id="n">
<p> for neighbors

<form method="POST" id="neighbors" action="/neighbors">
	<#if 0 < nfields?size >
		<#list nfields as nfield>
  		<label type="text" name="${nfield}" id="${nfield}" for="${nfield}">${nfield} </label>
  		<input type="text" name="${nfield}"id="${nfield}"  />
  		</#list>
  	</#if>
  	<input type="radio" onclick="check_name()" name="choice" class="rad" id="star-name"> Search by Name<br>
 	<input type="radio" onclick="check_coord()"name="choice"  class="rad" id="coordinate" value="coordinate" checked> Search by Coordinate<br>
  	<input type="submit">

</form>


</p>
</div>
<div class="forms" id="r">
<p> by radius

<form method="POST" id="radius" action="/radius">
	<#if 0 < rfields?size >
		<#list rfields as rfield>
  		<label type="text" name="${rfield}" id="${rfield}" for="${rfield}">${rfield}</label>
  		<input type="text"  name="${rfield}"id="${rfield}" />
  		</#list>
  	</#if>
  	<input type="radio" onclick="rcheck_name()"  class="rad" name="choice" id="rstar-name"> Search by Name<br>
 	<input type="radio" onclick="rcheck_coord()"name="choice"  class="rad" id="rcoordinate" value="coordinate" checked> Search by Coordinate<br>
  	<input type="submit">

</form>

</p>
</div>
</div>

</#assign>
<#include "main.ftl">
