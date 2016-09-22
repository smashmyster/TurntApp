<?php
  class InfoExchange{
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
    function get_club_info($id){
      $query="SELECT * FROM club_info WHERE id=$id";
      $info=$this->db_connect_get_one($query);
      $query="SELECT id FROM events WHERE event_type=1 AND host_id=$id";
      $data=$this->db_connect_get_many($query);
      $response["club_info"]=$info;
      $response["events_num"]=sizeof($data);
      return $response;
    }
    function get_user_info($me,$person){
      $query="SELECT * FROM followers WHERE follower=$person AND following=$me";
      $check=$this->db_connect_get_many($query);
      $query="SELECT * FROM user WHERE id=$person";
      $info=$this->db_connect_get_one($query);
      $query="SELECT * FROM events WHERE event_type=0 AND host_id=$person";
      $data=$this->db_connect_get_many($query);
      $num=sizeof($data);
      $info["num"]=$num;
      unset($info["password"]);
      unset($info["pp"]);
      unset($info["email"]);
      unset($info["phone"]);
      if(sizeof($check)>=1){
        $info["following"]=1;
        return $info;
      }else{
        unset($info["birthday"]);
        $info["following"]=0;
        if($info["private"]==0){
          //unset($info["birthday"]);
          return $info;
        }else{
          return $info;
        }
      }
    }
    function get_user_basic_info($user){
      $query="SELECT id,name,surname,image_name,following,followers,status FROM user WHERE id=$user";
      return $this->db_connect_get_one($query);
    }
    function is_following($me,$user){
      $query="SELECT 1 FROM followers WHERE follower=$me AND following=$user";
      $check_follow=$this->db_connect_get_many($query);
      return $check_follow;
    }
    function is_private($user){
      $query="SELECT private FROM user WHERE id=$user";
      $check=$this->db_connect_get_one($query);
      if($check["private"]==1){
        return true;
      }else{
        return false;
      }
    }
    function get_my_followers($me){
      $query="SELECT follower FROM followers WHERE following=$me";
      $followers=$this->db_connect_get_many($query);
      $response["people"]=array();
      foreach ($followers as $get) {
        array_push($response["people"],$this->get_user_basic_info($get["follower"]));
      }
      $response["success"]=1;
      return $response;
    }
    function is_attending($event,$user){
      $query="SELECT 1 FROM attending WHERE event=$event AND user=$user";
      $check=$this->db_connect_get_many($query);
      return $check ? true:false;
    }
    function is_invited($event,$user,$me){
      $query="SELECT 1 FROM invites WHERE event=$event AND inviter=$me AND invitee=$user";
      $check=$this->db_connect_get_many($query);
      return $check ? true:false;
    }
    function search($text,$me){
      $query="SELECT id FROM user WHERE name LIKE :ename OR surname LIKE :ename OR username LIKE :ename OR email LIKE :ename LIMIT 10";
      $query_params=array(':ename'=>'%'.$text.'%');
      $data=$this->db_connect_get_many($query,$query_params);
      $response["data"]=array();
      foreach ($data as $key ) {
        $get=$this->get_user_basic_info($key["id"]);
        if($this->is_following($me,$key["id"])){
            $get["is_following"]=1;
        }else{
            $get["is_following"]=0;
        }
        array_push($response["data"],$get);
      }
      $response["success"]=1;
      return $response;
    }
    function search_event($name){
      $query="SELECT id,name FROM events WHERE name LIKE :ename";
      $query_params=array(':ename'=>'%'.$name.'%');
      $data=$this->db_connect_get_many($query,$query_params);
      if($data){
        $response["success"]=1;
        $response["data"]=array();
        foreach ($data as $key ) {
          array_push($response["data"],$key);
        }
      }else{
        $response["success"]=0;
        $response["message"]="Event not found";
      }
      return $response;
    }

  }
?>
