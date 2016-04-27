<#assign content>
<p id="q"> ${k}  </p>
<p id="error">${error}</p>
<#if 0 < stars?size >
<p id= "statement">We found ${stars?size} neighbors: </p>
<ul>
<#list stars as star>
   <li>${star}
</#list>
</ul>
</#if>

<a href="/stars">Do another search.</a>

</#assign>
<#include "main.ftl">