<?php
	if(!empty($_POST)){
		require("dblogin.php");
		$user=$_POST["user"];
		$pass=sha1($_POST["pass"]);
		$email=$_POST["email"];
		$name=$_POST["name"];
		$surname=$_POST["surname"];
		$bday=$_POST["bday"];
		$phone=$_POST["phone"];
		$image=$_POST["image"];
		$ext=$_POST["ext"];
		$regid=$_POST["regid"];
		$gender=$_POST["gender"];
		$query_params = array(':euser' => $user);
		$query="SELECT 1 from user where username=:euser";
		try {
			$stmp=$db->prepare($query);
			$result=$stmp->execute($query_params);
		} catch (Exception $e) {
			die($e);
		}
		$check_username=$stmp->fetch();
		if($check_username){
			$response["success"]=0;
			$response["message"]='Username is already in use';
			$response["error"]=201;
			die(json_encode($response));
		}else{
			$query_params = array(':eemail'=>$email);
			$query="SELECT 1 from user where email=:eemail";
			try {
				$stmp=$db->prepare($query);
				$result=$stmp->execute($query_params);
			} catch (Exception $e) {
				die($e);
			}	
			$check_email=$stmp->fetch();
			if($check_email){
				$response["success"]=0;
				$response["message"]='Email is already in use';
				$response["error"]=202;
				die(json_encode($response));
			}else{
				$query_params = array(':ephone'=>$phone);
				$query="SELECT 1 from user where phone=:ephone";
				try {
					$stmp=$db->prepare($query);
					$result=$stmp->execute($query_params);
				} catch (Exception $e) {
					die($e);
				}	
				$check_phone=$stmp->fetch();
				if ($check_phone) {
					$response["success"]=0;
					$response["message"]='Phone number is already in use';
					$response["error"]=203;
					die(json_encode($response));					
				} else {
					$query_params = array(':euser' => $user,':epass'=>$pass,':eemail'=>$email,':ename'=>$name,':esurname'=>$surname,':ebday'=>$bday,':ephone'=>$phone,':eimage'=>$image,':eregid'=>$regid,':egender'=>$gender);
					$query="INSERT INTO user (username,password,email,name,surname,birthday,phone,status,image,pp,loyalty,state,private,regid,gender) VALUES (:euser,:epass,:eemail,:ename,:esurname,:ebday,:ephone,'Lets get turnt','',:eimage,0,0,0,:eregid,:egender)";
					try {
						$stmp=$db->prepare($query);
						$result=$stmp->execute($query_params);
					} catch (Exception $e) {
						die($e);
					}
					$response["success"]=1;
					$response["message"]='Welcome to turntapp '.$name;
					$query="SELECT id FROM user WHERE email=:eemail";
					$query_params = array(':eemail'=>$email);
					try {
						$stmp=$db->prepare($query);
						$result=$stmp->execute($query_params);
					} catch (Exception $e) {
						die($e);
					}
					$row=$stmp->fetch();
					$response["id"]=$row["id"];
					die(json_encode($response));
				}
				
			}
		}
	}else{
?>
<form action="SignUp.php" method="POST">
	Username <input type="text" name="user" value="Jackson"><br>
	Password<input type="text" name="pass" value="Jackson"><br>
	Email <input type="text" name="email" value="aksddjk"><br>
	Name <input type="text" name="name" value="Jackson"><br>
	Surname <input type="text" name="surname" value="Dyora"><br>
	BDay <input type="text" name="bday" value="01/02/1993"><br>
	Phone <input type="text" name="phone" value="aldkajdlk"><br>
	Image <input type="text" name="image" value="Jackson"><br>
	Ext <input type="text" name="ext" value="jpg"><br>
	Regid <input type="text" name="regid" value="null"><br>
	<select name="gender">
		<option value="0">Male</option>
		<option value="1">Female</option>
	</select>
	<input type="submit" value="Submit">
</form>
<?php
}
?>