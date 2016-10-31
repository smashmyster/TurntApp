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
    function get_upcoming_events($id,$user){
      include_once 'InfoExchange.php';
      $exchange=new InfoExchange();
      $now = new DateTime();
      $date=$now_string =$now->format("YmdHi");
      if($id==0){
        $response["ongoing"]=array();
        $query="SELECT * FROM events WHERE id>$id AND start_time<$date AND end_time>$date  LIMIT 10";
        $rows=$this->db_connect_get_many($query);
        $response["ongoing"]=array();
        foreach ($rows as $row ) {
          $query="SELECT * FROM attending WHERE user=$user AND event=:eid";
          $jaa=$this->db_connect_get_many($query,array(':eid'=>$row["id"]));
          if($jaa){
            $row["me_attending"]=1;
          }else{
            $row["me_attending"]=0;
          }
          if($row["event_type"]==1){
              $row["host_name"]=$exchange->get_club_info($row["host_id"])["club_info"]["name"];
            }else{
              $row["host_name"]=$exchange->get_user_basic_info($row["host_id"])["name"];
            }
          array_push($response["ongoing"],$row);
        }
      }else{
        $response["code"]=1;
      }
      $query="SELECT * FROM events WHERE id>$id AND start_time>$date LIMIT 10";
      $rows=$this->db_connect_get_many($query);
      $response["upcoming"]=array();
      foreach ($rows as $row ) {
        $io=$row["id"];
        $query="SELECT * FROM attending WHERE user=$user AND event=$io";
        $jaa=$this->db_connect_get_many($query);
        if($jaa){
          $row["me_attending"]=1;
        }else{
          $row["me_attending"]=0;
        }
        if($row["event_type"]==1){
           $row["host_name"]=$exchange->get_club_info($row["host_id"])["club_info"]["name"];
        }else{
          $row["host_name"]=$exchange->get_user_basic_info($row["host_id"])["name"];
        }
        array_push($response["upcoming"],$row);
      }
      $query="SELECT * FROM invites WHERE invitee=$user AND state=-1";
      $ass=$this->db_connect_get_many($query);
      $response["invites"]=sizeof($ass);

      $query="SELECT * FROM followers WHERE follower=$user";
      $ass=$this->db_connect_get_many($query);
      $response["following"]=sizeof($ass);

      $query="SELECT * FROM followers WHERE following=$user";
      $ass=$this->db_connect_get_many($query);
      $response["followers"]=sizeof($ass);

      $query="SELECT image_name FROM user WHERE id=$user";
      $ass=$this->db_connect_get_one($query);
      $response["image_name"]=$ass["image_name"];

      if(sizeof($response["upcoming"])==0){
        $response["success"]=1;
        return $response;
      }else{
        $response["success"]=1;
        return $response;
      }
    }
    function get_ongoing_events($id,$user){
      $now = new DateTime();
      $date=$now_string =$now->format("YmdHi");
      $query="SELECT * FROM events WHERE id>$id AND $date>=start_time AND $date<=end_time LIMIT 10";
      $rows=$this->db_connect_get_many($query);
      include_once 'InfoExchange.php';
      $exchange=new InfoExchange();
      $response["ongoing"]=array();
      if($id!=0){
        $response["code"]=0;
      }
      foreach ($rows as $row ) {
        $io=$row["id"];
        $query="SELECT * FROM attending WHERE user=$user AND id=$io";
        $jaa=$this->db_connect_get_one($query);
        if(sizeof($jaa)>0){
          $row["me_attending"]=1;
        }else{
          $row["me_attending"]=0;
        }
        if($row["event_type"]==1){
   $row["host_name"]=$exchange->get_club_info($row["host_id"])["club_info"]["name"];
}else{
  $row["host_name"]=$exchange->get_user_basic_info($row["host_id"])["name"];
}
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
      $att["data"]=array();
      foreach ($rows as $row) {
        $party_id=$row["event"];
        $query="SELECT * FROM events WHERE id=$party_id";
        $info=$this->db_connect_get_one($query);
        $info["me_attending"]=1;
        array_push($att["data"],$info);
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
        $query="SELECT regid FROM user WHERE id=$user";
        $reg=$this->db_connect_get_one($query)["regid"];
        $query="SELECT id,name,logo FROM events WHERE id=$event";
        $event_info=$this->db_connect_get_one($query);
        $tokens=array();
        if($reg!=""){
          array_push($tokens,$reg);
          include_once 'SendGCM.php';
          $gcm=new GCM();
          $message=array();
          $message["id"]=$event_info["id"];
          $message["name"]=$event_info["name"];
          $message["logo"]=$event_info["logo"];
          $gcm->send_invite($tokens,$message);
        }
        $response["success"]=1;
        $response["message"]="User successfully invited to this event";
      }
      return $response;
    }
    function respond_to_invite($event,$user,$accept){
      $query="SELECT * FROM invites WHERE invitee=$user AND event=$event";
      $po=$this->db_connect_get_one($query);
      $requst_id=$po["id"];
      if($accept==1){
        $query="SELECT event FROM invites WHERE id=$requst_id";
        $row=$this->db_connect_get_one($query);
        $event=$row["event"];
        // echo $event;
        $query="UPDATE events SET attending=attending+1 WHERE id=$event";
        $this->db_connect_get_none($query);
        $query="UPDATE invites SET state=1 WHERE id=$requst_id";
        $this->db_connect_get_none($query);
        $this->attend_event($user,$event);
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
            array_push($response["people"],$person);
          }
        }
      }
      $response["id"]=$event;
      $response["success"]=1;
      return $response;
    }
    function lookup($string){

      $string = str_replace (" ", "+", urlencode($string));
      $details_url = "http://maps.googleapis.com/maps/api/geocode/json?address=".$string."&sensor=false";

      $ch = curl_init();
      curl_setopt($ch, CURLOPT_URL, $details_url);
      curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
      $response = json_decode(curl_exec($ch), true);

   // If Status Code is ZERO_RESULTS, OVER_QUERY_LIMIT, REQUEST_DENIED or INVALID_REQUEST
      if ($response['status'] != 'OK') {
        return null;
      }
      $geometry = $response['results'][0]['geometry'];

      $longitude = $geometry['location']['lat'];
      $latitude = $geometry['location']['lng'];

      $data=$geometry['location']['lat'].','.$geometry['location']['lng'];

      return $data;

    }
    function user_create_event($name,$address,$me,$djs,$specials,$gen_fee,$vip_fee,$start_time,$end_time,$ext,$pic){
        $image_name=$name.'_'.$start_time.'.'.$ext;
        $latlong=$this->lookup($address);
        $this->get_image($pic,$image_name);
        $query="SELECT 1 FROM events WHERE name=:ename AND address=:eaddress";
        $query_params=array(':ename'=>$name,'eaddress'=>$address);
        $check=$this->db_connect_get_many($query,$query_params);
        if($check){
          $response["success"]=0;
          $response["message"]="This event has already been created ";
        }else{
          $query="INSERT INTO events (name,address,latlong,host_id,djs,specials,gen_fee,vip_fee,start_time,end_time,event_type,logo)
                          VALUES (:ename,:eaddress,:elatlong,:eme,:edjs,:especials,:egen_fee,:evip_fee,:estart_time,:eend_time,0,:eimage_name)
          ";
          $query_params = array(':ename'=>$name,
          ':eaddress'=>$address,
          ':elatlong'=>$latlong,
          ':eme'=>$me,
          ':edjs'=>$djs,
          ':especials'=>$specials,
          ':egen_fee'=>$gen_fee,
          ':evip_fee'=>$vip_fee,
          ':estart_time'=>$start_time,
          ':eend_time'=>$end_time,
          ':eimage_name'=>$image_name);
          $this->db_connect_get_none($query,$query_params);
          $query="SELECT id FROM events ORDER BY id DESC LIMIT 1";
          $get=$this->db_connect_get_one($query);
          $response["id"]=$get["id"];
          $response["success"]=1;
          $response["message"]="Event successfully created";
          $response["people"]=$this->get_invitable_list($get["id"],$me)["people"];
        }
        return $response;
    }
    function get_image($pic,$filename){
          // Get file name posted from Android App

          // Decode Image
          $binary=base64_decode($pic);

          //header('Content-Type: bitmap; charset=utf-8');
          // Images will be saved under 'www/imgupload/uplodedimages' folder
          $file = fopen('EventImages/'.$filename, 'w');
          // Create File
          fwrite($file, $binary);
          fclose($file);
    }
    function get_event_by_id($id){
      $query="SELECT * FROM events WHERE id=$id";
      return $this->db_connect_get_one($query);
    }
    function attend_event($id,$event){
      $query="INSERT INTO attending (event,user) VALUES ($event,$id)";
      $this->db_connect_get_none($query);
      include('QRCreator/qrlib.php');
      $codeContents = $id."_".$event;

      // we need to generate filename somehow,
      // with md5 or with database ID used to obtains $codeContents...
      $fileName = $id."_".$event.'.png';
      $pngAbsoluteFilePath = "BarCodes/".$fileName;
      // generating
      // QRcode::png($codeContents, $pngAbsoluteFilePath);
      QRcode::png($codeContents, $pngAbsoluteFilePath,QR_ECLEVEL_L, 80);

      // displaying
      return array('success'=>1,'message'=>'attending');
    }
    function unattend_event($id,$event){
      $query="DELETE FROM attending WHERE event=$event AND user=$id";
      $this->db_connect_get_none($query);
      return array('success'=>1,'message'=>'unattending');
    }
    function get_events($me){
      // echo $me;
      $query="SELECT * FROM invites WHERE invitee=$me AND state=-1";
      $data=$this->db_connect_get_many($query);
      include_once 'InfoExchange.php';
      $exchange=new InfoExchange();
      $response["data"]=array();
      foreach ($data as $key) {
        $query="SELECT * FROM events WHERE id=:eid";
        $send["event"]=$this->db_connect_get_one($query,array(':eid'=>$key["event"]));
        $send["inviter"]=$exchange->get_user_basic_info($key["inviter"]);
        array_push($response["data"],$send);
      }
      $response["success"]=1;
      return $response;
    }
  }

?>
