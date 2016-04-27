<#assign content>


<#if 0 < stars?size >
You scored with these:
<ul>
<#list stars as star>
   <li>${star}
</#list>
</ul>
</#if>

<a href="/stars">Do another search.</a>

</#assign>
<#include "main.ftl">
