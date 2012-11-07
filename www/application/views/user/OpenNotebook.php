<?php echo "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" ?>

<!DOCTYPE jnlp PUBLIC "-//Sun Microsystems, Inc//DTD JNLP
Descriptor 6.0//EN" "http://java.sun.com/dtd/JNLP-6.0.dtd">
<jnlp spec="6.0+" codebase="http://localhost">
       <information>
               <title>OpenNotebook</title>
               <vendor>Open Education</vendor>
               <description>A document preparation and homework completion system for math classes.</description>
               <offline-allowed />
       </information>
       <security>
               <all-permissions />
               <j2ee-application-client-permissions />
       </security>
       <resources>
               <j2se version="1.6+" />
               <jar href="OpenNotebook.jar" main="true"
download="eager" />
				<property name="jnlp.packEnabled" value="true"/>
       </resources>
       <application-desc main-class="doc_gui.OpenNotebook" name="OpenNotebook">
		<?php foreach ($parameters as $parameter){
			echo "<argument>".htmlspecialchars($parameter)."</argument>\n";
		} ?>
</application-desc>
</jnlp> 
