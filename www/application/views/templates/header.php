<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title><?php echo $title ?></title>

    <style type="text/css" media="all">
		@import url("/css/jquery.wysiwyg.css");
		@import url("/css/facebox.css");
		@import url("/css/visualize.css");
		@import url("/css/date_input.css");
		@import url("/js/chosen.css");
		@import url("/css/style.css");
    </style>
	
	<!--[if IE]><script type="text/javascript" src="/js/excanvas.js"></script><![endif]-->
	<script type="text/javascript" src="/js/jquery.js"></script>
	<script type="text/javascript" src="/js/jquery.img.preload.js"></script>
	<script type="text/javascript" src="/js/jquery.filestyle.mini.js"></script>
	<script type="text/javascript" src="/js/jquery.wysiwyg.js"></script>
	<script type="text/javascript" src="/js/jquery.date_input.pack.js"></script>
	<script type="text/javascript" src="/js/facebox.js"></script>
	<script type="text/javascript" src="/js/jquery.visualize.js"></script>
	<script type="text/javascript" src="/js/jquery.visualize.tooltip.js"></script>
	<script type="text/javascript" src="/js/jquery.select_skin.js"></script>
	<script type="text/javascript" src="/js/jquery.tablesorter.min.js"></script>
	<script type="text/javascript" src="/js/ajaxupload.js"></script>
	<script type="text/javascript" src="/js/jquery.pngfix.js"></script>
	<script type="text/javascript" src="/js/custom.js"></script>
	<script type="text/javascript" src="/js/chosen.jquery.min.js"></script>
	<script type="text/javascript">
		// add function to strings to allow for automatic ellipses insertion if they are too long
		// http://www.barryvan.com.au/2009/08/javascript-string-ellipsising/
		String.prototype.ellipsise = function(toLength, where, ellipsis) { // Where is one of ['front','middle','end'] -- default is 'end'
			if (toLength < 1) return this;
			ellipsis = ellipsis || '\u2026';
			if (this.length < toLength) return this;
			switch (where) {
				case 'front':
					return ellipsis + this.substr(this.length - toLength);
					break;
				case 'middle':
					return this.substr(0, toLength / 2) + ellipsis + this.substr(this.length - toLength / 2)
					break;
				case 'end':
				default:
					return this.substr(0, toLength) + ellipsis;
					break;
			}
		}
		$(document).ready(function(){
			$(".chosen_dropdown").chosen();
		});
	</script>

</head>
<body>
<div class="wrapper">
<!--OpenMath header-->
<div id="header">
  <div class="hdrl"></div>
  <div class="hdrr"></div>
  <h1><a href="/index.php">Open-Math</a></h1>
  <ul id="nav">
<!-- navigation menus for top bar -->
<?php if (isset($class_list)): ?>
	<li> <a href="/index.php/user/classes">Classes</a>
		<ul>
		<?php foreach($class_list as $class): ?>
		<li><a href="/index.php/user/view_class/<?php echo $class['class_id'] ?>">
			<?php echo $class['subject'].' - Hr '.$class['class_hour']?></a>
		</li>
		<?php endforeach; ?>
		</ul>
	</li>
 
 <?php endif; ?>
 <?php if (isset($user_id)): ?>
	<li><a href="/index.php/user/documents">Docs</a>
		<ul>
		<li><a href="/index.php/user/documents">My Docs</a></li>
 		<li><a href="/index.php/user/doc_search">Find Documents</a></li>
 		</ul>
	</li>
		<li> <a href="/index.php/user/colleagues">Connect</a>
			<ul>
			<!-- add a list of all groups here? if they are used how I expect people shouldn't be members of too many -->
			<li><a href="/index.php/user/colleagues">Colleagues</a></li>
			<li><a href="/index.php/user/add_colleague">Add Colleague</a></li>
			<li><a href="/index.php/user/pending_colleagues">Pending</a></li>
			<!-- will implement this later <li> <a href="/index.php/user/groups">Groups</a></li> -->
			</ul>
		</li>
	<?php endif; ?>
	<?php if (isset($class_id)): ?>
 	<li><a href="/index.php/user/current_assignments">Assignments</a>
 		<ul>
 		<li><a href="/index.php/user/current_assignments">Current Assignments</a></li>
 		<li><a href="/index.php/user/class_admin">Past Assignments</a></li>
 		<?php if (isset($is_teacher) && $is_teacher): ?>
 			<li><a href="/index.php/user/assign">New Assignment</a></li>
 		<?php endif; ?>
 		</ul>
 	</li>
 	<?php endif; ?>
 	<?php if (isset($is_teacher) && $is_teacher): ?>
	<li><a href="/index.php/user/class_admin">Class Admin</a></li>
	<?php endif; ?>
 </ul>
 <?php
 if (isset($user_id)) ?>
<p class="user">
<?php if (! isset($user_id)) { ?>
  <a href="/index.php/auth/login">Log in</a> &nbsp;
  <a href="/index.php/auth/register">Register</a>
<?php } else{ echo 'Hi, '.$first_name.'&nbsp;|&nbsp;'; ?>
<a href="/index.php?/auth/logout">Logout</a>
<?php } ?>
</p>
</div>
<div class="block">
  <div class="block_head">
    <div class="bheadl"></div>
    <div class="bheadr"></div>
    <h2><?php echo isset($heading) ? $heading : $title?></h2>
  </div>
  <div class="block_content" style="position:relative">
	<?php if (isset($messages)){
		foreach($messages as $message){
			if($message['type'] == 'success'){ ?>
				<div class="message success"><p> <?php echo $message['msg'] ?> </p></div>
			<?php
			} else if ($message['type'] == 'error'){ ?>
				<div class="message errormsg"><p> <?php echo $message['msg'] ?> </p></div>
			<?php
			} else if ($message['type'] == 'warning'){ ?>
				<div class="message warning"><p> <?php echo $message['msg'] ?> </p></div>
			<?php
			}
		}
	} ?>
