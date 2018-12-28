<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ attribute name="width" required="false"%>
<%@ attribute name="style" required="false"%>
<div class="line" style="border-top-style: ${(empty style ? 'solid' : style)}; width: ${(empty width ? '100%' : width)};"></div>