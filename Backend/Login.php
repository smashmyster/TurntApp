<?php
	if(!empty($_POST)){
		require("dblogin.php");
		$user=$_POST["user"];
		$pass=sha1($_POST["pass"]);
		$query_params= array(':ename' => $user,':epass'=>$pass);
		$query="SELECT 1 from user where username=:ename OR email=:ename";
		try {
			$stmp=$db->prepare($query);
			$result=$stmp->execute($query_params);
		} catch (Exception $e) {
			die($e);
		}
		$row=$stmp->fetch();
		if($row){
			$query="SELECT * from user where (username=:ename OR email=:ename) AND password=:epass";
			try {
				$stmp=$db->prepare($query);
				$result=$stmp->execute($query_params);
			} catch (Exception $e) {
				die($e);
			}	
			$get=$stmp->fetch();
			if($get){
				$response["success"]=1;
				$response["id"]=$get["id"];
				$response["user"]=$get["username"];
				$response["pass"]=$get["password"];
				$response["name"]=$get["name"];
				$response["surname"]=$get["surname"];
				$response["email"]=$get["email"];
				$response["phone"]=$get["phone"];
				$response["bday"]=$get["birthday"];
				$response["image"]=$get["image"];
				$response["pp"]=$get["pp"];
				$response["loyalty"]=$get["loyalty"];
				$response["status"]=$get["status"];
				$response["following"]=$get["following"];
				$response["followers"]=$get["followers"];
				$response["regid"]=$get["regid"];
				$response["private"]=$get["private"];
				$response["state"]=$get["state"];
				$response["gender"]=$get["gender"];
				die(json_encode($response));
			}else{
				$response["success"]=0;
				$response["error"]=403;
				$response["message"]="You entered a wrong password please try again";
				die(json_encode($response));	
			}
		}else{
			$response["success"]=0;
			$response["error"]=404;
			$response["message"]="This username is not registered please double check";
			die(json_encode($response));
		}
	}else{
?>
	<form action ="Login.php"method="post">
		Username
		<input type="text" name="user" placeholder="username" value="Jackson"><br>
		<br>
		Password
		<input type="text" name="pass" placeholder="password" value="Taps">
		<br>
		<input type="submit" value="submit">
	</form>		
<?php
	}
?>