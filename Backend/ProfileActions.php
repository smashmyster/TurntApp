<?php

  class UserActions
  {
    function db_connect_get_none($query,$query_params=array()){
      require "dblogin.php";
      try {
        $stmp=$db->prepare($query);
        $result=$stmp->execute($query_params);
      } catch (Exception $e) {
        die($e);
      }

    }
    function db_connect_get_one($query,$query_params=array()){
      require "dblogin.php";
      try {
        $stmp=$db->prepare($query);
        $result=$stmp->execute($query_params);
      } catch (Exception $e) {
        die($e);
      }
      return $stmp->fetch();
    }
    function db_connect_get_many($query,$query_params=array()){
      require "dblogin.php";
      try {
        $stmp=$db->prepare($query);
        $result=$stmp->execute($query_params);
      } catch (Exception $e) {
        die($e);
      }
      return $stmp->fetchAll();
    }
    function sign_up($user,$pass,$email,$name,$surname,$bday,$phone,$image,$ext,$regid,$gender){
      $query="SELECT 1 from user where username=:euser";
      $query_params = array(':euser' => $user);
      $check_username=$this->db_connect_get_many($query,$query_params);
      if($check_username){
        $response["success"]=0;
        $response["message"]='Username is already in use';
        $response["error"]=201;
        return $response;
      }else{
        $query_params = array(':eemail'=>$email);
        $query="SELECT 1 from user where email=:eemail";
        $check_email=$this->db_connect_get_many($query,$query_params);
        if($check_email){
          $response["success"]=0;
          $response["message"]='Email is already in use';
          $response["error"]=202;
          return $response;
        }else{
          $query_params = array(':ephone'=>$phone);
          $query="SELECT 1 from user where phone=:ephone";
          $check_phone=$this->db_connect_get_many($query,$query_params);
          if ($check_phone) {
            $response["success"]=0;
            $response["message"]='Phone number is already in use';
            $response["error"]=203;
            return $response;
          } else {
            $image_name=$this->get_image($image,$ext,$user);
            $query_params = array(':euser' => $user,':epass'=>$pass,':eemail'=>$email,':ename'=>$name,':esurname'=>$surname,':ebday'=>$bday,':ephone'=>$phone,':eimage'=>$image_name,':eregid'=>$regid,':egender'=>$gender);
            $query="INSERT INTO user (username,password,email,name,surname,birthday,phone,status,image_name,loyalty,state,private,regid,gender) VALUES (:euser,:epass,:eemail,:ename,:esurname,:ebday,:ephone,'Lets get turnt',:eimage,0,0,0,:eregid,:egender)";
            $this->db_connect_get_none($query,$query_params);
            $response["success"]=1;
            $response["message"]='Welcome to turntapp '.$name;
            $query="SELECT id FROM user WHERE email=:eemail";
            $query_params = array(':eemail'=>$email);
            $row=$this->db_connect_get_one($query,$query_params);
            $response["id"]=$row["id"];
            $response["code"]=1;
            return $response;
          }
        }
      }
    }
    function login($user,$pass){
      $query_params= array(':ename' => $user,':epass'=>$pass);
  		$query="SELECT 1 from user where username=:ename OR email=:ename";
  		$row=$this->db_connect_get_many($query,$query_params);
  		if($row){
  			$query="SELECT * from user where (username=:ename OR email=:ename) AND password=:epass";
  			$get=$this->db_connect_get_one($query,$query_params);
  			if($get){
          unset($get["password"]);
  				$get["success"]=1;
          $get["code"]=0;
  				return $get;
  			}else{
  				$response["success"]=0;
  				$response["error"]=403;
  				$response["message"]="You entered a wrong password please try again";
  				return $response;
  			}
  		}else{
  			$response["success"]=0;
  			$response["error"]=404;
  			$response["message"]="This username is not registered please double check";
  		  return $response;
  		}
    }
    function get_image($pic,$ext,$name){
          // Get file name posted from Android App

          // Decode Image
          $binary=base64_decode($pic);
          $filename=$name.'.'.$ext;
          //header('Content-Type: bitmap; charset=utf-8');
          // Images will be saved under 'www/imgupload/uplodedimages' folder
          $file = fopen('UserProfilePics/'.$filename, 'w');
          // Create File
          fwrite($file, $binary);
          fclose($file);
          return $filename;

    }
    function follow_user($follower,$following){
      $query="SELECT 1 from followers where follower=$follower AND following=$following";
		  $row=$this->db_connect_get_one($query);
		  if(!$row){
			  $query="SELECT 1 from followers where following=$follower AND follower=$following";
			  $check=$this->db_connect_get_one($query);
			if($check){
				$query="INSERT INTO followers (follower,following,state) VALUES ($follower,$following,1)";
        $this->db_connect_get_none($query);
				$query="UPDATE followers set state=1 WHERE following=$follower AND follower=$following";
        $this->db_connect_get_none($query);
			}else{
				$query="INSERT INTO followers (follower,following,state) VALUES ($follower,$following,0)";
        $this->db_connect_get_none($query);
			}
			$query="UPDATE user set followers=followers+1 WHERE id=$following";
      $this->db_connect_get_none($query);
			$query="UPDATE user set following=following+1 WHERE id=$follower";
      $this->db_connect_get_none($query);
		}else{
			$response["success"]=0;
			$response["error"]=204;
			$response["message"]="You are already following this user";
			return $response;
		}
		$response["success"]=1;
		$response["message"]="You are now following";
		return $response;
    }
    function unfollow_user($follower,$following){
      $query="SELECT 1 from followers where follower=$follower AND following=$following";
      $row=$this->db_connect_get_one($query);
      if($row){

        $query="SELECT 1 from followers where following=$follower AND follower=$following";
        $check=$this->db_connect_get_one($query);
      if($check){
        $query="DELETE FROM followers WHERE follower=$follower AND following=$following";
        $this->db_connect_get_none($query);
      }
      $query="UPDATE user set followers=followers-1 WHERE id=$following";
      $this->db_connect_get_none($query);
      $query="UPDATE user set following=following-1 WHERE id=$follower";
      $this->db_connect_get_none($query);
    }else{
      $response["success"]=0;
      $response["error"]=204;
      $response["message"]="You are already following this user";
      return $response;
    }
    $response["success"]=1;
    $response["message"]="You are now following";
    return $response;
    }
  }

?>
