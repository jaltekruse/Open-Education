<html>
<head>
<title>Open Math</title>
	
    <style type="text/css" media="all">
		@import url("../css/style.css");
		@import url("../css/jquery.wysiwyg.css");
		@import url("../css/facebox.css");
		@import url("../css/visualize.css");
		@import url("../css/date_input.css");
    </style>
<script language="javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"></script>
</head>
<body>
<div class="wrapper">
<!--OpenMath header-->
<?php include("../common/header.php"); ?>

<table style="width:100%"><tr><td width="50%" valign="top">


<div class="block">
  <div class="block_head">
    <div class="bheadl"></div>
    <div class="bheadr"></div>
    <h2>Students</h2>
  </div>
  <div class="block_content" style="position:relative">
    <table cellpadding="0" cellspacing="0" width="100%" class="sortable">
    <thead>
      <tr>
	<td></td>
	<td colspan="3">Assignments</td>
	<td></td>
      </tr>
      <tr>
	<td>Student</td>
	<td>1</td><td>2</td><td>3</td>
	<td>Grade</td>
      </tr>
    </thead>
							
    <tbody>
      <tr>
	<td><a href="student.php">Albus Dumbledore</a></td>
	<td>B</td><td>B-</td><td>NG</td>
	<td>B-</td>
      </tr>
      <tr>
	<td><a href="student.php">Bethany Bethany</a></td>
	<td>A</td><td>A</td><td>A</td>
	<td>A</td>
      </tr>
      <tr>
	<td><a href="student.php">Reupert Montgomery</a></td>
	<td>D</td><td>C-</td><td>D</td>
	<td>D</td>
      </tr>
      <tr>
	<td><a href="student.php">Sweeney Todd</a></td>
	<td>B</td><td>E-</td><td>K</td>
	<td>&Omega;</td>
      </tr>
    </tbody>
    </table>
  </div>
  <div class="bendl"></div>
  <div class="bendr"></div>
</div>

<div class="block">
  <div class="block_head">
    <div class="bheadl"></div>
    <div class="bheadr"></div>
    <h2>Upload</h2>
  </div>
  <div class="block_content">
    <form> <!-- So css styles apply -->
      <label>Assignment Name:</label> <br />
      <input type="text" class="text" />
      <label>Topics</label> <br />
      <input type="text" class="text" />
      <p class="fileupload">
      <label>Upload file:</label><br />
      <input type="file" id="fileupload" style="height:30px" />
      </p>
    </form>
  </div>
  <div class="bendl"></div>
  <div class="bendr"></div>
</div>

</td>
<td><div style="width:10px"></div></td>
<td width="50%" valign="top">

<div class="block">
  <div class="block_head">
    <div class="bheadl"></div>
    <div class="bheadr"></div>
    <h2>Messages</h2>
  </div>
  <div class="block_content" id="loginWrapper" style="position:relative">
    <div class="message success"><p>Question of the Week posted</p></div>
    <div class="message info"><p>Beatrice Beatrice submitted Assignment 3: Partial Fractions <a href="#">(go to assignment)</a></p></div>
    <div class="message info"><p>Herman Melville submitted Assignment 3: Partial Fractions <a href="#">(go to assignment)</a></p></div>
    <div class="message comment">
      <p>I won't be able to come to class tomorrow.  My house burned down.</p><br>
      <div align="right" style="font-weight:400">-Bethany&nbsp;Bethany</div>
      <a href="#">Reply</a>
    </div>
  </div>
  <div class="bendl"></div>
  <div class="bendr"></div>
</div>

<div class="block">
  <div class="block_head">
    <div class="bheadl"></div>
    <div class="bheadr"></div>
    <h2>Assign</h2>
  </div>
  <div class="block_content" id="loginWrapper" style="position:relative">
    <form>
    <table cellpadding="0" cellspacing="0" width="100%" class="sortable">
    <thead>
      <tr>
	<td>File</td>
	<td>Due Date</td>
	<td>Assigned</td>
      </tr>
    </thead>
							
    <tbody>
      <tr>
	<td>Assignment 1</td>
	<td><input type="text" class="text date_picker" /></td>
	<td>B-</td>
      </tr>
      <tr>
	<td>Assignment 2</td>
	<td><input type="text" class="text date_picker" /></td>
	<td>A</td>
      </tr>
      <tr>
	<td>Assignment 3</td>
	<td><input type="text" class="text date_picker" /></td>
	<td>D</td>
      </tr>
      <tr>
	<td>Assignment 4</td>
	<td><input type="text" class="text date_picker" /></td>
	<td>&Omega;</td>
      </tr>
    </tbody>
    </table>
    <p><input class="submit" type="submit" value="Apply"></p>
    </form>
  </div>
  <div class="bendl"></div>
  <div class="bendr"></div>
</div>
</td></tr></table>

</div>	

<!-- Animus-related scripts -->

<script type="text/javascript" src="../js/jquery.js"></script>
<script type="text/javascript" src="../js/jquery.img.preload.js"></script>
<script type="text/javascript" src="../js/jquery.filestyle.mini.js"></script>
<script type="text/javascript" src="../js/jquery.wysiwyg.js"></script>
<script type="text/javascript" src="../js/jquery.date_input.pack.js"></script>
<script type="text/javascript" src="../js/facebox.js"></script>
<script type="text/javascript" src="../js/jquery.visualize.js"></script>
<script type="text/javascript" src="../vjs/jquery.visualize.tooltip.js"></script>
<script type="text/javascript" src="../js/jquery.select_skin.js"></script>
<script type="text/javascript" src="../js/jquery.tablesorter.min.js"></script>
<script type="text/javascript" src="../js/ajaxupload.js"></script>
<script type="text/javascript" src="../js/jquery.pngfix.js"></script>
<script type="text/javascript" src="../js/custom.js"></script>

</body>
</html>