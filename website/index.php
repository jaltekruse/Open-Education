<html>
<head>
<title>Open Math</title>
	
    <style type="text/css" media="all">
		@import url("css/style.css");
		@import url("css/jquery.wysiwyg.css");
		@import url("css/facebox.css");
		@import url("css/visualize.css");
		@import url("css/date_input.css");
    </style>
<script language="javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"></script>
<script language="javascript" src="scripts/bezier.js"></script>
</head>
<body>
<div class="wrapper">
<!--OpenMath header-->
<?php include("common/header.php"); ?>
<div class="block small left">
  <div class="block_head">
    <div class="bheadl"></div>
    <div class="bheadr"></div>
    <h2>About Open Math</h2>
  </div>
  <div class="block_content">
    <ul>
      <li>Innovative, collaborative something something</li>
      <li>Innovative, collaborative something something</li>
      <li>Innovative, collaborative something something</li>
      <li>Innovative, collaborative something something</li>
    </ul>
  </div>
  <div class="bendl"></div>
  <div class="bendr"></div>
</div>


<div class="block small right">
  <div class="block_head">
    <div class="bheadl"></div>
    <div class="bheadr"></div>
    <h2>Log In / Register</h2>
  </div>
  <div class="block_content" id="loginWrapper" style="position:relative">
    <div id="spacer"></div>
    <div id="loginContent1">
      <form action="auth.html">
	<p><label>Email:</label><br><input class="text" type="text" name="email"></p>
	<p><label>Password:</label><br><input class="text" type="password" name="password"></p>
	<p><input type="button" class="submit" value="Register" onClick="interpolateBlocks('loginWrapper', 'loginContent1', 'loginContent2');">&nbsp;
	<input type="button" class="submit" value="Login">&nbsp;
	<label>Remember Me</label><input type="checkbox" class="checkbox" id="rememberme"></p>
      </form>
    </div>
    <div id="loginContent2" style="display:none" align="center">
      <h2>I am a</h2>
      <form>
	<input type="button" class="submit" value="Teacher" onClick="interpolateBlocks('loginWrapper', 'loginContent2', 'loginContent3')">&nbsp;&nbsp;
	<input type="button" class="submit" value="Student" onClick="window.location = 'student/index.php';">
      </form>
      <div style="height:8px" id="spacer"></div>
    </div>
    <div id="loginContent3" style="display:none">
      <form action="auth.html">
	<p><label>Email:</label><br><input class="text" type="text" name="email"></p>
	<p><label>Password:</label><br><input class="text" type="text" name="password"></p>
	<p><label>Confirm Password:</label><br><input class="text" type="text" name="password2"></p>
	<p><input type="button" class="submit" value="Sign Up" onClick="window.location='teacher/index.php';">
      </form>
    </div>
  </div>
  <div class="bendl"></div>
  <div class="bendr"></div>
</div>

</div>
</body>
</html>