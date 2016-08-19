<?php
  /**
   *
   */
  class Events
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
    function get_upcoming_events($id){
      $now = new DateTime();
      $date=$now_string =$now->format("YmdHi");
      $response["upcoming"]=array();
      if($id==0){
        $response["ongoing"]=array();
        $query="SELECT * FROM events WHERE id>$id AND $date>=start_time AND $date<=end_time LIMIT 10";
        $rows=$this->db_connect_get_many($query);
        $response["ongoing"]=array();
        foreach ($rows as $row ) {
          array_push($response["ongoing"],$row);
        }
      }else{
        $response["code"]=1;
      }
      $query="SELECT * FROM events WHERE id>$id AND $date<start_time LIMIT 10";
      $rows=$this->db_connect_get_many($query);
      foreach ($rows as $row ) {
        array_push($response["upcoming"],$row);
      }
      if(sizeof($response["upcoming"])==0){
        $response["success"]=0;
        unset($response["upcoming"]);
        return $response;
      }else{
        $response["success"]=1;
        return $response;
      }
    }
    function get_ongoing_events($id){
      $now = new DateTime();
      $date=$now_string =$now->format("YmdHi");
      $query="SELECT * FROM events WHERE id>$id AND $date>=start_time AND $date<=end_time LIMIT 10";
      $rows=$this->db_connect_get_many($query);
      $response["ongoing"]=array();
      if($id!=0){
        $response["code"]=0;
      }
      foreach ($rows as $row ) {
        array_push($response["ongoing"],$row);
      }
      if(sizeof($response["ongoing"])==0){
        $response["success"]=0;
        unset($response["ongoing"]);
        return $response;
      }else{
        $response["success"]=1;
        return $response;
      }
    }
    function get_user_events_attending($user,$me){
      include_once 'InfoExchange.php';
      $exchange=new InfoExchange();
      if($exchange->is_private($user)){
        if($exchange->is_following($me,$user)){
          return $this->get_user_events_attending_allowed($user);
        }else{
          return array('success' =>0 ,'message'=>"This account is private");
        }
      }else{
        return $this->get_user_events_attending_allowed($user);
      }
    }
    function get_user_events_hosting($me,$user){
      include_once 'InfoExchange.php';
      $exchange=new InfoExchange();
      if($exchange->is_private($user)){
        if($exchange->is_following($me,$user)){
          return $this->get_user_events_hosting_allowed($user);
        }else{
          return array('success' =>0 ,'message'=>"This account is private");
        }
      }else{
        return $this->get_user_events_hosting_allowed($user);
      }
    }
    function get_user_events_attending_allowed($user){
      $query="SELECT event FROM attending WHERE user=$user";
      $rows=$this->db_connect_get_many($query);
      $att=array();
      foreach ($rows as $row) {
        $party_id=$row["event"];
        $query="SELECT * FROM events WHERE id=$party_id";
        $info=$this->db_connect_get_one($query);
        array_push($att,$info);
      }
      $att["success"]=1;
      return $att;
    }
    function get_user_events_hosting_allowed($user){
      $query="SELECT * FROM events WHERE event_type=0 AND host_id=$user";
      $rows=$this->db_connect_get_many($query);
      $att=array();
      foreach ($rows as $row) {
        array_push($att,$row);
      }
      $att["success"]=1;
      return $att;
    }
    function invite_user($me,$user,$event){
      $query="SELECT 1 FROM invites WHERE event=$event AND inviter=$me AND invitee=$user";
      $check=$this->db_connect_get_many($query);
      if($check){
        $query="SELECT state FROM invites WHERE event=$event AND inviter=$me AND invitee=$user";
        $corr=$this->db_connect_get_one($query);
        if($corr["state"]==0){
          $response["message"]="User rejected this request";
        }else if ($corr["state"]==1){
          $response["message"]="User is already attending this event";
        }else{
          $response["message"]="User hasn't respondent to this request yet";
        }
        $response["success"]=0;
      }else{
        $query="INSERT INTO invites (event,inviter,invitee,state) VALUES ($event,$me,$user,-1)";
        $this->db_connect_get_none($query);
        $response["success"]=1;
        $response["message"]="User successfully invited to this event";
      }
      return $response;
    }
    function respond_to_invite($requst_id,$accept){
      if($accept==1){
        $query="SELECT event FROM invites WHERE id=$requst_id";
        $row=$this->db_connect_get_one($query);
        $event=$row["event"];
        $query="UPDATE events SET attending=attending+1 WHERE id=$event";
        $this->db_connect_get_none($query);
        $query="UPDATE invites SET state=1 WHERE id=$requst_id";
        $this->db_connect_get_none($query);
      }else{
        $query="SELECT state,event FROM invites WHERE id=$requst_id";
        $row=$this->db_connect_get_one($query);
        if($row["state"]==1){
            $event=$row["event"];
            $query="UPDATE events SET attending=attending-1 WHERE id=$event";
            $this->db_connect_get_none($query);
        }
        $query="UPDATE invites SET state=0 WHERE id=$requst_id";
        $this->db_connect_get_none($query);
      }
      $response["success"]=1;
      $response["message"]="Thank you for responding";
      return $response;
    }
    function get_events_users_attending($event){
      $query="SELECT user FROM attending WHERE event=$event";
      $people=$this->db_connect_get_many($query);
      include_once 'InfoExchange.php';
      $exchange=new InfoExchange();
      $response["people"]=array();
      foreach ($people as $person) {
        array_push($response["people"],$exchange->get_user_basic_info($person["user"]));
      }
      $response["success"]=1;
      return $response;
    }
    function get_invitable_list($event,$me){
      include_once 'InfoExchange.php';
      $exchange=new InfoExchange();
      $followers=$exchange->get_my_followers($me)["people"];
      $response["people"]=array();
      foreach ($followers as $person) {
        if(!$exchange->is_attending($event,$person["id"])){
          if($exchange->is_invited($event,$person["id"],$me)){
            $person["invited"]=1;
          }else{
            $person["invited"]=0;
          }
          array_push($response["people"],$person);
        }
      }
      $response["success"]=1;
      return $response;
    }
  }

?>
