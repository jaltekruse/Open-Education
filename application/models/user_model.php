<?php 
class User_model extends CI_Model{
	
	private $doc_description_fields;
	private $user_info_fields;

	public function __construct(){
		$this->load->database();
		$this->load->library('tank_auth');

		$this->doc_description_fields = "docname,material_type, " .
		 $this->mysql_date_format("upload_date") . ", doc_id,size,doc_type ";
		$this->user_info_fields = "user_profiles.first_name, user_profiles.last_name, user_profiles.user_id ";
		define('TEACHER', 1);
		define('STUDENT', 2);
		define('ADMIN', 3);
		
		define('TEST', 1);
		define('WORKSHEET', 2);
		define('QUIZ', 3);
		define('LESSON_PLAN', 4);
		define('ACTIVITY', 5);
		define('ARTICLE', 6);
		define('DIAGRAM', 7);
		define('OTHER', 30);
	}

	public function get_colleagues($user_id){
		$query = 'SELECT ' . $this->user_info_fields . 
				'FROM user_profiles,colleagues WHERE colleagues.sender_user_id = ? AND
				colleagues.receiver_user_id = user_profiles.user_id';
		$result = $this->db->query($query, array( 'sender_user_id' => $user_id))->result_array();
		$query = 'SELECT user_profiles.first_name, user_profiles.last_name, user_profiles.user_id FROM user_profiles,colleagues
				WHERE colleagues.receiver_user_id = ? AND colleagues.sender_user_id = user_profiles.user_id';
		return array_merge($result, $this->db->query($query, array( 'sender_user_id' => $user_id))->result_array());
	}
	
	public function get_pending_colleagues($user_id){
		$query = 'SELECT ' . $this->user_info_fields .
			'FROM user_profiles,colleagues WHERE
			colleagues.receiver_user_id = ? AND colleagues.sender_user_id = user_profiles.user_id AND accepted = 0';
		return $this->db->query($query, array( 'receiver_user_id' => $user_id))->result_array();
	}
	
	public function confirm_colleague($pending_user_id, $user_id){
		$sql_query = "UPDATE colleagues set accepted = 1 WHERE sender_user_id = ? AND receiver_user_id = ?";
		$query = $this->db->query($sql_query, array( 'sender_user_id' => $pending_user_id, 'receiver_user_id' => $user_id));
	}
	
	public function get_id_from_email($email){
		$sql_query = "SELECT users.id FROM users WHERE email = ?";
		$query = $this->db->query($sql_query, array( 'email' => $email));
		if ($query->num_rows == 0){
			return FALSE;
		}
		return $new_colleague_id = $query->row()->id;
	}
	
	public function can_add_colleague($email, $user_id){
		$new_colleague_id = $this->get_id_from_email($email);
		if ( ! $new_colleague_id )
			return "There is no user in our records with that E-mail.";
		if ( $this->request_already_sent($user_id, $new_colleague_id) )
			return "You have already sent a request to the user registered with that E-mail.";
		if ( $this->is_pending_colleague($new_colleague_id, $user_id) )
			return "The user registered with that E-mail had already sent you a request, we have added them to your list of colleagues.";
		if ( $this->is_current_colleague($new_colleague_id, $user_id) )
			return "You are already connected with the user registered with that E-mail.";
		return TRUE;
	}
	
	public function request_already_sent($sender_id, $receiver_id){
		$sql_query = "SELECT sender_user_id FROM colleagues WHERE sender_user_id = ? AND receiver_user_id = ?";
		$query = $this->db->query($sql_query, array( 'sender_user_id' => $sender_id, 'receiver_user_id' => $receiver_id));
		if ($query->num_rows > 0)
			return TRUE;
		return FALSE;
	}
	
	public function is_pending_colleague($new_colleague_id, $user_id){
		$sql_query = "SELECT sender_user_id FROM colleagues WHERE receiver_user_id = ? AND sender_user_id = ?";
		$query = $this->db->query($sql_query, array( 'receiver_user_id' => $user_id, 'sender_user_id' => $new_colleague_id));
		if ($query->num_rows > 0)
			return TRUE;
		return FALSE;
	}
	
	public function is_current_colleague($new_colleague_id, $user_id){
		$sql_query = "SELECT colleagues.sender_user_id FROM colleagues WHERE colleagues.sender_user_id = ?
				AND colleagues.receiver_user_id = ?";
		$query = $this->db->query($sql_query, array( 'sender_user_id' => $user_id, 'receiver_user_id' => $new_colleague_id));
		if ($query->num_rows > 0)
			return TRUE;
		$sql_query = "SELECT colleagues.sender_user_id FROM colleagues WHERE colleagues.sender_user_id = ?
				AND colleagues.receiver_user_id = ?";
		$query = $this->db->query($sql_query, array( 'sender_user_id' => $new_colleague_id, 'receiver_user_id' => $user_id));
		if ($query->num_rows > 0)
			return TRUE;
		return FALSE;
	}
	
	public function add_colleague_request($sender_id, $receiver_id){
		$this->db->insert('colleagues', array('sender_user_id' => $sender_id, 'receiver_user_id' => $receiver_id));
	}

	/*
	 * User profiles are only created by tank auth after account activation, but user
	 * info must be saved right away. Adding database entry in this method,
	 * to re-enable tank-auth creation see /models/tank_auth/users.php Line:381	
	*/
	public function add_user(){
		$this->load->helper('url');
		
		$data = array(
			'email' => $this->input->post('email'),
			'first_name' => 'temp_name',
			'last_name' => 'temp_name'
		);
		
		return $this->db->query('UPDATE user_profiles WHERE', $data);
	}
	
	public function open_enrollment($class_id, $password){
		$salt = md5(rand().microtime());
		$sql_row = array(	'class_id' => $class_id,
				'password' => md5(''.$salt.$salt.$password.$salt.$salt.''),
				'salt' => $salt);
		$this->db->insert('class_enrollments', $sql_row);
		unset($sql_row);
		unset($salt);
	}
	
	public function close_enrollment($class_id){
		if ( ! $this->enrollment_is_open($class_id)){
			return FALSE;
		}
		$sql_query = "DELETE FROM class_enrollments WHERE class_enrollments.class_id = ?";
		$query = $this->db->query($sql_query, array('class_id' => $class_id));
		return TRUE;
	}
	
	public function join_class($class_id, $user_id, $password){
		if ( ! $this->enrollment_is_open($class_id) ){
			return FALSE;
		}
		if ( $this->is_class_member($user_id, $class_id)){
			//already a member
			return FALSE;
		}
		$sql_query = "SELECT * FROM class_enrollments WHERE class_enrollments.class_id = ?";
		$query = $this->db->query($sql_query, array('class_id' => $class_id))->row();
		$salt = $query->salt;
		
		$sql_query = "SELECT * FROM class_enrollments WHERE class_enrollments.class_id = ? AND
				password = ?";
		$sql_row = array(	'class_id' => $class_id,
							'password' => md5($salt.$salt.$password.$salt.$salt.''),
		);
		$query = $this->db->query($sql_query, $sql_row);
		if ($query->num_rows == 0){
			return FALSE;
		}
		unset($sql_row);
		$sql_row = array(	'class_id' => $class_id,
						 	'user_id' => $user_id,
						 	'role' => STUDENT,
		);
		$this->db->insert('class_members', $sql_row);
		return TRUE;
	}
	
	public function enrollment_is_open($class_id){
		$sql_query = "SELECT * FROM class_enrollments WHERE class_enrollments.class_id = ?";
		$query = $this->db->query($sql_query, array('class_id' => $class_id));
		if ($query->num_rows == 0){
			return FALSE;
		}
		return TRUE;
	}
	
	public function is_doc_owner($user_id, $doc_id){
		$sql_query = "SELECT * FROM documents WHERE documents.owner_user_id = ? AND
				documents.doc_id = ?";
		
		$query = $this->db->query($sql_query, array( 'owner_user_id' => $user_id, 'doc_id' => $doc_id));
		if ($query->num_rows == 0){
			return FALSE;
		}
		return TRUE;
	}
	
	public function get_doc($doc_id){
		$sql_query = "SELECT * FROM documents WHERE	documents.doc_id = ?";
		$result = $this->db->query($sql_query, array( 'doc_id' => $doc_id))->row_array();
		if (isset($result['material_type'])){
			$result['material_type'] = $this->material_type_string($result['material_type']);
		}
		return $result;
	}
	
	public function get_student_submission($assignment_id, $user_id){
		$sql_query = "SELECT assignments_and_users.student_doc, user_id FROM assignments_and_users WHERE	assignment_id = ? AND user_id = ?";
		return $this->db->query($sql_query, array( 'assignment_id' => $assignment_id, 'user_id' => $user_id))->row_array();
	}
	
	public function get_orignial_assignment($assignment_id){
		$sql_query = "SELECT assignment_name, student_doc, notes, assign_date, due_date, total_points FROM assignments WHERE assignments.assignment_id = ?";
		return $this->db->query($sql_query, array( 'assignment_id' => $assignment_id))->row_array();
	}

	public function get_teacher_assignment($assignment_id){
		$sql_query = "SELECT assignment_name, student_doc, notes, assign_date, due_date, total_points FROM assignments WHERE assignments.assignment_id = ?";
		return $this->db->query($sql_query, array( 'assignment_id' => $assignment_id))->row_array();
	}

	public function get_assignment_info($assignment_id){
		$sql_query = "SELECT assignment_name, student_doc, notes, assign_date, due_date, total_points FROM assignments WHERE assignments.assignment_id = ?";
		return $this->db->query($sql_query, array( 'assignment_id' => $assignment_id))->row_array();
	}

	public function get_student_assignment($assignment_id, $user_id){
		$sql_query = "SELECT assignment_name, assignment_id, user_id, notes, LOWER(DATE_FORMAT(assign_date, '%c/%e/%y at %l:%i%p')) AS assign_date, ".
			"LOWER(DATE_FORMAT(due_date, '%c/%e/%y at %l:%i%p')) AS due_date, assignments_and_users.student_doc, assignments.student_doc AS original_assignment, total_points, ".
			"CASE WHEN submit_time = '00/00/0000' THEN NULL ELSE LOWER(DATE_FORMAT(submit_time, '%c/%e/%y at %l:%i%p')) END AS submit_time ".
			"FROM assignments, assignments_and_users WHERE assignments.assignment_id = ? ". 
			"AND assignments.assignment_id = assignments_and_users.assignment_id ".
			"AND assignments_and_users.user_id = ?";
		return $this->db->query($sql_query, array( 'assignment_id' => $assignment_id, 'user_id' => $user_id))->row_array();
	}

	public function delete_doc($doc_id){
		$this->db->delete('document_tags', array('doc_id' => $doc_id));
		$this->db->delete('documents', array('doc_id' => $doc_id));
		return TRUE;
	}
	
	public function delete_assignment($assignment_id){
		$this->db->delete('assignments_and_users', array('assignment_id' => $assignment_id));
		$this->db->delete('assignments', array('assignment_id' => $assignment_id));
		return TRUE;
	}
	
	public function is_students_assignment($user_id, $assignment_id){
		$sql_query = "SELECT * FROM assignments_and_users WHERE and_users.user_id = ? AND
				assignments_and_users.assignment_id = ?";
		
		$query = $this->db->query($sql_query, array( 'user_id' => $user_id, 'assignment_id' => $assignment_id));
		if ($query->num_rows == 0){
			return FALSE;
		}
		return TRUE;
	}
	
	public function remove_student($user_id, $class_id){
		$sql_row = array(	'class_id' => $class_id,
			 	'user_id' => $user_id,
		);
		$this->db->delete('class_members', $sql_row);
		return TRUE;
	}
	
	public function is_class_member($user_id, $class_id){
		$sql_query = "SELECT * FROM class_members WHERE class_members.user_id = ? AND
				class_members.class_id = ?";
		
		$query = $this->db->query($sql_query, array( 'user_id' => $user_id, 'class_id' => $class_id));
		if ($query->num_rows == 0){
			return FALSE;
		}
		return TRUE;
	}
	
	public function is_teacher($user_id, $class_id){
		$sql_query = "SELECT * FROM class_members WHERE class_members.user_id = ? AND
				class_members.class_id = ?";
		
		$query = $this->db->query($sql_query, array( 'user_id' => $user_id, 'class_id' => $class_id))->row();
		if ( ! $query || $query->role != TEACHER){
			return FALSE;
		}
		return TRUE;
	}
	
	public function is_student($user_id, $class_id){
		$sql_query = "SELECT * FROM class_members WHERE class_members.user_id = ? AND
				class_members.class_id = ?";
		
		$query = $this->db->query($sql_query, array( 'user_id' => $user_id, 'class_id' => $class_id))->row();
		if ( ! $query || $query->role != STUDENT){
			return FALSE;
		}
		return TRUE;
	}

	public function get_classes($user_id){
		$sql_query = "SELECT role, classes.class_id, subject, class_hour, LOWER(DATE_FORMAT(create_date, '%c/%e/%y at %l:%i%p')) AS create_date, ".
			"CASE WHEN close_date = '000/00/00' THEN NULL ELSE LOWER(DATE_FORMAT(close_date, '%c/%e/%y at %l:%i:%p')) END AS close_date,  ".
		"class_enrollments.class_id AS enrollment ".
		"FROM class_members, classes LEFT OUTER JOIN class_enrollments ON class_enrollments.class_id = classes.class_id ".
		"WHERE class_members.user_id = ? AND class_members.class_id = classes.class_id";
		$result_array = $this->db->query($sql_query, $user_id)->result_array();
		for ($index = 0; $index < sizeof($result_array) ; $index++){
			$result_array[$index]['role'] = $this->role_num_to_string($result_array[$index]['role']);
			if (strlen($result_array[$index]['subject']) > 17){
					$result_array[$index]['subject'] = substr( $result_array[$index]['subject'], 0, 15).'...';
			}
		}
		uasort($result_array, array('user_model', 'compare_class_hours'));
		return $result_array;
	}
	
	public function get_students($class_id){
			$sql_query = "SELECT user_profiles.first_name,user_profiles.last_name,user_profiles.user_id FROM user_profiles, class_members WHERE class_members.class_id = ? AND class_members.role = ? AND class_members.user_id = user_profiles.user_id";
		
		$sql_row = array(	'class_id' => $class_id,
							'role' => STUDENT
		);
		$result_array = $this->db->query($sql_query, $sql_row)->result_array();
		uasort($result_array, array('user_model', 'compare_student_names'));
		return $result_array;
	}
	
	public function compare_student_names($student1, $student2){
		if ( $student1['last_name'] > $student2['last_name']) return 1;
		else if ( $student1['last_name'] < $student2['last_name']) return -1;
		else return 0;
	}
	
	public function compare_class_hours($num1, $num2){
		if ( $num1['class_hour'] > $num2['class_hour'] ) return 1;
		else if ( $num1['class_hour'] < $num2['class_hour'] ) return -1;
		else return 0;
	}
	
	public function role_num_to_string($role){
		if ( $role == TEACHER)
			return 'Teacher';
		else if ($role == STUDENT)
			return 'Student';
		else if ($role == ADMIN)
			return 'Admin';
	}
		
	public function material_type_code($type){
		if ($type == 'test')
			return TEST;
		else if ($type == 'worksheet')
			return WORKSHEET;
		else if ($type == 'quiz')
			return QUIZ;
		else if ($type == 'lesson_plan')
			return LESSON_PLAN; 
		else if ($type == 'activity')
			return ACTIVITY;
		else if ($type == 'article')
			return ARTICLE;
		else if ($type == 'diagram')
			return DIAGRAM;
		else if ($type == 'other')
			return OTHER;
		else
			return FALSE;
	}
	
	public function material_type_string($type_id){
		if ($type_id == TEST)
			return 'Test';
		else if ($type_id == WORKSHEET)
			return 'Worksheet';
		else if ($type_id == QUIZ)
			return 'Quiz';
		else if ($type_id == LESSON_PLAN)
			return 'Lesson Plan'; 
		else if ($type_id == ACTIVITY)
			return 'Activity';
		else if ($type_id == ARTICLE)
			return 'Article';
		else if ($type_id == DIAGRAM)
			return 'Diagram';
		else if ($type_id == OTHER)
			return 'Other';
		else
			return '-';
	}

	/* helper function to ensure that all dates are displayed uniformly on the site
	 * returns the column specified as an alias for the fomatted date.
	 */
	public function mysql_date_format($column_name){
		return 'LOWER(DATE_FORMAT(' . $column_name . ", '%c/%e/%y %l:%i%p')) as " . $column_name;
	}

	/* helper function to standardize how result lists handle limiting results for pagination
	 */
	public function limit_results($per_page, $curr_pagination_page){
		return 'limit ' . $per_page * ($curr_pagination_page - 1) . ', ' . $per_page; 
	}

	/*
	 * Grabs all of the documents accessible to a user.
	 * Arguments:
	 * user_id - current user
	 * per_page - number of results to show on each page when seperating results
	 * curr_pagination_page - the page that the user has navigated to in the pagination of the results
	 */	
	public function get_docs($user_id, $per_page, $curr_pagination_page){
		$sql_query = "SELECT " . $this->doc_description_fields . " FROM documents where owner_user_id = ? " . $this->limit_results($per_page, $curr_pagination_page);
		$result_array = $this->db->query($sql_query, $user_id)->result_array();
		for ($index = 0; $index < sizeof($result_array) ; $index++){
			if (strlen($result_array[$index]['docname']) > 25){
					$result_array[$index]['docname'] = substr( $result_array[$index]['docname'], 0, 24).'...';
			}
			$result_array[$index]['material_type'] = $this->material_type_string($result_array[$index]['material_type']);
			$result_array[$index]['tags'] = $this->get_doc_tags($result_array[$index]['doc_id']);
		}
		return $result_array;
	}

	public function get_doc_count($user_id){
		$result_array = $this->db->query('select count(*) as doc_count from documents where owner_user_id = ?', array($user_id))->result_array();
		return $result_array[0]['doc_count'];
	}

	public function get_sudent_assignment_count($user_id, $class_id){
		$result_array = $this->db->query('select count(*) as assignment_count from assignments as a, assignments_and_users as a_u where a_u.user_id = ? AND a_u.assignment_id = a.assignment_id AND a.class_id = ?')->result_array();
		return $result_array[0]['assignment_count'];	
	}
	
	public function get_student_assignments($user_id, $class_id, $per_page, $curr_pagination_page){
		// TODO - remove *'s  
		$sql_query = "SELECT a.assignment_name, a.assignment_id, a.total_points, " . $this->mysql_date_format('assign_date') . ", " . $this->mysql_date_format('due_date') . ", CASE WHEN a_u.submit_time = '00/00/0000' THEN NULL ELSE " . $this->mysql_date_format('submit_time') . ") END AS submit_time FROM assignments_and_users as a_u,assignments as a WHERE a_u.user_id = ? AND a_u.assignment_id = a.assignment_id AND a.class_id = ? " . $this->limit_results($per_page, $curr_pagination_page);
		$result_array = $this->db->query($sql_query, array( 'user_id' => $user_id, 'class_id' => $class_id))->result_array();
		for ($index = 0; $index < sizeof($result_array) ; $index++){
			if (strlen($result_array[$index]['assignment_name']) > 25){
				$result_array[$index]['assignment_name'] = substr( $result_array[$index]['assignment_name'], 0, 24).'...';
			}
		}
		return $result_array;
	}
	
	public function get_teacher_assignments($user_id, $class_id){	
		$sql_query = "SELECT assignment_id, assignment_name, " . $this->mysql_date_format('assign_date') . ', ' . $this->mysql_date_format('due_date') . ", total_points, notes FROM assignments WHERE class_id = ?";
		$result_array = $this->db->query($sql_query, array('class_id' => $class_id))->result_array();
		for ($index = 0; $index < sizeof($result_array) ; $index++){
			if (strlen($result_array[$index]['assignment_name']) > 25){
				$result_array[$index]['assignment_name'] = substr( $result_array[$index]['assignment_name'], 0, 24).'...';
			}
		}
		return $result_array;
	}
	
	public function teacher_has_assignment($user_id, $assignment_id){
		$sql_query = 'SELECT class_members.user_id from assignments, class_members WHERE assignments.class_id =
				class_members.class_id AND class_members.role = ? AND class_members.user_id = ? AND assignments.assignment_id = ?';
		$query = $this->db->query($sql_query, array( 'role' => TEACHER, 'user_id' => $user_id, 'assignment_id' => $assignment_id));
		if ($query->num_rows == 0){
			return FALSE;
		}
		return TRUE;
	}
	
	public function get_student_submissions($assignment_id){
		// should remove the * here
		$query = 'SELECT a_u.*, u_p.first_name,u_p.last_name FROM assignments_and_users as a_u,
				user_profiles as u_p WHERE a_u.assignment_id = ? AND a_u.user_id = u_p.user_id';
		return $this->db->query($query, $assignment_id)->result_array();
	}
	
	public function search_user_docs($user_id, $tags, $max_docs){
		$search_results = array();
		$used_already = FALSE;
		foreach ($tags as $tag){
			$sql_query = 'SELECT documents.* FROM documents,document_tags,tags WHERE documents.owner_user_id = ? AND
				documents.doc_id = document_tags.doc_id AND tags.tag = ? AND tags.tag_id = document_tags.tag_id';
			$result_array = $this->db->query($sql_query, array('owner_user_id' => $user_id, 'tag' => $tag))->result_array();
			foreach($result_array as $result){
				$used_already = FALSE;
				foreach ($search_results as $prev_result){
					if ($result['doc_id'] == $prev_result['doc_id'])
						$used_already = TRUE;
				}
				if ( ! $used_already){
					$result['tags'] = $this->get_doc_tags($result['doc_id']);
					$result['material_type'] = $this->material_type_string($result['material_type']);
					array_push($search_results, $result);
				}
			}
		}
		
		return $search_results;
	}
	
	public function get_doc_tags($doc_id){
		$sql_query = 'SELECT tags.tag FROM tags, document_tags
				WHERE tags.tag_id = document_tags.tag_id AND document_tags.doc_id = ?';
		$result_array = $this->db->query($sql_query, array('document_doc_id' => $doc_id))->result_array();
		$tags = array();
		foreach ($result_array as $tag){
			array_push($tags, $tag['tag']);
		}
		return $tags;
	}
	
	public function add_class($class_data, $user_id){
		$sql_row = array(	'subject' => $class_data['subject'],
							'class_hour' => $class_data['class_hour'],
							'create_date' => date('Y-m-d H:i'));
		$this->db->insert('classes', $sql_row);
		unset($sql_row);
		$sql_row = array(	'class_id' => $this->db->insert_id(),
						 	'user_id' => $user_id,
						 	'role' => TEACHER,
		);
		$this->db->insert('class_members', $sql_row);
	}
	
	public function user_has_assignment($user_id, $assignment_id){
		//check to see if the user has access to downloading/submitting the assignment
		// this can be done in the controller
		$sql_query = "SELECT * FROM assignments_and_users WHERE assignments_and_users.user_id = ? AND
				assignments_and_users.assignment_id = ?";
		
		$query = $this->db->query($sql_query, array( 'user_id' => $user_id, 'assignment_id' => $assignment_id));
		if ($query->num_rows == 0){
			return FALSE;
		}
		return TRUE;
	}
	
	public function add_assignment($assignment_data){
		$this->db->insert('assignments', $assignment_data);
		$assignment_id = $this->db->insert_id();
		foreach ($this->get_students($assignment_data['class_id']) as $student){
			$this->db->insert('assignments_and_users', array('assignment_id' => $assignment_id, 'user_id' => $student['user_id']));
		}
	}
}
?>
